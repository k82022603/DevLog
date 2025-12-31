# Database Setup Guide

## PostgreSQL 설치 및 설정

### 1. PostgreSQL 설치
PostgreSQL 15 버전을 설치합니다.

### 2. 데이터베이스 생성
```bash
# PostgreSQL 접속
psql -U postgres

# 데이터베이스 생성
CREATE DATABASE devlog;

# 데이터베이스 확인
\l

# 접속 종료
\q
```

### 3. 스키마 생성
```bash
# schema.sql 실행
psql -U postgres -d devlog -f schema.sql
```

### 4. 샘플 데이터 삽입 (선택사항)
```bash
# seed.sql 실행
psql -U postgres -d devlog -f seed.sql
```

## 데이터베이스 구조

### projects 테이블
- `id`: 프로젝트 ID (Primary Key)
- `name`: 프로젝트 이름
- `description`: 프로젝트 설명
- `status`: 프로젝트 상태 (ACTIVE, COMPLETED, ON_HOLD, ARCHIVED)
- `start_date`: 시작일
- `end_date`: 종료일
- `created_at`: 생성일시
- `updated_at`: 수정일시

### dev_logs 테이블
- `id`: 로그 ID (Primary Key)
- `project_id`: 프로젝트 ID (Foreign Key)
- `title`: 로그 제목
- `content`: 로그 내용
- `tags`: 태그 (쉼표로 구분)
- `log_date`: 로그 작성 날짜
- `created_at`: 생성일시
- `updated_at`: 수정일시

## 데이터베이스 관리

### 테이블 초기화
```sql
-- 모든 데이터 삭제
TRUNCATE TABLE dev_logs CASCADE;
TRUNCATE TABLE projects CASCADE;
```

### 테이블 재생성
```bash
psql -U postgres -d devlog -f schema.sql
```

## 연결 정보
- **Host**: localhost
- **Port**: 5432
- **Database**: devlog
- **Username**: postgres
- **Password**: postgres (개발 환경용, 운영 환경에서는 변경 필요)
