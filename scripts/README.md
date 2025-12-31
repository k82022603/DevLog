# DevLog API 테스트 스크립트

이 디렉토리에는 DevLog API를 테스트하기 위한 재사용 가능한 스크립트들이 포함되어 있습니다.

## 📋 사전 요구사항

### 백엔드 실행
API 테스트를 실행하기 전에 백엔드 서버가 실행 중이어야 합니다.

#### Maven이 설치된 경우:
```bash
cd backend
mvn spring-boot:run
```

#### IntelliJ IDEA 사용:
1. `backend` 폴더를 프로젝트로 열기
2. `DevLogApplication.java` 파일 열기
3. `main` 메서드 옆의 녹색 실행 버튼 클릭
4. 또는 `Shift + F10` 단축키 사용

#### VS Code 사용 (Extension Pack for Java 필요):
1. VS Code에서 `backend` 폴더 열기
2. F5를 눌러 디버깅 시작
3. 또는 왼쪽 Run and Debug 패널에서 실행

### curl 설치 확인
```bash
curl --version
```

Windows 10 버전 1803 이상에는 curl이 기본 설치되어 있습니다.

---

## 🧪 테스트 스크립트 목록

### 1. PowerShell 스크립트 (Windows 추천) ⭐

**파일**: `Test-API.ps1`

**실행 방법**:
```powershell
# scripts 디렉토리로 이동
cd scripts

# 실행 정책 우회하여 실행
powershell -ExecutionPolicy Bypass -File .\Test-API.ps1
```

**특징**:
- ✅ 가장 강력하고 상세한 테스트
- ✅ 색상으로 구분된 결과 출력
- ✅ CRUD 전체 테스트 (Create, Read, Update, Delete)
- ✅ 자동 cleanup (테스트 데이터 삭제)
- ✅ 테스트 통계 (총/성공/실패 개수)

**테스트 항목**:
1. Health Check
2. Project API (GET, POST, PUT, DELETE)
3. DevLog API (GET, POST, PUT, DELETE)
4. Statistics API (Weekly, Monthly, Project, Tech Stack)
5. Cleanup (테스트 데이터 자동 삭제)

---

### 2. Batch 스크립트 (Windows 간단 버전)

**파일**: `test-api.bat`

**실행 방법**:
```cmd
cd scripts
test-api.bat
```

**특징**:
- ✅ 간단한 테스트 (주요 엔드포인트만)
- ✅ cmd 환경에서 바로 실행 가능
- ✅ 추가 설정 불필요

---

### 3. Bash 스크립트 (Linux/Mac/Git Bash)

**파일**: `test-api.sh`

**실행 방법**:
```bash
cd scripts
chmod +x test-api.sh
./test-api.sh
```

**특징**:
- ✅ Linux/Mac 환경 지원
- ✅ Git Bash에서도 실행 가능
- ✅ PowerShell 버전과 동일한 기능

---

## 🎯 사용 예시

### 완전한 테스트 (PowerShell)

```powershell
# 1. 백엔드 실행 (새 터미널)
cd backend
mvn spring-boot:run

# 2. 테스트 실행 (다른 터미널)
cd scripts
powershell -ExecutionPolicy Bypass -File .\Test-API.ps1
```

### 출력 예시

```
======================================
  DevLog API Testing Started
======================================

[1] Health Check
✓ Health Check (Status: 200)

[2] Project API Tests
✓ Get all projects (Status: 200)
✓ Create project (Status: 201)
  Created project with ID: 1
✓ Get project by ID (Status: 200)
✓ Update project (Status: 200)

[3] DevLog API Tests
✓ Get all logs (Status: 200)
✓ Create log (Status: 201)
  Created log with ID: 1
✓ Get log by ID (Status: 200)
✓ Update log (Status: 200)
✓ Get logs by project (Status: 200)

[4] Statistics API Tests
✓ Get weekly statistics (Status: 200)
✓ Get monthly statistics (Status: 200)
✓ Get project statistics (Status: 200)
✓ Get tech stack statistics (Status: 200)

[5] Cleanup
✓ Delete log (Status: 204)
✓ Delete project (Status: 204)

======================================
  Test Summary
======================================
Total Tests:  16
Passed:       16
Failed:       0

All tests passed! ✓
```

---

## 🔧 트러블슈팅

### 문제: "Connection refused" 오류

**원인**: 백엔드 서버가 실행되지 않았거나 포트가 다름

**해결**:
1. 백엔드가 실행 중인지 확인
2. `http://localhost:8080/api/health` 브라우저에서 접속
3. 포트가 다르면 스크립트의 `$baseUrl` 수정

### 문제: "404 Not Found" 오류

**원인**: API 경로가 다를 수 있음

**해결**:
1. 백엔드 로그에서 실제 등록된 경로 확인
2. `application.properties`에서 `server.servlet.context-path` 확인
3. 필요시 스크립트의 `baseUrl` 수정

### 문제: PowerShell 실행 정책 오류

**원인**: PowerShell 실행 정책 제한

**해결**:
```powershell
# 방법 1: 일회성 우회
powershell -ExecutionPolicy Bypass -File .\Test-API.ps1

# 방법 2: 현재 사용자에 대해 정책 변경 (관리자 권한)
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
```

---

## 📝 커스텀 테스트 작성

### 수동으로 단일 API 테스트

```powershell
# Health Check
curl http://localhost:8080/api/health

# Get all projects
curl http://localhost:8080/api/projects

# Create project
curl -X POST http://localhost:8080/api/projects `
  -H "Content-Type: application/json" `
  -d '{
    "name": "My Project",
    "description": "Test",
    "status": "ACTIVE",
    "startDate": "2025-01-01T00:00:00",
    "progress": 0
  }'

# Get project by ID
curl http://localhost:8080/api/projects/1

# Delete project
curl -X DELETE http://localhost:8080/api/projects/1
```

---

## 🚀 CI/CD 통합

### GitHub Actions 예시

```yaml
name: API Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2

      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'

      - name: Start Backend
        run: |
          cd backend
          mvn spring-boot:run &
          sleep 30

      - name: Run API Tests
        run: |
          cd scripts
          chmod +x test-api.sh
          ./test-api.sh
```

---

## 📚 추가 리소스

- [DevLog API 문서](../docs/API.md)
- [프로젝트 아키텍처](../docs/ARCHITECTURE.md)
- [개발 가이드](../CLAUDE.md)

---

## ✨ 기여하기

새로운 테스트 케이스를 추가하려면:

1. 해당 스크립트 파일 수정
2. 테스트 함수 추가
3. README 업데이트
4. Pull Request 생성

---

**Last Updated**: 2025-12-30
**Version**: 1.0.0
