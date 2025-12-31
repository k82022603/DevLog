# DevLog Frontend Testing Guide

이 문서는 DevLog 프론트엔드 애플리케이션을 테스트하는 방법을 설명합니다.

## 📋 목차

- [테스트 스크립트 개요](#테스트-스크립트-개요)
- [사전 요구사항](#사전-요구사항)
- [사용 방법](#사용-방법)
- [테스트 항목](#테스트-항목)
- [문제 해결](#문제-해결)
- [수동 테스트](#수동-테스트)

---

## 테스트 스크립트 개요

DevLog 프론트엔드를 테스트하기 위한 3가지 스크립트를 제공합니다:

| 스크립트 | 플랫폼 | 설명 |
|---------|--------|------|
| `Test-Frontend.ps1` | Windows (PowerShell) | 가장 상세한 출력, 색상 코딩 |
| `test-frontend.sh` | Linux/Mac/Git Bash | Unix 환경용, 자동화에 적합 |
| `test-frontend.bat` | Windows (CMD) | 기본 Windows 환경 |

---

## 사전 요구사항

### 필수 요구사항

1. **Docker 실행 중**
   ```bash
   # 모든 컨테이너 확인
   docker ps

   # devlog-frontend, devlog-backend, devlog-postgres 가 Running 상태여야 함
   ```

2. **프론트엔드 실행 중**
   - URL: http://localhost:3000
   - 포트: 3000

3. **백엔드 API 실행 중**
   - URL: http://localhost:8080/api
   - 포트: 8080

### 도구 요구사항

**PowerShell 스크립트** (`Test-Frontend.ps1`):
- Windows PowerShell 5.1 이상 또는 PowerShell Core 7+
- `curl` 또는 `Invoke-WebRequest` 사용 가능

**Bash 스크립트** (`test-frontend.sh`):
- Bash 4.0 이상
- `curl` 설치됨
- `bc` 계산기 (성공률 계산용)

**Batch 스크립트** (`test-frontend.bat`):
- Windows CMD
- `curl` 설치됨 (Windows 10 1803 이상은 기본 포함)

---

## 사용 방법

### Windows PowerShell

```powershell
# 기본 실행
.\scripts\Test-Frontend.ps1

# 상세 출력 모드
.\scripts\Test-Frontend.ps1 -Verbose

# 다른 URL 지정
.\scripts\Test-Frontend.ps1 -BaseUrl "http://192.168.1.100:3000"
```

### Linux/Mac/Git Bash

```bash
# 기본 실행
./scripts/test-frontend.sh

# 다른 URL 지정
./scripts/test-frontend.sh http://192.168.1.100:3000

# 스크립트에 실행 권한 부여 (처음 한 번만)
chmod +x ./scripts/test-frontend.sh
```

### Windows CMD

```cmd
REM 기본 실행
scripts\test-frontend.bat
```

---

## 테스트 항목

### 1. 프론트엔드 가용성 테스트
- ✅ 프론트엔드 서버가 실행 중인지 확인
- ✅ HTTP 200 응답 확인
- ✅ HTML 콘텐츠 반환 확인

### 2. 주요 페이지 테스트
| 페이지 | URL | 확인 사항 |
|--------|-----|----------|
| 대시보드 | `/` | 홈 페이지 로드 |
| 로그 목록 | `/logs` | 로그 페이지 접근 |
| 프로젝트 목록 | `/projects` | 프로젝트 페이지 접근 |
| 설정 | `/settings` | 설정 페이지 접근 |

### 3. 정적 리소스 테스트
- ✅ CSS 파일 로드 확인
- ✅ JavaScript 파일 로드 확인
- ✅ 파일 크기 표시

### 4. 백엔드 API 통합 테스트
- ✅ 백엔드 헬스 체크 (`/api/health`)
- ✅ Projects API (`/api/projects`)
- ✅ Logs API (`/api/logs`)

### 5. 브라우저 콘솔 체크 (수동)
- 수동으로 확인할 항목 안내

---

## 출력 예시

### 성공적인 테스트

```
========================================
  DevLog Frontend Test Suite
========================================

ℹ Base URL: http://localhost:3000
ℹ Timestamp: 2025-12-30 19:45:00

========================================
  1. Frontend Availability
========================================

✓ Frontend server is running (Status: 200, Content matched)

========================================
  2. Main Pages
========================================

✓ Home page (Dashboard) (Status: 200)
✓ Logs page (Status: 200)
✓ Projects page (Status: 200)
✓ Settings page (Status: 200)

========================================
  3. Static Resources
========================================

✓ CSS: /static/css/main.abc123.css loaded (Size: 45.23 KB)
✓ JS: /static/js/main.xyz789.js loaded (Size: 234.56 KB)

========================================
  4. Backend API Integration
========================================

✓ Backend API is accessible (http://localhost:8080/api/health)
✓ Projects API responding
✓ Logs API responding

========================================
  Test Summary
========================================

Total Tests:  10
Passed:       10
Failed:       0
Success Rate: 100%

✓ All tests passed! 🎉
```

### 실패한 테스트

```
✗ Frontend server is running (Expected status 200, got 000)
✗ Logs API (Error: Connection refused)

========================================
  Test Summary
========================================

Total Tests:  10
Passed:       7
Failed:       3
Success Rate: 70%

✗ Some tests failed. Please check the output above.
```

---

## 문제 해결

### 문제: "Frontend server is running" 실패

**원인**: 프론트엔드 컨테이너가 실행 중이 아님

**해결**:
```bash
# 컨테이너 상태 확인
docker ps -a | grep devlog-frontend

# 프론트엔드 시작
cd frontend
docker-compose up -d frontend

# 로그 확인
docker logs devlog-frontend
```

### 문제: "Backend API not accessible" 실패

**원인**: 백엔드 API가 응답하지 않음

**해결**:
```bash
# 백엔드 컨테이너 확인
docker ps -a | grep devlog-backend

# 백엔드 시작
cd backend
docker-compose up -d backend

# 백엔드 로그 확인
docker logs devlog-backend

# API 직접 테스트
curl http://localhost:8080/api/health
```

### 문제: 정적 리소스 로드 실패

**원인**:
- React 빌드가 안 됨
- Nginx 설정 문제

**해결**:
```bash
# 프론트엔드 재빌드
cd frontend
docker-compose build --no-cache frontend
docker-compose up -d frontend
```

### 문제: 페이지가 404 반환

**원인**: React Router 설정 또는 Nginx 설정 문제

**해결**:
```bash
# Nginx 설정 확인
docker exec devlog-frontend cat /etc/nginx/conf.d/default.conf

# 프론트엔드 재시작
docker-compose restart frontend
```

### 문제: CORS 에러

**원인**: 백엔드 CORS 설정 누락

**해결**:
```java
// backend/src/main/java/com/vibecoding/devlog/config/WebConfig.java 확인
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("*")
            .allowedHeaders("*");
}
```

---

## 수동 테스트

스크립트 테스트 후, 브라우저에서 수동으로 확인해야 할 사항:

### 1. 브라우저 개발자 도구 열기

1. **Chrome/Edge**: `F12` 또는 `Ctrl+Shift+I`
2. **Firefox**: `F12` 또는 `Ctrl+Shift+I`
3. **Safari**: `Cmd+Option+I` (개발자 메뉴 활성화 필요)

### 2. Console 탭 확인

#### ✅ 정상 상태
```
React App loaded
API connection successful
```

#### ❌ 에러 예시
```
Failed to load resource: net::ERR_CONNECTION_REFUSED
Uncaught TypeError: Cannot read property 'map' of undefined
CORS policy: No 'Access-Control-Allow-Origin' header
```

### 3. Network 탭 확인

#### 확인할 항목
- [ ] 모든 CSS/JS 파일이 200 상태로 로드됨
- [ ] API 요청이 성공적으로 완료됨 (200 상태)
- [ ] 리소스 로딩 시간이 적절함 (< 3초)

#### 필터 사용
- **JS**: JavaScript 파일만 표시
- **CSS**: CSS 파일만 표시
- **XHR**: API 요청만 표시

### 4. 페이지별 기능 테스트

#### 대시보드 (`/`)
- [ ] 통계 카드가 표시됨
- [ ] 차트가 렌더링됨
- [ ] 데이터가 백엔드에서 로드됨

#### 로그 목록 (`/logs`)
- [ ] 로그 목록이 표시됨
- [ ] 필터/검색이 작동함
- [ ] 로그 생성 버튼이 작동함
- [ ] 페이지네이션이 작동함

#### 프로젝트 목록 (`/projects`)
- [ ] 프로젝트 목록이 표시됨
- [ ] 프로젝트 생성이 작동함
- [ ] 프로젝트 수정이 작동함
- [ ] 프로젝트 삭제가 작동함

#### 설정 (`/settings`)
- [ ] 설정 페이지가 로드됨
- [ ] 설정 저장이 작동함

### 5. 반응형 디자인 테스트

#### 데스크톱 (1920x1080)
```
✅ 모든 요소가 적절히 배치됨
✅ 텍스트가 읽기 쉬움
```

#### 태블릿 (768x1024)
```
✅ 레이아웃이 조정됨
✅ 네비게이션이 접힘 (햄버거 메뉴)
```

#### 모바일 (375x667)
```
✅ 단일 컬럼 레이아웃
✅ 터치 친화적 버튼 크기
```

---

## CI/CD 통합

### GitHub Actions

```yaml
name: Frontend Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v2

      - name: Start Docker containers
        run: docker-compose up -d

      - name: Wait for services
        run: sleep 30

      - name: Run frontend tests
        run: ./scripts/test-frontend.sh

      - name: Stop containers
        run: docker-compose down
```

### Jenkins

```groovy
pipeline {
    agent any

    stages {
        stage('Start Services') {
            steps {
                sh 'docker-compose up -d'
                sh 'sleep 30'
            }
        }

        stage('Test Frontend') {
            steps {
                sh './scripts/test-frontend.sh'
            }
        }

        stage('Cleanup') {
            steps {
                sh 'docker-compose down'
            }
        }
    }
}
```

---

## 추가 테스트 도구

### Lighthouse (성능 테스트)

```bash
# Chrome Lighthouse 설치
npm install -g lighthouse

# 성능 측정
lighthouse http://localhost:3000 --output html --output-path ./report.html
```

### Cypress (E2E 테스트)

```bash
# Cypress 설치
npm install --save-dev cypress

# Cypress 실행
npx cypress open
```

---

## 참고 자료

- [React Testing Library](https://testing-library.com/react)
- [Jest Documentation](https://jestjs.io/)
- [Cypress Documentation](https://www.cypress.io/)
- [Chrome DevTools](https://developer.chrome.com/docs/devtools/)

---

**마지막 업데이트**: 2025-12-30
**버전**: 1.0.0
