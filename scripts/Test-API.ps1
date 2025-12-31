# DevLog API Test Script (PowerShell)
# 모든 API 엔드포인트를 테스트하고 결과를 출력합니다.

$baseUrl = "http://localhost:8080/api"
$totalTests = 0
$passedTests = 0
$failedTests = 0
$projectId = $null
$logId = $null

# Helper function to test endpoint
function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Endpoint,
        [string]$TestName,
        [int]$ExpectedStatus = 200,
        [object]$Body = $null
    )

    $script:totalTests++

    try {
        $uri = "$baseUrl$Endpoint"
        $params = @{
            Uri = $uri
            Method = $Method
            ContentType = "application/json"
            ErrorAction = "Stop"
        }

        if ($Body) {
            $params.Body = ($Body | ConvertTo-Json -Depth 10)
        }

        $response = Invoke-RestMethod @params -ResponseHeadersVariable headers -StatusCodeVariable statusCode

        if ($statusCode -eq $ExpectedStatus) {
            Write-Host "✓ $TestName (Status: $statusCode)" -ForegroundColor Green
            $script:passedTests++
            return $response
        } else {
            Write-Host "✗ $TestName (Expected: $ExpectedStatus, Got: $statusCode)" -ForegroundColor Red
            $script:failedTests++
            return $null
        }
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        if ($statusCode -eq $ExpectedStatus) {
            Write-Host "✓ $TestName (Status: $statusCode)" -ForegroundColor Green
            $script:passedTests++
        } else {
            Write-Host "✗ $TestName (Expected: $ExpectedStatus, Got: $statusCode)" -ForegroundColor Red
            Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Yellow
            $script:failedTests++
        }
        return $null
    }
}

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  DevLog API Testing Started" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

# 1. Health Check
Write-Host "[1] Health Check" -ForegroundColor Yellow
Test-Endpoint -Method "GET" -Endpoint "/health" -TestName "Health Check" | Out-Null
Write-Host ""

# 2. Project API Tests
Write-Host "[2] Project API Tests" -ForegroundColor Yellow
Test-Endpoint -Method "GET" -Endpoint "/projects" -TestName "Get all projects" | Out-Null

# Create project
$projectData = @{
    name = "Test Project"
    description = "API 테스트용 프로젝트"
    status = "ACTIVE"
    startDate = "2025-01-01T00:00:00"
    progress = 50
}

$project = Test-Endpoint -Method "POST" -Endpoint "/projects" -TestName "Create project" -ExpectedStatus 201 -Body $projectData
if ($project -and $project.id) {
    $script:projectId = $project.id
    Write-Host "  Created project with ID: $projectId" -ForegroundColor Green

    # Get project by ID
    Test-Endpoint -Method "GET" -Endpoint "/projects/$projectId" -TestName "Get project by ID" | Out-Null

    # Update project
    $updateData = @{
        name = "Updated Test Project"
        description = "수정된 테스트 프로젝트"
        status = "ACTIVE"
        startDate = "2025-01-01T00:00:00"
        progress = 75
    }
    Test-Endpoint -Method "PUT" -Endpoint "/projects/$projectId" -TestName "Update project" -Body $updateData | Out-Null
}
Write-Host ""

# 3. DevLog API Tests
Write-Host "[3] DevLog API Tests" -ForegroundColor Yellow
Test-Endpoint -Method "GET" -Endpoint "/logs" -TestName "Get all logs" | Out-Null

if ($projectId) {
    # Create log
    $logData = @{
        projectId = $projectId
        title = "Test Log Entry"
        description = "API 테스트용 로그"
        logDate = "2025-01-20T10:00:00"
        startTime = "09:00"
        endTime = "12:00"
        achievements = "API 테스트 완료"
        challenges = "없음"
        learnings = "API 테스트 방법 학습"
        codeSnippets = "console.log('test');"
        tags = "Test,API"
        mood = "GOOD"
    }

    $log = Test-Endpoint -Method "POST" -Endpoint "/logs" -TestName "Create log" -ExpectedStatus 201 -Body $logData
    if ($log -and $log.id) {
        $script:logId = $log.id
        Write-Host "  Created log with ID: $logId" -ForegroundColor Green

        # Get log by ID
        Test-Endpoint -Method "GET" -Endpoint "/logs/$logId" -TestName "Get log by ID" | Out-Null

        # Update log
        $updateLogData = @{
            projectId = $projectId
            title = "Updated Test Log"
            description = "수정된 API 테스트 로그"
            logDate = "2025-01-20T10:00:00"
            startTime = "09:00"
            endTime = "13:00"
            tags = "Test,API,Updated"
        }
        Test-Endpoint -Method "PUT" -Endpoint "/logs/$logId" -TestName "Update log" -Body $updateLogData | Out-Null

        # Get logs by project
        Test-Endpoint -Method "GET" -Endpoint "/logs?projectId=$projectId" -TestName "Get logs by project" | Out-Null
    }
}
Write-Host ""

# 4. Statistics API Tests
Write-Host "[4] Statistics API Tests" -ForegroundColor Yellow
Test-Endpoint -Method "GET" -Endpoint "/statistics/weekly" -TestName "Get weekly statistics" | Out-Null
Test-Endpoint -Method "GET" -Endpoint "/statistics/monthly" -TestName "Get monthly statistics" | Out-Null

if ($projectId) {
    Test-Endpoint -Method "GET" -Endpoint "/statistics/projects/$projectId" -TestName "Get project statistics" | Out-Null
}

Test-Endpoint -Method "GET" -Endpoint "/statistics/tech-stacks" -TestName "Get tech stack statistics" | Out-Null
Write-Host ""

# 5. Cleanup
Write-Host "[5] Cleanup" -ForegroundColor Yellow
if ($logId) {
    Test-Endpoint -Method "DELETE" -Endpoint "/logs/$logId" -TestName "Delete log" -ExpectedStatus 204 | Out-Null
}

if ($projectId) {
    Test-Endpoint -Method "DELETE" -Endpoint "/projects/$projectId" -TestName "Delete project" -ExpectedStatus 204 | Out-Null
}
Write-Host ""

# Print Summary
Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  Test Summary" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host "Total Tests:  $totalTests"
Write-Host "Passed:       $passedTests" -ForegroundColor Green
Write-Host "Failed:       $failedTests" -ForegroundColor Red
Write-Host ""

if ($failedTests -eq 0) {
    Write-Host "All tests passed! ✓" -ForegroundColor Green
    exit 0
} else {
    Write-Host "Some tests failed. Please check the output above." -ForegroundColor Red
    exit 1
}
