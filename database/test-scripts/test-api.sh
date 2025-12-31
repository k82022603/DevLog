#!/bin/bash

# ============================================================
# DevLog API Test Script
# ============================================================
# This script tests all major API endpoints
# Usage: ./test-api.sh
# ============================================================

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# API Base URL
API_URL="http://localhost:8080"

# Test counters
TESTS_PASSED=0
TESTS_FAILED=0

# Function to print test result
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ PASS${NC}: $2"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}✗ FAIL${NC}: $2"
        ((TESTS_FAILED++))
    fi
}

# Function to test API endpoint
test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3
    local data=$4
    local expected_status=$5

    echo -e "\n${YELLOW}Testing:${NC} $description"
    echo "  Method: $method"
    echo "  Endpoint: $endpoint"

    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$API_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$API_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi

    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    if [ "$http_code" = "$expected_status" ]; then
        print_result 0 "$description"
        echo "  Response: $body" | head -c 200
        echo "..."
    else
        print_result 1 "$description (Expected: $expected_status, Got: $http_code)"
        echo "  Response: $body"
    fi
}

# ============================================================
# Start Testing
# ============================================================

echo "======================================"
echo "  DevLog API Test Suite"
echo "======================================"
echo ""

# ============================================================
# Health Check
# ============================================================

echo -e "\n${YELLOW}[1] Health Check API${NC}"
test_endpoint "GET" "/health" "Health check endpoint" "" "200"

# ============================================================
# Project API Tests
# ============================================================

echo -e "\n${YELLOW}[2] Project API${NC}"

# GET /api/projects
test_endpoint "GET" "/api/projects" "Get all projects" "" "200"

# POST /api/projects
PROJECT_DATA='{
  "name": "테스트 프로젝트",
  "description": "API 테스트용 프로젝트",
  "status": "ACTIVE",
  "progress": 0,
  "techStack": "Spring Boot,React",
  "startDate": "2025-12-31T00:00:00"
}'
test_endpoint "POST" "/api/projects" "Create new project" "$PROJECT_DATA" "201"

# GET /api/projects/1
test_endpoint "GET" "/api/projects/1" "Get project by ID" "" "200"

# PUT /api/projects/1
UPDATE_PROJECT_DATA='{
  "name": "수정된 프로젝트",
  "description": "프로젝트 설명 수정",
  "status": "ACTIVE",
  "progress": 50,
  "techStack": "Spring Boot,React,PostgreSQL",
  "startDate": "2025-12-31T00:00:00"
}'
test_endpoint "PUT" "/api/projects/1" "Update project" "$UPDATE_PROJECT_DATA" "200"

# ============================================================
# DevLog API Tests
# ============================================================

echo -e "\n${YELLOW}[3] DevLog API${NC}"

# GET /api/logs
test_endpoint "GET" "/api/logs" "Get all dev logs" "" "200"

# POST /api/logs
LOG_DATA='{
  "projectId": 1,
  "title": "테스트 로그",
  "description": "API 테스트용 로그",
  "content": "이것은 API 테스트를 위한 개발 로그입니다.",
  "tags": "Test,API",
  "mood": "GOOD",
  "startTime": "09:00:00",
  "endTime": "17:00:00",
  "logDate": "2025-12-31T00:00:00"
}'
test_endpoint "POST" "/api/logs" "Create new dev log" "$LOG_DATA" "201"

# GET /api/logs/1
test_endpoint "GET" "/api/logs/1" "Get log by ID" "" "200"

# PUT /api/logs/1
UPDATE_LOG_DATA='{
  "projectId": 1,
  "title": "수정된 로그",
  "description": "수정된 설명",
  "content": "수정된 내용입니다.",
  "tags": "Test,Updated",
  "mood": "GREAT",
  "startTime": "09:00:00",
  "endTime": "18:00:00",
  "logDate": "2025-12-31T00:00:00"
}'
test_endpoint "PUT" "/api/logs/1" "Update dev log" "$UPDATE_LOG_DATA" "200"

# ============================================================
# Statistics API Tests
# ============================================================

echo -e "\n${YELLOW}[4] Statistics API${NC}"

# GET /api/statistics/weekly/current
test_endpoint "GET" "/api/statistics/weekly/current" "Get current week statistics" "" "200"

# GET /api/statistics/monthly/current
test_endpoint "GET" "/api/statistics/monthly/current" "Get current month statistics" "" "200"

# GET /api/statistics/project/1
test_endpoint "GET" "/api/statistics/project/1" "Get project statistics" "" "200"

# GET /api/statistics/tech-stack
test_endpoint "GET" "/api/statistics/tech-stack" "Get tech stack statistics" "" "200"

# ============================================================
# Filter and Search Tests
# ============================================================

echo -e "\n${YELLOW}[5] Filter and Search${NC}"

# GET /api/logs?projectId=1
test_endpoint "GET" "/api/logs?projectId=1" "Filter logs by project" "" "200"

# GET /api/logs?startDate=2025-12-01&endDate=2025-12-31
test_endpoint "GET" "/api/logs?startDate=2025-12-01&endDate=2025-12-31" "Filter logs by date range" "" "200"

# GET /api/logs?keyword=테스트
test_endpoint "GET" "/api/logs?keyword=테스트" "Search logs by keyword" "" "200"

# ============================================================
# Error Cases
# ============================================================

echo -e "\n${YELLOW}[6] Error Handling${NC}"

# GET /api/projects/999 (non-existent)
test_endpoint "GET" "/api/projects/999" "Get non-existent project" "" "404"

# POST /api/logs with invalid data
INVALID_LOG_DATA='{
  "projectId": 999,
  "title": "",
  "content": "내용"
}'
test_endpoint "POST" "/api/logs" "Create log with invalid data" "$INVALID_LOG_DATA" "400"

# ============================================================
# Test Summary
# ============================================================

echo ""
echo "======================================"
echo "  Test Summary"
echo "======================================"
echo -e "${GREEN}Passed:${NC} $TESTS_PASSED"
echo -e "${RED}Failed:${NC} $TESTS_FAILED"
echo "======================================"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}All tests passed!${NC}"
    exit 0
else
    echo -e "${RED}Some tests failed!${NC}"
    exit 1
fi
