# Docker 환경 설정 가이드

## 개요
DevLog 프로젝트는 Docker Compose를 사용하여 PostgreSQL 데이터베이스를 관리합니다.

## 사전 요구사항
- Docker Desktop 설치 완료
- Docker Compose 설치 완료

## 구성 요소

### 1. PostgreSQL (포트: 5432)
- **이미지**: postgres:15-alpine
- **컨테이너명**: devlog-postgres
- **데이터베이스**: devlog
- **사용자**: devlog
- **비밀번호**: devlog123

### 2. pgAdmin (포트: 5050) - 선택사항
- **이미지**: dpage/pgadmin4:latest
- **컨테이너명**: devlog-pgadmin
- **이메일**: admin@devlog.com
- **비밀번호**: admin123

## 명령어

### 데이터베이스 시작
```bash
# PostgreSQL만 시작
docker-compose up -d postgres

# PostgreSQL + pgAdmin 시작
docker-compose up -d
```

### 데이터베이스 중지
```bash
# 모든 서비스 중지
docker-compose down

# 데이터까지 삭제 (주의!)
docker-compose down -v
```

### 로그 확인
```bash
# PostgreSQL 로그
docker-compose logs -f postgres

# pgAdmin 로그
docker-compose logs -f pgadmin
```

### 데이터베이스 접속
```bash
# psql 접속
docker exec -it devlog-postgres psql -U devlog -d devlog

# 테이블 목록 확인
docker exec devlog-postgres psql -U devlog -d devlog -c "\dt"

# 데이터 조회
docker exec devlog-postgres psql -U devlog -d devlog -c "SELECT * FROM projects;"
```

## 초기 데이터베이스 설정

Docker Compose로 PostgreSQL을 처음 시작하면:
1. `database/schema.sql` 자동 실행 → 테이블 생성
2. `database/seed.sql` 자동 실행 → 샘플 데이터 삽입

### 데이터베이스 재설정
```bash
# 1. 컨테이너와 볼륨 삭제
docker-compose down -v

# 2. 다시 시작 (스키마와 샘플 데이터 자동 생성)
docker-compose up -d postgres

# 3. 15초 대기 후 확인
docker exec devlog-postgres psql -U devlog -d devlog -c "\dt"
```

## pgAdmin 사용법

### 1. 접속
- URL: http://localhost:5050
- 이메일: admin@devlog.com
- 비밀번호: admin123

### 2. 서버 추가
1. 좌측 메뉴에서 "Servers" 우클릭 → "Register" → "Server"
2. **General 탭**
   - Name: DevLog
3. **Connection 탭**
   - Host name/address: postgres (컨테이너명)
   - Port: 5432
   - Maintenance database: devlog
   - Username: devlog
   - Password: devlog123
   - Save password: 체크
4. "Save" 클릭

### 3. 데이터베이스 탐색
- Servers → DevLog → Databases → devlog → Schemas → public → Tables

## 데이터 백업 및 복구

### 백업
```bash
# 전체 데이터베이스 백업
docker exec devlog-postgres pg_dump -U devlog devlog > backup.sql

# 특정 테이블만 백업
docker exec devlog-postgres pg_dump -U devlog -t projects devlog > projects_backup.sql
```

### 복구
```bash
# 백업 파일로 복구
docker exec -i devlog-postgres psql -U devlog devlog < backup.sql
```

## 볼륨 관리

### 데이터 위치
PostgreSQL 데이터는 Docker 볼륨에 저장됩니다:
- 볼륨명: `03devlog_postgres_data`
- 위치: Docker Desktop의 볼륨 관리에서 확인 가능

### 볼륨 확인
```bash
# 볼륨 목록
docker volume ls

# 볼륨 상세 정보
docker volume inspect 03devlog_postgres_data
```

## 트러블슈팅

### 포트 충돌 (5432 포트 사용 중)
```bash
# 로컬 PostgreSQL 중지
# Windows: services.msc에서 PostgreSQL 서비스 중지
# Mac: brew services stop postgresql

# 또는 docker-compose.yml에서 포트 변경
# ports:
#   - "15432:5432"  # 호스트 포트를 15432로 변경
```

### 컨테이너 재시작
```bash
# 컨테이너 재시작
docker-compose restart postgres

# 컨테이너 상태 확인
docker-compose ps
```

### 로그 확인
```bash
# 에러 로그 확인
docker-compose logs postgres | grep -i error

# 실시간 로그 모니터링
docker-compose logs -f postgres
```

## 성능 최적화

