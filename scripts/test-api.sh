#!/bin/bash

# DevLog API Test Script
# 이 스크립트는 모든 API 엔드포인트를 테스트합니다.

BASE_URL="http://localhost:8080/api"
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Helper function to print test result
print_result() {
    local test_name="$1"
    local status_code="$2"
    local expected_code="$3"

    TOTAL_TESTS=$((TOTAL_TESTS + 1))

    if [ "$status_code" -eq "$expected_code" ]; then
        echo -e "${GREEN}✓${NC} $test_name (Status: $status_code)"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}✗${NC} $test_name (Expected: $expected_code, Got: $status_code)"
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
}

# Helper function to make API call and check status
test_endpoint() {
    local method="$1"
    local endpoint="$2"
    local test_name="$3"
    local expected_code="${4:-200}"
    local data="$5"

    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" \
            -H "Content-Type: application/json" \
            -d "$data" \
            "$BASE_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$BASE_URL$endpoint")
    fi

    status_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    print_result "$test_name" "$status_code" "$expected_code"

    # Return body for further use
    echo "$body"
}

echo "======================================"
echo "  DevLog API Testing Started"
echo "======================================"
echo ""

# 1. Health Check
echo -e "${YELLOW}[1] Health Check${NC}"
test_endpoint "GET" "/health" "Health Check" 200 > /dev/null
echo ""

# 2. Project API Tests
echo -e "${YELLOW}[2] Project API Tests${NC}"
test_endpoint "GET" "/projects" "Get all projects" 200 > /dev/null

# Create a new project
PROJECT_DATA='{
  "name": "Test Project",
  "description": "API 테스트용 프로젝트",
  "status": "ACTIVE",
  "startDate": "2025-01-01T00:00:00",
  "progress": 50
}'
project_response=$(test_endpoint "POST" "/projects" "Create project" 201 "$PROJECT_DATA")
PROJECT_ID=$(echo "$project_response" | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)

if [ -n "$PROJECT_ID" ]; then
    echo -e "  ${GREEN}Created project with ID: $PROJECT_ID${NC}"

    # Get project by ID
    test_endpoint "GET" "/projects/$PROJECT_ID" "Get project by ID" 200 > /dev/null

    # Update project
    UPDATE_DATA='{
      "name": "Updated Test Project",
      "description": "수정된 테스트 프로젝트",
      "status": "ACTIVE",
      "startDate": "2025-01-01T00:00:00",
      "progress": 75
    }'
    test_endpoint "PUT" "/projects/$PROJECT_ID" "Update project" 200 "$UPDATE_DATA" > /dev/null
else
    echo -e "  ${RED}Failed to create project${NC}"
fi
echo ""

# 3. DevLog API Tests
echo -e "${YELLOW}[3] DevLog API Tests${NC}"
test_endpoint "GET" "/logs" "Get all logs" 200 > /dev/null

if [ -n "$PROJECT_ID" ]; then
    # Create a new log
    LOG_DATA='{
      "projectId": '"$PROJECT_ID"',
      "title": "Test Log Entry",
      "description": "API 테스트용 로그",
      "logDate": "2025-01-20T10:00:00",
      "startTime": "09:00",
      "endTime": "12:00",
      "achievements": "API 테스트 완료",
      "challenges": "없음",
      "learnings": "API 테스트 방법 학습",
      "codeSnippets": "console.log(\"test\");",
      "tags": "Test,API",
      "mood": "GOOD"
    }'
    log_response=$(test_endpoint "POST" "/logs" "Create log" 201 "$LOG_DATA")
    LOG_ID=$(echo "$log_response" | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)

    if [ -n "$LOG_ID" ]; then
        echo -e "  ${GREEN}Created log with ID: $LOG_ID${NC}"

        # Get log by ID
        test_endpoint "GET" "/logs/$LOG_ID" "Get log by ID" 200 > /dev/null

        # Update log
        UPDATE_LOG_DATA='{
          "projectId": '"$PROJECT_ID"',
          "title": "Updated Test Log",
          "description": "수정된 API 테스트 로그",
          "logDate": "2025-01-20T10:00:00",
          "startTime": "09:00",
          "endTime": "13:00",
          "tags": "Test,API,Updated"
        }'
        test_endpoint "PUT" "/logs/$LOG_ID" "Update log" 200 "$UPDATE_LOG_DATA" > /dev/null

        # Get logs by project
        test_endpoint "GET" "/logs?projectId=$PROJECT_ID" "Get logs by project" 200 > /dev/null
    fi
fi
echo ""

# 4. Statistics API Tests
echo -e "${YELLOW}[4] Statistics API Tests${NC}"
test_endpoint "GET" "/statistics/weekly" "Get weekly statistics" 200 > /dev/null
test_endpoint "GET" "/statistics/monthly" "Get monthly statistics" 200 > /dev/null

if [ -n "$PROJECT_ID" ]; then
    test_endpoint "GET" "/statistics/projects/$PROJECT_ID" "Get project statistics" 200 > /dev/null
fi

test_endpoint "GET" "/statistics/tech-stacks" "Get tech stack statistics" 200 > /dev/null
echo ""

# 5. Cleanup (Delete created resources)
echo -e "${YELLOW}[5] Cleanup${NC}"
if [ -n "$LOG_ID" ]; then
    test_endpoint "DELETE" "/logs/$LOG_ID" "Delete log" 204 > /dev/null
fi

if [ -n "$PROJECT_ID" ]; then
    test_endpoint "DELETE" "/projects/$PROJECT_ID" "Delete project" 204 > /dev/null
fi
echo ""

# Print summary
echo "======================================"
echo "  Test Summary"
echo "======================================"
echo -e "Total Tests:  $TOTAL_TESTS"
echo -e "${GREEN}Passed:       $PASSED_TESTS${NC}"
echo -e "${RED}Failed:       $FAILED_TESTS${NC}"
echo ""

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}All tests passed! ✓${NC}"
    exit 0
else
    echo -e "${RED}Some tests failed. Please check the output above.${NC}"
    exit 1
fi
