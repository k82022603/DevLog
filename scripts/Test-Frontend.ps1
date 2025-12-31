# DevLog Frontend Test Script (PowerShell)
# This script tests the DevLog frontend application

param(
    [string]$BaseUrl = "http://localhost:3000",
    [switch]$Verbose
)

$ErrorActionPreference = "Continue"

# Color output functions
function Write-Success {
    param([string]$Message)
    Write-Host "✓ $Message" -ForegroundColor Green
}

function Write-Failure {
    param([string]$Message)
    Write-Host "✗ $Message" -ForegroundColor Red
}

function Write-Info {
    param([string]$Message)
    Write-Host "ℹ $Message" -ForegroundColor Cyan
}

function Write-Section {
    param([string]$Message)
    Write-Host "`n========================================" -ForegroundColor Yellow
    Write-Host "  $Message" -ForegroundColor Yellow
    Write-Host "========================================`n" -ForegroundColor Yellow
}

# Test counters
$script:totalTests = 0
$script:passedTests = 0
$script:failedTests = 0

function Test-Endpoint {
    param(
        [string]$Url,
        [string]$TestName,
        [string]$ExpectedContent = $null,
        [int]$ExpectedStatusCode = 200
    )

    $script:totalTests++

    try {
        $response = Invoke-WebRequest -Uri $Url -Method Get -TimeoutSec 10 -ErrorAction Stop

        if ($response.StatusCode -eq $ExpectedStatusCode) {
            if ($ExpectedContent) {
                if ($response.Content -like "*$ExpectedContent*") {
                    Write-Success "$TestName (Status: $($response.StatusCode), Content matched)"
                    $script:passedTests++
                } else {
                    Write-Failure "$TestName (Expected content '$ExpectedContent' not found)"
                    $script:failedTests++
                    if ($Verbose) {
                        Write-Info "Response snippet: $($response.Content.Substring(0, [Math]::Min(200, $response.Content.Length)))"
                    }
                }
            } else {
                Write-Success "$TestName (Status: $($response.StatusCode))"
                $script:passedTests++
            }
        } else {
            Write-Failure "$TestName (Expected status $ExpectedStatusCode, got $($response.StatusCode))"
            $script:failedTests++
        }
    }
    catch {
        Write-Failure "$TestName (Error: $($_.Exception.Message))"
        $script:failedTests++
    }
}

function Test-ResourceLoad {
    param(
        [string]$Url,
        [string]$ResourceType
    )

    $script:totalTests++

    try {
        $response = Invoke-WebRequest -Uri $Url -Method Head -TimeoutSec 10 -ErrorAction Stop

        if ($response.StatusCode -eq 200) {
            $size = $response.Headers.'Content-Length'
            if ($size) {
                Write-Success "$ResourceType loaded (Size: $([math]::Round($size/1024, 2)) KB)"
            } else {
                Write-Success "$ResourceType loaded"
            }
            $script:passedTests++
        } else {
            Write-Failure "$ResourceType (Status: $($response.StatusCode))"
            $script:failedTests++
        }
    }
    catch {
        Write-Failure "$ResourceType (Error: $($_.Exception.Message))"
        $script:failedTests++
    }
}

# Main test execution
Write-Section "DevLog Frontend Test Suite"
Write-Info "Base URL: $BaseUrl"
Write-Info "Timestamp: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"

# 1. Frontend Availability Test
Write-Section "1. Frontend Availability"
Test-Endpoint -Url $BaseUrl -TestName "Frontend server is running" -ExpectedContent "<!DOCTYPE html>"

# 2. Main Pages Test
Write-Section "2. Main Pages"
Test-Endpoint -Url "$BaseUrl/" -TestName "Home page (Dashboard)" -ExpectedContent "html"
Test-Endpoint -Url "$BaseUrl/logs" -TestName "Logs page" -ExpectedContent "html"
Test-Endpoint -Url "$BaseUrl/projects" -TestName "Projects page" -ExpectedContent "html"
Test-Endpoint -Url "$BaseUrl/settings" -TestName "Settings page" -ExpectedContent "html"

# 3. Static Resources Test
Write-Section "3. Static Resources"

