@echo off
REM DevLog API Test Script (Windows)
REM This script tests all API endpoints

setlocal enabledelayedexpansion

set BASE_URL=http://localhost:8080/api
set TOTAL_TESTS=0
set PASSED_TESTS=0
set FAILED_TESTS=0

echo ======================================
echo   DevLog API Testing Started
echo ======================================
echo.

REM 1. Health Check
echo [1] Health Check
curl -s -o nul -w "%%{http_code}" %BASE_URL%/health > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo [PASS] Health Check
    set /a PASSED_TESTS+=1
) else (
    echo [FAIL] Health Check - Expected: 200, Got: !STATUS!
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
del temp_status.txt
echo.

REM 2. Get All Projects
echo [2] Project API Tests
curl -s -o nul -w "%%{http_code}" %BASE_URL%/projects > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo [PASS] Get all projects
    set /a PASSED_TESTS+=1
) else (
    echo [FAIL] Get all projects - Expected: 200, Got: !STATUS!
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
del temp_status.txt

REM 3. Create Project
echo Creating test project...
curl -s -X POST -H "Content-Type: application/json" ^
-d "{\"name\":\"Test Project\",\"description\":\"API Test\",\"status\":\"ACTIVE\",\"startDate\":\"2025-01-01T00:00:00\",\"progress\":50}" ^
-w "\n%%{http_code}" %BASE_URL%/projects > temp_response.txt

for /f "tokens=*" %%a in (temp_response.txt) do set LAST_LINE=%%a
if "!LAST_LINE!"=="201" (
    echo [PASS] Create project
    set /a PASSED_TESTS+=1
) else (
    echo [FAIL] Create project - Expected: 201, Got: !LAST_LINE!
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
del temp_response.txt
echo.

REM 4. Get All Logs
echo [3] DevLog API Tests
curl -s -o nul -w "%%{http_code}" %BASE_URL%/logs > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo [PASS] Get all logs
    set /a PASSED_TESTS+=1
) else (
    echo [FAIL] Get all logs - Expected: 200, Got: !STATUS!
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
del temp_status.txt
echo.

REM 5. Statistics
echo [4] Statistics API Tests
curl -s -o nul -w "%%{http_code}" %BASE_URL%/statistics/weekly > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo [PASS] Get weekly statistics
    set /a PASSED_TESTS+=1
) else (
    echo [FAIL] Get weekly statistics - Expected: 200, Got: !STATUS!
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
del temp_status.txt

curl -s -o nul -w "%%{http_code}" %BASE_URL%/statistics/monthly > temp_status.txt
set /p STATUS=<temp_status.txt
if "!STATUS!"=="200" (
    echo [PASS] Get monthly statistics
    set /a PASSED_TESTS+=1
) else (
    echo [FAIL] Get monthly statistics - Expected: 200, Got: !STATUS!
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
del temp_status.txt
echo.

REM Print Summary
echo ======================================
echo   Test Summary
echo ======================================
echo Total Tests:  !TOTAL_TESTS!
echo Passed:       !PASSED_TESTS!
echo Failed:       !FAILED_TESTS!
echo.

if !FAILED_TESTS!==0 (
    echo All tests passed!
    exit /b 0
) else (
    echo Some tests failed.
    exit /b 1
)
