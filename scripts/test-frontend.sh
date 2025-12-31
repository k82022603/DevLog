#!/bin/bash
# DevLog Frontend Test Script (Bash)
# This script tests the DevLog frontend application

# Configuration
BASE_URL="${1:-http://localhost:3000}"
BACKEND_URL="http://localhost:8080/api"

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Test counters
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Helper functions
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_failure() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${CYAN}ℹ $1${NC}"
}

print_section() {
    echo -e "\n${YELLOW}========================================"
    echo -e "  $1"
    echo -e "========================================${NC}\n"
}

# Test endpoint function
test_endpoint() {
    local url="$1"
    local test_name="$2"
    local expected_content="$3"
    local expected_status="${4:-200}"

    ((TOTAL_TESTS++))

    # Make request
    response=$(curl -s -w "\n%{http_code}" "$url" 2>&1)
    status_code=$(echo "$response" | tail -n1)
    content=$(echo "$response" | head -n-1)

    if [ "$status_code" = "$expected_status" ]; then
        if [ -n "$expected_content" ]; then
            if echo "$content" | grep -q "$expected_content"; then
                print_success "$test_name (Status: $status_code, Content matched)"
                ((PASSED_TESTS++))
            else
                print_failure "$test_name (Expected content '$expected_content' not found)"
                ((FAILED_TESTS++))
            fi
        else
            print_success "$test_name (Status: $status_code)"
            ((PASSED_TESTS++))
        fi
    else
        print_failure "$test_name (Expected status $expected_status, got $status_code)"
        ((FAILED_TESTS++))
    fi
}

# Test resource load
test_resource() {
    local url="$1"
    local resource_type="$2"

    ((TOTAL_TESTS++))

    # Use HEAD request to check resource
    status_code=$(curl -s -o /dev/null -w "%{http_code}" --head "$url")

    if [ "$status_code" = "200" ]; then
        # Get file size
        size=$(curl -s -I "$url" | grep -i "content-length" | awk '{print $2}' | tr -d '\r')
        if [ -n "$size" ]; then
            size_kb=$(echo "scale=2; $size / 1024" | bc)
            print_success "$resource_type loaded (Size: ${size_kb} KB)"
        else
            print_success "$resource_type loaded"
        fi
        ((PASSED_TESTS++))
    else
        print_failure "$resource_type (Status: $status_code)"
        ((FAILED_TESTS++))
    fi
}

# Main test execution
print_section "DevLog Frontend Test Suite"
print_info "Base URL: $BASE_URL"
print_info "Backend URL: $BACKEND_URL"
print_info "Timestamp: $(date '+%Y-%m-%d %H:%M:%S')"

# 1. Frontend Availability Test
print_section "1. Frontend Availability"
test_endpoint "$BASE_URL" "Frontend server is running" "<!DOCTYPE html>"

# 2. Main Pages Test
print_section "2. Main Pages"
test_endpoint "$BASE_URL/" "Home page (Dashboard)" "html"
test_endpoint "$BASE_URL/logs" "Logs page" "html"
test_endpoint "$BASE_URL/projects" "Projects page" "html"
test_endpoint "$BASE_URL/settings" "Settings page" "html"

# 3. Static Resources Test
print_section "3. Static Resources"

# Get index.html and extract asset references
index_html=$(curl -s "$BASE_URL")

# Extract CSS files (basic extraction)
css_files=$(echo "$index_html" | grep -oP 'href="([^"]*\.css)"' | cut -d'"' -f2 | head -3)

# Extract JS files (basic extraction)
js_files=$(echo "$index_html" | grep -oP 'src="([^"]*\.js)"' | cut -d'"' -f2 | head -3)

# Test CSS files
if [ -n "$css_files" ]; then
    while IFS= read -r css; do
        if [[ $css == http* ]]; then
            css_url="$css"
        else
            css_url="$BASE_URL/${css#/}"
        fi
        test_resource "$css_url" "CSS: $css"
    done <<< "$css_files"
fi

# Test JS files
if [ -n "$js_files" ]; then
    while IFS= read -r js; do
        if [[ $js == http* ]]; then
            js_url="$js"
        else
            js_url="$BASE_URL/${js#/}"
        fi
        test_resource "$js_url" "JS: $js"
    done <<< "$js_files"
fi

# 4. Backend API Integration Test
print_section "4. Backend API Integration"
print_info "Testing if frontend can reach backend API..."

((TOTAL_TESTS++))
backend_status=$(curl -s -o /dev/null -w "%{http_code}" "$BACKEND_URL/health")

if [ "$backend_status" = "200" ]; then
    print_success "Backend API is accessible ($BACKEND_URL/health)"
    ((PASSED_TESTS++))

    # Test other endpoints
    for endpoint in "projects:Projects API" "logs:Logs API"; do
        IFS=: read -r path name <<< "$endpoint"
        ((TOTAL_TESTS++))
        status=$(curl -s -o /dev/null -w "%{http_code}" "$BACKEND_URL/$path")
        if [ "$status" = "200" ]; then
            print_success "$name responding"
            ((PASSED_TESTS++))
        else
            print_failure "$name (Status: $status)"
            ((FAILED_TESTS++))
        fi
    done
else
    print_failure "Backend API not accessible (Status: $backend_status)"
    ((FAILED_TESTS++))
fi

# 5. Browser Console Test (Manual check reminder)
print_section "5. Browser Console Check"
print_info "Please manually check the following in your browser:"
echo "  1. Open http://localhost:3000 in Chrome/Firefox"
echo "  2. Open Developer Tools (F12)"
echo "  3. Check Console tab for errors"
echo "  4. Check Network tab for failed requests"
echo "  5. Verify all pages load correctly"

# Test Summary
print_section "Test Summary"
echo "Total Tests:  $TOTAL_TESTS"
echo -n "Passed:       "
echo -e "${GREEN}$PASSED_TESTS${NC}"
echo -n "Failed:       "
echo -e "${RED}$FAILED_TESTS${NC}"
echo -n "Success Rate: "

if [ $TOTAL_TESTS -gt 0 ]; then
    success_rate=$(echo "scale=2; ($PASSED_TESTS / $TOTAL_TESTS) * 100" | bc)
    if [ "$(echo "$success_rate == 100" | bc)" -eq 1 ]; then
        echo -e "${GREEN}${success_rate}%${NC}"
    elif [ "$(echo "$success_rate >= 80" | bc)" -eq 1 ]; then
        echo -e "${YELLOW}${success_rate}%${NC}"
    else
        echo -e "${RED}${success_rate}%${NC}"
    fi
else
    echo "N/A"
fi

echo ""

# Exit code
if [ $FAILED_TESTS -eq 0 ]; then
    print_success "All tests passed! 🎉"
    exit 0
else
    print_failure "Some tests failed. Please check the output above."
    exit 1
fi
