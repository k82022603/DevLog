# DevLog 설치 및 실행 가이드

> 🚀 **빠른 시작**: Docker를 사용하면 3분 안에 시작할 수 있습니다!

---

## ⚡ 빠른 시작 (Docker 권장)

### 최소 요구사항
- **Docker 20.10+**
- **Docker Compose 2.0+**

### 3단계 설정

```bash
# 1️⃣ 저장소 클론
git clone https://github.com/k82022603/DevLog.git
cd DevLog

# 2️⃣ Docker Compose로 백엔드 빌드 및 실행
docker-compose up -d

# 3️⃣ 프론트엔드 실행 (별도 터미널)
cd frontend
npm install
npm start
```

### 접속 정보
- 🌐 **프론트엔드**: http://localhost:3000
- 🔌 **백엔드 API**: http://localhost:8080/api
- 🗄️ **pgAdmin** (DB 관리): http://localhost:5050
  - 이메일: `admin@devlog.com`
  - 비밀번호: `admin123`

자세한 내용은 [DOCKER.md](./DOCKER.md)를 참조하세요.

---

## 📦 전통적인 설치 (Java/Node.js 직접 설치)

### 사전 요구사항

#### 필수 소프트웨어
- **Java 17** 이상
- **Node.js 18** 이상
- **PostgreSQL 15**
- **Maven 3.8** 이상

### 설치 단계

### 1. 프로젝트 클론
```bash
git clone <repository-url>
cd devlog
```

### 2. 데이터베이스 설정

#### PostgreSQL 설치 및 데이터베이스 생성
```bash
# PostgreSQL 접속
psql -U postgres

# 데이터베이스 생성
CREATE DATABASE devlog;
\q
```

#### 스키마 생성
```bash
cd database
psql -U postgres -d devlog -f schema.sql
```

#### 샘플 데이터 삽입 (선택사항)
```bash
psql -U postgres -d devlog -f seed.sql
```

### 3. 백엔드 설정 및 실행

#### 데이터베이스 연결 정보 확인
`backend/src/main/resources/application.yml` 파일에서 데이터베이스 연결 정보를 확인합니다.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/devlog
    username: postgres
    password: postgres
```

#### Maven 의존성 설치 및 실행
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

백엔드 서버가 `http://localhost:8080`에서 실행됩니다.

#### 서버 동작 확인
```bash
curl http://localhost:8080/api/health
```

### 4. 프론트엔드 설정 및 실행

#### NPM 의존성 설치
```bash
cd frontend
npm install
```

#### 개발 서버 실행
```bash
npm start
```

프론트엔드 서버가 `http://localhost:3000`에서 실행됩니다.

## 실행 확인

### 백엔드
- Health Check: http://localhost:8080/api/health
- API 문서: [docs/API.md](./API.md) 참조

### 프론트엔드
- 애플리케이션: http://localhost:3000
- 대시보드: http://localhost:3000/
- 개발 로그: http://localhost:3000/logs
- 프로젝트: http://localhost:3000/projects

## 빌드

### 백엔드 빌드
```bash
cd backend
mvn clean package
```

빌드된 JAR 파일은 `target/devlog-0.0.1-SNAPSHOT.jar`에 생성됩니다.

### 프론트엔드 빌드
```bash
cd frontend
npm run build
```

빌드된 파일은 `build/` 디렉토리에 생성됩니다.

## 문제 해결

### 데이터베이스 연결 오류
1. PostgreSQL이 실행 중인지 확인
2. 데이터베이스 이름, 사용자명, 비밀번호 확인
3. `application.yml` 설정 확인

### 포트 충돌
- 백엔드: `application.yml`에서 `server.port` 변경
- 프론트엔드: `.env` 파일에서 `PORT` 변경

### CORS 오류
`backend/src/main/java/com/vibecoding/devlog/config/WebConfig.java`에서 CORS 설정 확인

## 개발 모드

### 백엔드 Hot Reload
Spring Boot DevTools가 포함되어 있어 코드 변경 시 자동으로 재시작됩니다.

### 프론트엔드 Hot Reload
React 개발 서버는 자동으로 변경사항을 감지하고 새로고침합니다.

## 환경 변수

### 백엔드
`application.yml` 또는 환경 변수로 설정:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### 프론트엔드
`.env` 파일 생성:
```
REACT_APP_API_URL=http://localhost:8080/api
```

---

## 🚀 프로덕션 배포

프로덕션 환경에서는 다음을 권장합니다:

1. **Docker 컨테이너 배포**: [DOCKER.md](./DOCKER.md) 참조
2. **운영 가이드**: [OPERATIONS_MANUAL.md](./OPERATIONS_MANUAL.md) 참조
3. **배포 체크리스트**: [DEPLOYMENT_COMPLETE.md](../DEPLOYMENT_COMPLETE.md) 참조

### 프로덕션 환경 설정

**환경 변수 설정** (`.env` 또는 환경 변수)
```bash
# 백엔드
SPRING_DATASOURCE_URL=jdbc:postgresql://db.example.com:5432/devlog
SPRING_DATASOURCE_USERNAME=devlog
SPRING_DATASOURCE_PASSWORD=secure_password
SERVER_PORT=8080

# 프론트엔드
REACT_APP_API_URL=https://api.example.com
```

---

## 📚 추가 참고 자료

- **아키텍처**: [ARCHITECTURE.md](./ARCHITECTURE.md)
- **API 명세**: [API.md](./API.md) 또는 [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
- **UI/UX 가이드**: [UI_UX_GUIDE.md](./UI_UX_GUIDE.md)
- **Docker 가이드**: [DOCKER.md](./DOCKER.md)
- **운영 매뉴얼**: [OPERATIONS_MANUAL.md](./OPERATIONS_MANUAL.md)
- **개발 가이드**: [CLAUDE.md](../CLAUDE.md)
- **문서 네비게이션**: [README.md](./README.md)

---

*Last Updated: 2025-12-31*