# Try to get index.html first to find asset references
try {
    $indexHtml = Invoke-WebRequest -Uri $BaseUrl -TimeoutSec 10

    # Extract CSS files
    $cssFiles = $indexHtml.Content | Select-String -Pattern 'href="([^"]*\.css)"' -AllMatches |
                ForEach-Object { $_.Matches } |
                ForEach-Object { $_.Groups[1].Value }

    # Extract JS files
    $jsFiles = $indexHtml.Content | Select-String -Pattern 'src="([^"]*\.js)"' -AllMatches |
               ForEach-Object { $_.Matches } |
               ForEach-Object { $_.Groups[1].Value }

    if ($cssFiles) {
        foreach ($css in $cssFiles | Select-Object -First 3) {
            $cssUrl = if ($css -match "^http") { $css } else { "$BaseUrl/$($css.TrimStart('/'))" }
            Test-ResourceLoad -Url $cssUrl -ResourceType "CSS: $css"
        }
    }

    if ($jsFiles) {
        foreach ($js in $jsFiles | Select-Object -First 3) {
            $jsUrl = if ($js -match "^http") { $js } else { "$BaseUrl/$($js.TrimStart('/'))" }
            Test-ResourceLoad -Url $jsUrl -ResourceType "JS: $js"
        }
    }
}
catch {
    Write-Failure "Failed to analyze static resources: $($_.Exception.Message)"
}

# 4. API Integration Test (через frontend)
Write-Section "4. Backend API Integration"
Write-Info "Testing if frontend can reach backend API..."

try {
    # Check if backend is accessible
    $backendUrl = "http://localhost:8080/api/health"
    $backendResponse = Invoke-WebRequest -Uri $backendUrl -TimeoutSec 5 -ErrorAction Stop

    if ($backendResponse.StatusCode -eq 200) {
        Write-Success "Backend API is accessible ($backendUrl)"
        $script:totalTests++
        $script:passedTests++

        # Test other endpoints
        $endpoints = @(
            @{url="http://localhost:8080/api/projects"; name="Projects API"},
            @{url="http://localhost:8080/api/logs"; name="Logs API"}
        )

        foreach ($endpoint in $endpoints) {
            $script:totalTests++
            try {
                $resp = Invoke-WebRequest -Uri $endpoint.url -TimeoutSec 5 -ErrorAction Stop
                if ($resp.StatusCode -eq 200) {
                    Write-Success "$($endpoint.name) responding"
                    $script:passedTests++
                } else {
                    Write-Failure "$($endpoint.name) (Status: $($resp.StatusCode))"
                    $script:failedTests++
                }
            }
            catch {
                Write-Failure "$($endpoint.name) (Error: $($_.Exception.Message))"
                $script:failedTests++
            }
        }
    }
}
catch {
    Write-Failure "Backend API not accessible: $($_.Exception.Message)"
    $script:totalTests++
    $script:failedTests++
}

# 5. Browser Console Test (Manual check reminder)
Write-Section "5. Browser Console Check"
Write-Info "Please manually check the following in your browser:"
Write-Host "  1. Open http://localhost:3000 in Chrome/Firefox" -ForegroundColor White
Write-Host "  2. Open Developer Tools (F12)" -ForegroundColor White
Write-Host "  3. Check Console tab for errors" -ForegroundColor White
Write-Host "  4. Check Network tab for failed requests" -ForegroundColor White
Write-Host "  5. Verify all pages load correctly" -ForegroundColor White

# Test Summary
Write-Section "Test Summary"
Write-Host "Total Tests:  $script:totalTests" -ForegroundColor White
Write-Host "Passed:       " -NoNewline
Write-Host "$script:passedTests" -ForegroundColor Green
Write-Host "Failed:       " -NoNewline
Write-Host "$script:failedTests" -ForegroundColor Red
Write-Host "Success Rate: " -NoNewline
if ($script:totalTests -gt 0) {
    $successRate = [math]::Round(($script:passedTests / $script:totalTests) * 100, 2)
    if ($successRate -eq 100) {
        Write-Host "$successRate%" -ForegroundColor Green
    } elseif ($successRate -ge 80) {
        Write-Host "$successRate%" -ForegroundColor Yellow
    } else {
        Write-Host "$successRate%" -ForegroundColor Red
    }
} else {
    Write-Host "N/A" -ForegroundColor Gray
}

Write-Host "`n"

# Exit code
if ($script:failedTests -eq 0) {
    Write-Success "All tests passed! 🎉"
    exit 0
} else {
    Write-Failure "Some tests failed. Please check the output above."
    exit 1
}
