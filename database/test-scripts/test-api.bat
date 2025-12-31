@echo off
REM ============================================================
REM DevLog API Test Script for Windows
REM ============================================================
REM This script tests all major API endpoints
REM Usage: test-api.bat
REM ============================================================

setlocal enabledelayedexpansion

REM API Base URL
set API_URL=http://localhost:8080

REM Test counters
set TESTS_PASSED=0
set TESTS_FAILED=0

echo ======================================
echo   DevLog API Test Suite
echo ======================================
echo.

REM ============================================================
REM Health Check
REM ============================================================

echo [1] Health Check API
echo Testing: Health check endpoint
curl -s -w "%%{http_code}" -X GET "%API_URL%/health"
echo.
echo.

REM ============================================================
REM Project API Tests
REM ============================================================

echo [2] Project API
echo.

echo Testing: Get all projects
curl -s -X GET "%API_URL%/api/projects"
echo.
echo.

echo Testing: Create new project
curl -s -X POST "%API_URL%/api/projects" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"테스트 프로젝트\",\"description\":\"API 테스트용 프로젝트\",\"status\":\"ACTIVE\",\"progress\":0,\"techStack\":\"Spring Boot,React\",\"startDate\":\"2025-12-31T00:00:00\"}"
echo.
echo.

echo Testing: Get project by ID
curl -s -X GET "%API_URL%/api/projects/1"
echo.
echo.

echo Testing: Update project
curl -s -X PUT "%API_URL%/api/projects/1" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"수정된 프로젝트\",\"description\":\"프로젝트 설명 수정\",\"status\":\"ACTIVE\",\"progress\":50}"
echo.
echo.

REM ============================================================
REM DevLog API Tests
REM ============================================================

echo [3] DevLog API
echo.

echo Testing: Get all dev logs
curl -s -X GET "%API_URL%/api/logs"
echo.
echo.

echo Testing: Create new dev log
curl -s -X POST "%API_URL%/api/logs" ^
  -H "Content-Type: application/json" ^
  -d "{\"projectId\":1,\"title\":\"테스트 로그\",\"description\":\"API 테스트용 로그\",\"content\":\"이것은 API 테스트를 위한 개발 로그입니다.\",\"tags\":\"Test,API\",\"mood\":\"GOOD\",\"startTime\":\"09:00:00\",\"endTime\":\"17:00:00\",\"logDate\":\"2025-12-31T00:00:00\"}"
echo.
echo.

echo Testing: Get log by ID
curl -s -X GET "%API_URL%/api/logs/1"
echo.
echo.

echo Testing: Update dev log
curl -s -X PUT "%API_URL%/api/logs/1" ^
  -H "Content-Type: application/json" ^
  -d "{\"projectId\":1,\"title\":\"수정된 로그\",\"content\":\"수정된 내용입니다.\",\"mood\":\"GREAT\",\"logDate\":\"2025-12-31T00:00:00\"}"
echo.
echo.

REM ============================================================
REM Statistics API Tests
REM ============================================================

echo [4] Statistics API
echo.

echo Testing: Get current week statistics
curl -s -X GET "%API_URL%/api/statistics/weekly/current"
echo.
echo.

echo Testing: Get current month statistics
curl -s -X GET "%API_URL%/api/statistics/monthly/current"
echo.
echo.

echo Testing: Get project statistics
curl -s -X GET "%API_URL%/api/statistics/project/1"
echo.
echo.

echo Testing: Get tech stack statistics
curl -s -X GET "%API_URL%/api/statistics/tech-stack"
echo.
echo.

REM ============================================================
REM Filter and Search Tests
REM ============================================================

echo [5] Filter and Search
echo.

echo Testing: Filter logs by project
curl -s -X GET "%API_URL%/api/logs?projectId=1"
echo.
echo.

echo Testing: Filter logs by date range
curl -s -X GET "%API_URL%/api/logs?startDate=2025-12-01&endDate=2025-12-31"
echo.
echo.

echo Testing: Search logs by keyword
curl -s -X GET "%API_URL%/api/logs?keyword=테스트"
echo.
echo.

REM ============================================================
REM Error Cases
REM ============================================================

echo [6] Error Handling
echo.

echo Testing: Get non-existent project (should return 404)
curl -s -w "%%{http_code}" -X GET "%API_URL%/api/projects/999"
echo.
echo.

echo Testing: Create log with invalid data (should return 400)
curl -s -w "%%{http_code}" -X POST "%API_URL%/api/logs" ^
  -H "Content-Type: application/json" ^
  -d "{\"projectId\":999,\"title\":\"\",\"content\":\"내용\"}"
echo.
echo.

REM ============================================================
REM Test Complete
REM ============================================================

echo ======================================
echo   All API tests completed!
echo ======================================

pause