### PostgreSQL 설정 (선택사항)
`docker-compose.yml`에 환경 변수 추가:
```yaml
environment:
  - POSTGRES_SHARED_BUFFERS=256MB
  - POSTGRES_EFFECTIVE_CACHE_SIZE=1GB
  - POSTGRES_MAX_CONNECTIONS=100
```

## 보안 주의사항

### 프로덕션 환경
1. **비밀번호 변경**
   - `docker-compose.yml`의 `POSTGRES_PASSWORD` 변경
   - `application.yml`의 password도 동일하게 변경

2. **외부 접속 차단**
   - 프로덕션에서는 포트를 외부에 노출하지 않음
   - `ports` 설정 제거 또는 `127.0.0.1:5432:5432`로 변경

3. **환경 변수 사용**
   - `.env` 파일 생성하여 비밀번호 관리
   - `.gitignore`에 `.env` 추가

## 유용한 쿼리

### 데이터베이스 상태 확인
```sql
-- 데이터베이스 크기
SELECT pg_size_pretty(pg_database_size('devlog'));

-- 테이블별 크기
SELECT
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename))
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- 현재 연결 수
SELECT count(*) FROM pg_stat_activity WHERE datname = 'devlog';
```

### 데이터 검증
```sql
-- 모든 테이블의 레코드 수
SELECT 'projects' as table_name, COUNT(*) FROM projects
UNION ALL
SELECT 'dev_logs', COUNT(*) FROM dev_logs
UNION ALL
SELECT 'tech_tags', COUNT(*) FROM tech_tags
UNION ALL
SELECT 'log_tech_tags', COUNT(*) FROM log_tech_tags
UNION ALL
SELECT 'project_stats', COUNT(*) FROM project_stats;
```

---

## 🚀 프로덕션 배포

### 프로덕션 Docker Compose 설정

프로덕션 환경에서는 다음 사항을 고려하세요:

```yaml
# docker-compose.prod.yml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: devlog
      POSTGRES_USER: devlog
      POSTGRES_PASSWORD: ${DB_PASSWORD}  # 환경 변수 사용
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./database/schema.sql:/docker-entrypoint-initdb.d/01-schema.sql
      - ./database/seed.sql:/docker-entrypoint-initdb.d/02-seed.sql
    restart: always
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U devlog"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
    driver: local
```

### 배포 체크리스트

```bash
# 1. 환경 변수 설정
export DB_PASSWORD="secure_password_123"
export API_URL="https://api.example.com"

# 2. 프로덕션 docker-compose 실행
docker-compose -f docker-compose.prod.yml up -d

# 3. 서비스 상태 확인
docker-compose -f docker-compose.prod.yml ps

# 4. 헬스 체크
curl -f http://localhost:5432 || exit 1

# 5. 로그 확인
docker-compose -f docker-compose.prod.yml logs -f postgres
```

### 보안 권장사항

1. **데이터베이스 암호**
   - 강력한 암호 사용 (최소 16자, 특수문자 포함)
   - 환경 변수로 관리 (`.env` 파일 사용, Git 제외)

2. **네트워크**
   - 데이터베이스 포트(5432) 내부 트래픽만 허용
   - pgAdmin(5050)은 VPN/내부망에서만 접근

3. **백업**
   - 일일 자동 백업 설정
   - 별도 스토리지에 저장
   - 정기적인 복구 테스트

4. **모니터링**
   - 디스크 사용량 모니터링
   - 데이터베이스 성능 모니터링
   - 로그 수집 및 분석

### 자동 백업 설정

```bash
# 백업 스크립트 (backup.sh)
#!/bin/bash
BACKUP_DIR="/var/backups/devlog"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR

docker exec devlog-postgres pg_dump \
  -U devlog \
  -d devlog \
  -F c \
  -f /tmp/devlog-backup-${TIMESTAMP}.dump

mv /tmp/devlog-backup-${TIMESTAMP}.dump $BACKUP_DIR/

# 7일 이상 된 파일 삭제
find $BACKUP_DIR -type f -mtime +7 -delete
```

**Cron 등록**:
```bash
# 매일 자정에 백업
0 0 * * * /usr/local/bin/backup.sh >> /var/log/devlog-backup.log 2>&1
```

---

## 추가 자료
- [PostgreSQL 공식 문서](https://www.postgresql.org/docs/15/)
- [Docker Compose 문서](https://docs.docker.com/compose/)
- [pgAdmin 사용법](https://www.pgadmin.org/docs/)
- [OPERATIONS_MANUAL.md](./OPERATIONS_MANUAL.md) - 운영 가이드
- [SETUP.md](./SETUP.md) - 설치 가이드

---

*Last Updated: 2025-12-31*
*DevLog Docker Guide v1.1.0*
