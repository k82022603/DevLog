@echo off
REM DevLog Frontend Test Script (Windows Batch)
REM This script tests the DevLog frontend application

setlocal enabledelayedexpansion

set BASE_URL=http://localhost:3000
set BACKEND_URL=http://localhost:8080/api
set TOTAL_TESTS=0
set PASSED_TESTS=0
set FAILED_TESTS=0

echo.
echo ========================================
echo   DevLog Frontend Test Suite
echo ========================================
echo.
echo Base URL: %BASE_URL%
echo Backend URL: %BACKEND_URL%
echo Timestamp: %date% %time%
echo.

REM 1. Frontend Availability Test
echo ========================================
echo   1. Frontend Availability
echo ========================================
echo.

set /a TOTAL_TESTS+=1
curl -s -o nul -w "%%{http_code}" %BASE_URL% > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if "%STATUS%"=="200" (
    echo [32m✓ Frontend server is running (Status: %STATUS%)[0m
    set /a PASSED_TESTS+=1
) else (
    echo [31m✗ Frontend server is running (Status: %STATUS%)[0m
    set /a FAILED_TESTS+=1
)

REM 2. Main Pages Test
echo.
echo ========================================
echo   2. Main Pages
echo ========================================
echo.

REM Test Dashboard
set /a TOTAL_TESTS+=1
curl -s -o nul -w "%%{http_code}" %BASE_URL%/ > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if "%STATUS%"=="200" (
    echo [32m✓ Home page - Dashboard (Status: %STATUS%)[0m
    set /a PASSED_TESTS+=1
) else (
    echo [31m✗ Home page - Dashboard (Status: %STATUS%)[0m
    set /a FAILED_TESTS+=1
)

REM Test Logs page
set /a TOTAL_TESTS+=1
curl -s -o nul -w "%%{http_code}" %BASE_URL%/logs > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if "%STATUS%"=="200" (
    echo [32m✓ Logs page (Status: %STATUS%)[0m
    set /a PASSED_TESTS+=1
) else (
    echo [31m✗ Logs page (Status: %STATUS%)[0m
    set /a FAILED_TESTS+=1
)

REM Test Projects page
set /a TOTAL_TESTS+=1
curl -s -o nul -w "%%{http_code}" %BASE_URL%/projects > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if "%STATUS%"=="200" (
    echo [32m✓ Projects page (Status: %STATUS%)[0m
    set /a PASSED_TESTS+=1
) else (
    echo [31m✗ Projects page (Status: %STATUS%)[0m
    set /a FAILED_TESTS+=1
)

REM Test Settings page
set /a TOTAL_TESTS+=1
curl -s -o nul -w "%%{http_code}" %BASE_URL%/settings > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if "%STATUS%"=="200" (
    echo [32m✓ Settings page (Status: %STATUS%)[0m
    set /a PASSED_TESTS+=1
) else (
    echo [31m✗ Settings page (Status: %STATUS%)[0m
    set /a FAILED_TESTS+=1
)

REM 3. Backend API Integration Test
echo.
echo ========================================
echo   3. Backend API Integration
echo ========================================
echo.

REM Test Backend Health
set /a TOTAL_TESTS+=1
curl -s -o nul -w "%%{http_code}" %BACKEND_URL%/health > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if "%STATUS%"=="200" (
    echo [32m✓ Backend API is accessible (Status: %STATUS%)[0m
    set /a PASSED_TESTS+=1
) else (
    echo [31m✗ Backend API not accessible (Status: %STATUS%)[0m
    set /a FAILED_TESTS+=1
)

REM Test Projects API
set /a TOTAL_TESTS+=1
curl -s -o nul -w "%%{http_code}" %BACKEND_URL%/projects > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if "%STATUS%"=="200" (
    echo [32m✓ Projects API responding (Status: %STATUS%)[0m
    set /a PASSED_TESTS+=1
) else (
    echo [31m✗ Projects API (Status: %STATUS%)[0m
    set /a FAILED_TESTS+=1
)

REM Test Logs API
set /a TOTAL_TESTS+=1
curl -s -o nul -w "%%{http_code}" %BACKEND_URL%/logs > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if "%STATUS%"=="200" (
    echo [32m✓ Logs API responding (Status: %STATUS%)[0m
    set /a PASSED_TESTS+=1
) else (
    echo [31m✗ Logs API (Status: %STATUS%)[0m
    set /a FAILED_TESTS+=1
)

REM 4. Browser Console Check (Manual)
echo.
echo ========================================
echo   4. Browser Console Check (Manual)
echo ========================================
echo.
echo Please manually check the following in your browser:
echo   1. Open http://localhost:3000 in Chrome/Firefox
echo   2. Open Developer Tools (F12)
echo   3. Check Console tab for errors
echo   4. Check Network tab for failed requests
echo   5. Verify all pages load correctly

REM Test Summary
echo.
echo ========================================
echo   Test Summary
echo ========================================
echo.
echo Total Tests:  %TOTAL_TESTS%
echo Passed:       [32m%PASSED_TESTS%[0m
echo Failed:       [31m%FAILED_TESTS%[0m

set /a SUCCESS_RATE=(%PASSED_TESTS% * 100) / %TOTAL_TESTS%
echo Success Rate: %SUCCESS_RATE%%%

echo.

if %FAILED_TESTS% EQU 0 (
    echo [32m✓ All tests passed! 🎉[0m
    exit /b 0
) else (
    echo [31m✗ Some tests failed. Please check the output above.[0m
    exit /b 1
)
