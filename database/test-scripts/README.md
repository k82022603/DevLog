# DevLog 테스트 스크립트

이 디렉토리에는 DevLog 애플리케이션을 테스트하기 위한 다양한 스크립트가 포함되어 있습니다.

## 📋 테스트 스크립트 목록

### 1. API 테스트

#### Linux/Mac: `test-api.sh`

모든 API 엔드포인트를 테스트하는 Bash 스크립트입니다.

**사용법**:
```bash
# 실행 권한 부여
chmod +x test-api.sh

# 실행
./test-api.sh
```

**테스트 항목**:
- Health Check API
- Project API (CRUD)
- DevLog API (CRUD)
- Statistics API (주간/월간/프로젝트/기술스택)
- 필터 및 검색 기능
- 에러 핸들링

#### Windows: `test-api.bat`

Windows용 API 테스트 배치 파일입니다.

**사용법**:
```cmd
test-api.bat
```

### 2. 데이터베이스 테스트

#### Linux/Mac: `test-database.sh`

데이터베이스 연결, 스키마, 인덱스, 뷰, 트리거를 테스트하는 스크립트입니다.

**사용법**:
```bash
# 실행 권한 부여
chmod +x test-database.sh

# 실행
./test-database.sh
```

**테스트 항목**:
- 데이터베이스 연결 테스트
- 테이블 존재 확인 (5개 테이블)
- 인덱스 확인
- 뷰 확인 (3개 뷰)
- 트리거 확인
- 데이터 개수 확인
- 성능 정보 (데이터베이스 크기, 테이블 크기, 활성 연결)
- 외래키 제약조건 확인

## 🔧 사전 요구사항

### API 테스트
- **curl** 설치 필요
- DevLog 애플리케이션 실행 중 (http://localhost:8080)

### 데이터베이스 테스트
- **Docker** 실행 중
- PostgreSQL 컨테이너 실행 중 (`devlog-postgres`)

## 📊 테스트 결과 해석

### 성공적인 테스트
```
✓ PASS: Health check endpoint
✓ PASS: Get all projects
✓ PASS: Create new project
...

======================================
  Test Summary
======================================
Passed: 20
Failed: 0
======================================
All tests passed!
```

### 실패한 테스트
```
✓ PASS: Health check endpoint
✗ FAIL: Get all projects (Expected: 200, Got: 500)
...

======================================
  Test Summary
======================================
Passed: 15
Failed: 5
======================================
Some tests failed!
```

## 🐛 문제 해결

### API 테스트 실패

#### "Connection refused" 또는 "Could not connect"
```bash
# 백엔드가 실행 중인지 확인
docker-compose ps backend

# 백엔드 로그 확인
docker-compose logs backend

# 백엔드 재시작
docker-compose restart backend
```

#### "404 Not Found"
- API 엔드포인트 경로 확인
- docs/API_DOCUMENTATION.md 참조

#### "500 Internal Server Error"
```bash
# 백엔드 에러 로그 확인
docker-compose logs backend | grep ERROR

# 데이터베이스 연결 확인
docker-compose ps postgres
```

### 데이터베이스 테스트 실패

#### "PostgreSQL container is NOT running"
```bash
# 컨테이너 시작
docker-compose up -d postgres

# 상태 확인
docker-compose ps postgres
```

#### "Table does NOT exist"
```bash
# 스키마 재생성
docker exec -i devlog-postgres psql -U devlog -d devlog < ../schema.sql
```

#### "No indexes found"
```bash
# 인덱스 재생성
docker exec devlog-postgres psql -U devlog -d devlog -c "REINDEX DATABASE devlog;"
```

## 📝 수동 테스트

### curl을 이용한 수동 API 테스트

```bash
# Health Check
curl http://localhost:8080/health

# 프로젝트 목록 조회
curl http://localhost:8080/api/projects

# 프로젝트 생성
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "새 프로젝트",
    "description": "테스트",
    "status": "ACTIVE",
    "progress": 0
  }'

# 개발 로그 생성
curl -X POST http://localhost:8080/api/logs \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": 1,
    "title": "테스트 로그",
    "content": "내용",
    "mood": "GOOD",
    "logDate": "2025-12-31T00:00:00"
  }'
```

### psql을 이용한 수동 데이터베이스 테스트

```bash
# PostgreSQL 컨테이너 접속
docker exec -it devlog-postgres psql -U devlog -d devlog

# 테이블 목록 확인
\dt

# 프로젝트 조회
SELECT * FROM projects;

# 개발 로그 조회
SELECT * FROM dev_logs;

# 뷰 조회
SELECT * FROM v_project_summary;

# 종료
\q
```

## 🔄 CI/CD 통합

### GitHub Actions 예시

```yaml
name: API Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2

      - name: Start services
        run: docker-compose up -d

      - name: Wait for services
        run: sleep 30

      - name: Run API tests
        run: ./database/test-scripts/test-api.sh

      - name: Run database tests
        run: ./database/test-scripts/test-database.sh

      - name: Stop services
        run: docker-compose down
```

## 📚 추가 자료

- [API 문서](../../docs/API_DOCUMENTATION.md)
- [데이터베이스 스키마](../SCHEMA.md)
- [운영자 매뉴얼](../../docs/OPERATIONS_MANUAL.md)

---

**마지막 업데이트**: 2025-12-31
