# DevLog 운영자 매뉴얼

## 📌 목차

- [개요](#개요)
- [시스템 요구사항](#시스템-요구사항)
- [초기 설치 및 설정](#초기-설치-및-설정)
- [일상 운영](#일상-운영)
- [모니터링](#모니터링)
- [백업 및 복구](#백업-및-복구)
- [성능 최적화](#성능-최적화)
- [보안 관리](#보안-관리)
- [문제 해결](#문제-해결)
- [업그레이드 및 유지보수](#업그레이드-및-유지보수)

---

## 개요

이 문서는 DevLog 애플리케이션의 운영 및 유지보수를 담당하는 시스템 관리자를 위한 가이드입니다.

**대상 독자**: 시스템 관리자, DevOps 엔지니어, SRE

**전제 조건**:
- Docker 및 Docker Compose 기본 지식
- Linux 명령어 기본 지식
- PostgreSQL 기본 지식
- 네트워크 기본 지식

---

## 시스템 요구사항

### 하드웨어 요구사항

#### 최소 사양
- **CPU**: 2 Core
- **RAM**: 4GB
- **디스크**: 20GB SSD
- **네트워크**: 10Mbps

#### 권장 사양
- **CPU**: 4 Core
- **RAM**: 8GB
- **디스크**: 50GB SSD
- **네트워크**: 100Mbps

### 소프트웨어 요구사항

- **운영체제**: Ubuntu 20.04 LTS 이상 또는 CentOS 8 이상
- **Docker**: 20.10 이상
- **Docker Compose**: 2.0 이상
- **(선택) Git**: 최신 버전

---

## 초기 설치 및 설정

### 1. 시스템 준비

#### 1.1 Docker 설치

```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install -y docker.io docker-compose

# Docker 서비스 시작 및 자동 시작 설정
sudo systemctl start docker
sudo systemctl enable docker

# 현재 사용자를 docker 그룹에 추가 (재로그인 필요)
sudo usermod -aG docker $USER
```

#### 1.2 방화벽 설정

```bash
# UFW (Ubuntu)
sudo ufw allow 80/tcp      # Frontend
sudo ufw allow 443/tcp     # HTTPS
sudo ufw allow 8080/tcp    # Backend API
sudo ufw allow 5432/tcp    # PostgreSQL (필요시)
sudo ufw enable

# Firewalld (CentOS)
sudo firewall-cmd --permanent --add-port=80/tcp
sudo firewall-cmd --permanent --add-port=443/tcp
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
```

### 2. 애플리케이션 설치

#### 2.1 소스 코드 다운로드

```bash
# Git을 사용하는 경우
git clone https://github.com/yourusername/devlog.git
cd devlog

# 또는 압축 파일 다운로드
wget https://github.com/yourusername/devlog/archive/main.zip
unzip main.zip
cd devlog-main
```

#### 2.2 환경 변수 설정

```bash
# .env 파일 생성
cp .env.example .env

# 환경 변수 편집
nano .env
```

**프로덕션 환경 필수 변경 사항**:

```bash
# 데이터베이스 비밀번호 변경
POSTGRES_PASSWORD=강력한_비밀번호_사용

# pgAdmin 비밀번호 변경
PGADMIN_DEFAULT_PASSWORD=강력한_비밀번호_사용

# CORS 설정 (실제 도메인으로 변경)
CORS_ALLOWED_ORIGINS=https://your-domain.com

# API Base URL (실제 도메인으로 변경)
REACT_APP_API_BASE_URL=https://your-domain.com/api
```

#### 2.3 초기 실행

```bash
# 모든 서비스 시작
docker-compose up -d

# 로그 확인
docker-compose logs -f

# 서비스 상태 확인
docker-compose ps
```

**예상 출력**:
```
NAME                   STATUS              PORTS
devlog-postgres        Up                  5432/tcp
devlog-backend         Up                  0.0.0.0:8080->8080/tcp
devlog-frontend        Up                  0.0.0.0:80->80/tcp
devlog-pgadmin         Up                  0.0.0.0:5050->80/tcp
```

#### 2.4 헬스 체크

```bash
# 백엔드 API 헬스 체크
curl http://localhost:8080/health

# 예상 응답
# {"status":"OK","message":"DevLog API is running","timestamp":1735632000000}

# 프론트엔드 접속 확인
curl -I http://localhost

# 예상 응답
# HTTP/1.1 200 OK
```

### 3. 데이터베이스 초기화

#### 3.1 스키마 확인

```bash
# PostgreSQL 컨테이너 접속
docker exec -it devlog-postgres psql -U devlog -d devlog

# 테이블 목록 확인
\dt

# 예상 출력
#                List of relations
#  Schema |      Name       | Type  | Owner
# --------+-----------------+-------+--------
#  public | dev_logs        | table | devlog
#  public | log_tech_tags   | table | devlog
#  public | project_stats   | table | devlog
#  public | projects        | table | devlog
#  public | tech_tags       | table | devlog

# PostgreSQL 종료
\q
```

#### 3.2 샘플 데이터 삽입 (선택)

```bash
# 샘플 데이터 삽입
docker exec -i devlog-postgres psql -U devlog -d devlog < database/seed-data.sql

# 데이터 확인
docker exec devlog-postgres psql -U devlog -d devlog -c "SELECT COUNT(*) FROM projects;"
docker exec devlog-postgres psql -U devlog -d devlog -c "SELECT COUNT(*) FROM dev_logs;"
```

---

## 일상 운영

### 서비스 관리

#### 서비스 시작

```bash
# 모든 서비스 시작
docker-compose up -d

# 특정 서비스만 시작
docker-compose up -d postgres
docker-compose up -d backend
docker-compose up -d frontend
```

#### 서비스 중지

```bash
# 모든 서비스 중지
docker-compose stop

# 특정 서비스만 중지
docker-compose stop backend
```

#### 서비스 재시작

```bash
# 모든 서비스 재시작
docker-compose restart

# 특정 서비스만 재시작
docker-compose restart backend
docker-compose restart frontend
```

#### 서비스 완전 제거

```bash
# 컨테이너 중지 및 제거 (볼륨 유지)
docker-compose down

# 컨테이너 및 볼륨 모두 제거 (데이터 삭제 주의!)
docker-compose down -v
```

### 로그 관리

#### 실시간 로그 확인

```bash
# 모든 서비스 로그
docker-compose logs -f

# 특정 서비스 로그
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres

# 최근 100줄만 확인
docker-compose logs --tail=100 backend
```

#### 로그 검색

```bash
# ERROR 로그만 검색
docker-compose logs backend | grep ERROR

# 특정 날짜 로그 검색
docker-compose logs --since 2025-12-31 backend

# 특정 시간 범위 로그
docker-compose logs --since 2025-12-31T10:00:00 --until 2025-12-31T11:00:00 backend
```

#### 로그 파일 저장

```bash
# 로그를 파일로 저장
docker-compose logs backend > backend-logs-$(date +%Y%m%d).log

# 압축하여 저장
docker-compose logs backend | gzip > backend-logs-$(date +%Y%m%d).log.gz
```

### 컨테이너 관리

#### 컨테이너 상태 확인

```bash
# 실행 중인 컨테이너 확인
docker-compose ps

# 자세한 정보 확인
docker stats

# 특정 컨테이너 상세 정보
docker inspect devlog-backend
```

#### 컨테이너 리소스 사용량

```bash
# 실시간 리소스 모니터링
docker stats devlog-backend devlog-frontend devlog-postgres

# 디스크 사용량
docker system df

# 컨테이너별 디스크 사용량
docker ps -s
```

#### 컨테이너 정리

```bash
# 중지된 컨테이너 제거
docker container prune

# 사용하지 않는 이미지 제거
docker image prune -a

# 사용하지 않는 볼륨 제거
docker volume prune

# 전체 정리 (주의!)
docker system prune -a
```

---

## 모니터링

### 시스템 헬스 체크

#### 자동화된 헬스 체크 스크립트

`scripts/health-check.sh` 생성:

```bash
#!/bin/bash

# DevLog Health Check Script

echo "======================================"
echo "DevLog System Health Check"
echo "======================================"
echo ""

# 1. Docker 서비스 확인
echo "1. Checking Docker services..."
docker-compose ps

# 2. 백엔드 API 확인
echo ""
echo "2. Checking Backend API..."
BACKEND_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/health)
if [ "$BACKEND_STATUS" -eq 200 ]; then
    echo "✓ Backend API: OK (HTTP $BACKEND_STATUS)"
else
    echo "✗ Backend API: FAILED (HTTP $BACKEND_STATUS)"
fi

# 3. 프론트엔드 확인
echo ""
echo "3. Checking Frontend..."
FRONTEND_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost)
if [ "$FRONTEND_STATUS" -eq 200 ]; then
    echo "✓ Frontend: OK (HTTP $FRONTEND_STATUS)"
else
    echo "✗ Frontend: FAILED (HTTP $FRONTEND_STATUS)"
fi

# 4. 데이터베이스 확인
echo ""
echo "4. Checking Database..."
DB_CHECK=$(docker exec devlog-postgres pg_isready -U devlog)
if [ $? -eq 0 ]; then
    echo "✓ Database: OK"
else
    echo "✗ Database: FAILED"
fi

# 5. 디스크 사용량 확인
echo ""
echo "5. Checking Disk Usage..."
docker system df

echo ""
echo "======================================"
echo "Health Check Complete"
echo "======================================"
```

실행 권한 부여 및 실행:

```bash
chmod +x scripts/health-check.sh
./scripts/health-check.sh
```

#### Cron을 이용한 정기 헬스 체크

```bash
# crontab 편집
crontab -e

# 매 5분마다 헬스 체크 실행 및 로그 저장
*/5 * * * * /path/to/devlog/scripts/health-check.sh >> /var/log/devlog-health.log 2>&1
```

### 애플리케이션 모니터링

#### 데이터베이스 통계 확인

```bash
# 데이터베이스 크기 확인
docker exec devlog-postgres psql -U devlog -d devlog -c "
SELECT
    pg_size_pretty(pg_database_size('devlog')) as database_size;
"

# 테이블별 크기 확인
docker exec devlog-postgres psql -U devlog -d devlog -c "
SELECT
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
"

# 활성 연결 수 확인
docker exec devlog-postgres psql -U devlog -d devlog -c "
SELECT count(*) as active_connections
FROM pg_stat_activity
WHERE datname = 'devlog';
"
```

#### API 응답 시간 모니터링

```bash
# 간단한 응답 시간 측정
time curl -s http://localhost:8080/api/projects > /dev/null

# 더 자세한 측정
curl -o /dev/null -s -w "Time: %{time_total}s\nStatus: %{http_code}\n" \
  http://localhost:8080/api/projects
```

---

## 백업 및 복구

### 데이터베이스 백업

#### 수동 백업

```bash
# 백업 디렉토리 생성
mkdir -p /var/backups/devlog

# 전체 데이터베이스 백업
docker exec devlog-postgres pg_dump -U devlog devlog | \
  gzip > /var/backups/devlog/devlog-backup-$(date +%Y%m%d-%H%M%S).sql.gz

# 백업 확인
ls -lh /var/backups/devlog/
```

#### 자동 백업 스크립트

`scripts/backup.sh` 생성:

```bash
#!/bin/bash

# DevLog Backup Script

BACKUP_DIR="/var/backups/devlog"
RETENTION_DAYS=30
DATE=$(date +%Y%m%d-%H%M%S)

echo "Starting DevLog backup at $(date)"

# 백업 디렉토리 생성
mkdir -p $BACKUP_DIR

# 데이터베이스 백업
docker exec devlog-postgres pg_dump -U devlog devlog | \
  gzip > $BACKUP_DIR/devlog-backup-$DATE.sql.gz

if [ $? -eq 0 ]; then
    echo "✓ Database backup completed: devlog-backup-$DATE.sql.gz"
else
    echo "✗ Database backup failed!"
    exit 1
fi

# 오래된 백업 삭제 (30일 이상)
find $BACKUP_DIR -name "devlog-backup-*.sql.gz" -mtime +$RETENTION_DAYS -delete
echo "✓ Old backups cleaned up (older than $RETENTION_DAYS days)"

echo "Backup completed at $(date)"
```

실행 권한 부여:

```bash
chmod +x scripts/backup.sh
```

#### Cron을 이용한 자동 백업

```bash
# crontab 편집
crontab -e

# 매일 새벽 2시에 백업 실행
0 2 * * * /path/to/devlog/scripts/backup.sh >> /var/log/devlog-backup.log 2>&1
```

### 데이터베이스 복구

#### 백업에서 복구

```bash
# 1. 기존 데이터베이스 중지
docker-compose stop backend

# 2. 데이터베이스 초기화 (주의: 기존 데이터 삭제!)
docker exec devlog-postgres psql -U devlog -d postgres -c "DROP DATABASE IF EXISTS devlog;"
docker exec devlog-postgres psql -U devlog -d postgres -c "CREATE DATABASE devlog OWNER devlog;"

# 3. 백업 파일 복구
gunzip < /var/backups/devlog/devlog-backup-YYYYMMDD-HHMMSS.sql.gz | \
  docker exec -i devlog-postgres psql -U devlog -d devlog

# 4. 백엔드 재시작
docker-compose start backend

# 5. 데이터 확인
docker exec devlog-postgres psql -U devlog -d devlog -c "SELECT COUNT(*) FROM projects;"
docker exec devlog-postgres psql -U devlog -d devlog -c "SELECT COUNT(*) FROM dev_logs;"
```

### 전체 시스템 백업

```bash
# Docker 볼륨 백업
docker run --rm \
  -v devlog_postgres_data:/data \
  -v $(pwd)/backups:/backup \
  ubuntu tar czf /backup/postgres-volume-backup-$(date +%Y%m%d).tar.gz /data

# 애플리케이션 코드 및 설정 백업
tar czf devlog-app-backup-$(date +%Y%m%d).tar.gz \
  --exclude='node_modules' \
  --exclude='target' \
  --exclude='.git' \
  .
```

---

## 성능 최적화

### 데이터베이스 최적화

#### 인덱스 재구축

```bash
# 모든 인덱스 재구축
docker exec devlog-postgres psql -U devlog -d devlog -c "REINDEX DATABASE devlog;"

# 특정 테이블 인덱스 재구축
docker exec devlog-postgres psql -U devlog -d devlog -c "REINDEX TABLE dev_logs;"
```

#### VACUUM 및 ANALYZE

```bash
# 전체 데이터베이스 VACUUM
docker exec devlog-postgres psql -U devlog -d devlog -c "VACUUM ANALYZE;"

# 특정 테이블만
docker exec devlog-postgres psql -U devlog -d devlog -c "VACUUM ANALYZE dev_logs;"

# VACUUM FULL (디스크 공간 회수, 테이블 잠금 발생)
docker exec devlog-postgres psql -U devlog -d devlog -c "VACUUM FULL;"
```

#### 슬로우 쿼리 확인

```bash
# 느린 쿼리 로깅 활성화
docker exec devlog-postgres psql -U devlog -d devlog -c "
ALTER SYSTEM SET log_min_duration_statement = 1000;  -- 1초 이상 쿼리 로깅
SELECT pg_reload_conf();
"

# 로그 확인
docker-compose logs postgres | grep "duration:"
```

### 애플리케이션 최적화

#### 컨테이너 리소스 제한

`docker-compose.yml` 수정:

```yaml
services:
  backend:
    # ... 기존 설정 ...
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G

  postgres:
    # ... 기존 설정 ...
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
```

#### 캐싱 설정

```bash
# Nginx 캐싱 설정 확인
docker exec devlog-frontend cat /etc/nginx/conf.d/default.conf

# 필요시 캐시 클리어
docker exec devlog-frontend rm -rf /var/cache/nginx/*
docker-compose restart frontend
```

---

## 보안 관리

### SSL/TLS 설정

#### Let's Encrypt 인증서 발급

```bash
# Certbot 설치
sudo apt-get install certbot python3-certbot-nginx

# 인증서 발급
sudo certbot --nginx -d your-domain.com -d www.your-domain.com

# 자동 갱신 설정 확인
sudo certbot renew --dry-run
```

#### Nginx SSL 설정

`frontend/nginx.conf` 수정:

```nginx
server {
    listen 443 ssl http2;
    server_name your-domain.com;

    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    # ... 나머지 설정 ...
}

server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}
```

### 방화벽 설정

```bash
# 불필요한 포트 차단
sudo ufw deny 5432/tcp  # PostgreSQL (외부 접근 차단)
sudo ufw deny 5050/tcp  # pgAdmin (외부 접근 차단)

# 허용된 포트만 열기
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
```

### 비밀번호 정책

```bash
# 강력한 비밀번호 생성
openssl rand -base64 32

# .env 파일 권한 설정
chmod 600 .env
```

---

## 문제 해결

### 일반적인 문제

#### 1. 백엔드가 시작되지 않음

**증상**: 백엔드 컨테이너가 계속 재시작됨

**원인 확인**:
```bash
docker-compose logs backend
```

**해결 방법**:

1. **데이터베이스 연결 실패**:
```bash
# PostgreSQL 상태 확인
docker-compose ps postgres

# 네트워크 확인
docker network inspect devlog_default
```

2. **포트 충돌**:
```bash
# 8080 포트 사용 프로세스 확인
sudo lsof -i :8080
sudo netstat -tulpn | grep 8080

# 프로세스 종료
sudo kill -9 <PID>
```

#### 2. 데이터베이스 연결 오류

**증상**: `Connection refused` 또는 `could not connect to server`

**해결 방법**:

```bash
# 1. PostgreSQL 상태 확인
docker exec devlog-postgres pg_isready -U devlog

# 2. 로그 확인
docker-compose logs postgres

# 3. 컨테이너 재시작
docker-compose restart postgres

# 4. 연결 테스트
docker exec devlog-postgres psql -U devlog -d devlog -c "SELECT 1;"
```

#### 3. 디스크 공간 부족

**증상**: `no space left on device`

**해결 방법**:

```bash
# 1. 디스크 사용량 확인
df -h

# 2. Docker 디스크 사용량 확인
docker system df

# 3. 불필요한 리소스 정리
docker system prune -a --volumes

# 4. 오래된 로그 파일 삭제
find /var/lib/docker/containers/ -name "*.log" -mtime +7 -delete
```

#### 4. 메모리 부족

**증상**: 컨테이너가 갑자기 종료됨, OOM (Out of Memory) 오류

**해결 방법**:

```bash
# 1. 메모리 사용량 확인
docker stats

# 2. 컨테이너 재시작
docker-compose restart

# 3. 메모리 제한 설정 (docker-compose.yml)
# deploy.resources.limits.memory 설정 참조
```

#### 5. 프론트엔드 빌드 실패

**증상**: `npm ERR!` 또는 빌드 실패

**해결 방법**:

```bash
# 1. 로컬에서 빌드 테스트
cd frontend
npm install
npm run build

# 2. Docker 캐시 클리어 후 재빌드
docker-compose build --no-cache frontend
docker-compose up -d frontend
```

### 성능 문제 해결

#### 1. API 응답 속도 느림

**진단**:
```bash
# 응답 시간 측정
time curl http://localhost:8080/api/logs

# 데이터베이스 슬로우 쿼리 확인
docker-compose logs postgres | grep "duration:"
```

**해결**:
- 데이터베이스 인덱스 추가
- 쿼리 최적화
- 캐싱 도입

#### 2. 데이터베이스 성능 저하

**진단**:
```bash
# 활성 쿼리 확인
docker exec devlog-postgres psql -U devlog -d devlog -c "
SELECT pid, now() - query_start as duration, query
FROM pg_stat_activity
WHERE state = 'active'
ORDER BY duration DESC;
"
```

**해결**:
```bash
# VACUUM ANALYZE 실행
docker exec devlog-postgres psql -U devlog -d devlog -c "VACUUM ANALYZE;"

# 인덱스 재구축
docker exec devlog-postgres psql -U devlog -d devlog -c "REINDEX DATABASE devlog;"
```

---

## 업그레이드 및 유지보수

### 애플리케이션 업그레이드

#### 1. 백업

```bash
# 데이터베이스 백업
./scripts/backup.sh

# 현재 버전 확인
docker-compose exec backend java -version
docker-compose exec frontend node --version
```

#### 2. 새 버전 다운로드

```bash
# Git을 사용하는 경우
git fetch origin
git checkout v2.0.0  # 원하는 버전

# 또는 압축 파일 다운로드
wget https://github.com/yourusername/devlog/archive/v2.0.0.zip
```

#### 3. 이미지 재빌드

```bash
# 이미지 재빌드
docker-compose build

# 또는 특정 서비스만
docker-compose build backend
docker-compose build frontend
```

#### 4. 서비스 재시작

```bash
# 무중단 배포를 위한 순차 재시작
docker-compose up -d --no-deps --build backend
docker-compose up -d --no-deps --build frontend
```

#### 5. 검증

```bash
# 헬스 체크
./scripts/health-check.sh

# API 테스트
curl http://localhost:8080/health
```

### 데이터베이스 마이그레이션

```bash
# 1. 백업
./scripts/backup.sh

# 2. 마이그레이션 스크립트 실행
docker exec -i devlog-postgres psql -U devlog -d devlog < database/migrations/v2.0.0.sql

# 3. 검증
docker exec devlog-postgres psql -U devlog -d devlog -c "\dt"
```

### 롤백 절차

```bash
# 1. 이전 버전으로 체크아웃
git checkout v1.0.0

# 2. 이미지 재빌드
docker-compose build

# 3. 서비스 재시작
docker-compose up -d

# 4. 데이터베이스 복구 (필요시)
gunzip < /var/backups/devlog/devlog-backup-YYYYMMDD-HHMMSS.sql.gz | \
  docker exec -i devlog-postgres psql -U devlog -d devlog
```

---

## 부록

### 유용한 명령어 모음

```bash
# 전체 시스템 상태 확인
docker-compose ps && docker stats --no-stream

# 데이터베이스 크기 확인
docker exec devlog-postgres psql -U devlog -d devlog -c "SELECT pg_size_pretty(pg_database_size('devlog'));"

# 로그 파일 크기 확인
docker exec devlog-backend du -sh /var/log/*

# 컨테이너 IP 주소 확인
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' devlog-backend

# 네트워크 연결 테스트
docker exec devlog-backend ping -c 3 devlog-postgres
```

### 모니터링 도구 설정 (Prometheus + Grafana)

향후 확장을 위한 모니터링 스택 설정은 별도 문서 참조:
- `docs/MONITORING_SETUP.md`

---

## 연락처 및 지원

**기술 지원**:
- GitHub Issues: https://github.com/yourusername/devlog/issues
- Email: support@devlog.com

**긴급 문의**:
- On-call: +82-10-1234-5678

---

**DevLog Operations Manual v1.0**
*Last Updated: 2025-12-31*
