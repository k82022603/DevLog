# DevLog 아키텍처 문서

## 시스템 개요

DevLog는 개발자를 위한 일일 개발 로그 및 프로젝트 관리 시스템으로, 3-tier 아키텍처를 기반으로 설계되었습니다.

## 기술 스택

### 백엔드
- **Framework**: Spring Boot 3.2.1
- **Language**: Java 17
- **Persistence**: MyBatis 3.0.3
- **Database**: PostgreSQL 15
- **Build Tool**: Maven

### 프론트엔드
- **Framework**: React 18.2
- **Styling**: Tailwind CSS
- **Charts**: Recharts
- **HTTP Client**: Axios
- **Icons**: Lucide React
- **Routing**: React Router DOM

## 아키텍처 다이어그램

```
┌─────────────────────────────────────────────────────────┐
│                   프론트엔드 (React)                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐               │
│  │Dashboard │  │Dev Logs  │  │Projects  │               │
│  └──────────┘  └──────────┘  └──────────┘               │
│         │              │              │                  │
│         └──────────────┴──────────────┘                  │
│                    │                                     │
│              ┌─────┴─────┐                               │
│              │   Axios   │                               │
│              └───────────┘                               │
└───────────────────┬─────────────────────────────────────┘
                    │ HTTP/REST API
┌───────────────────┴─────────────────────────────────────┐
│               백엔드 (Spring Boot)                        │
│  ┌──────────────────────────────────────────────────┐   │
│  │              Controller Layer                     │   │
│  │  ┌──────────────┐  ┌─────────────────────────┐   │   │
│  │  │HealthCheck   │  │  DevLog/Project         │   │   │
│  │  │Controller    │  │  Controllers            │   │   │
│  │  └──────────────┘  └─────────────────────────┘   │   │
│  └────────────────────┬─────────────────────────────┘   │
│  ┌────────────────────┴─────────────────────────────┐   │
│  │              Service Layer                        │   │
│  │  ┌──────────────┐  ┌─────────────────────────┐   │   │
│  │  │  DevLog      │  │  Project                │   │   │
│  │  │  Service     │  │  Service                │   │   │
│  │  └──────────────┘  └─────────────────────────┘   │   │
│  └────────────────────┬─────────────────────────────┘   │
│  ┌────────────────────┴─────────────────────────────┐   │
│  │           Persistence Layer (MyBatis)             │   │
│  │  ┌──────────────┐  ┌─────────────────────────┐   │   │
│  │  │  DevLog      │  │  Project                │   │   │
│  │  │  Mapper      │  │  Mapper                 │   │   │
│  │  └──────────────┘  └─────────────────────────┘   │   │
│  └────────────────────┬─────────────────────────────┘   │
└───────────────────────┴─────────────────────────────────┘
                        │
┌───────────────────────┴─────────────────────────────────┐
│                 데이터베이스 (PostgreSQL)                 │
│  ┌──────────────┐  ┌─────────────────────────┐         │
│  │  dev_logs    │  │  projects               │         │
│  └──────────────┘  └─────────────────────────┘         │
└─────────────────────────────────────────────────────────┘
```

## 백엔드 구조

### 디렉토리 구조
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/vibecoding/devlog/
│   │   │   ├── DevLogApplication.java
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/
│   │   │   │   └── HealthCheckController.java
│   │   │   ├── domain/
│   │   │   │   ├── DevLog.java
│   │   │   │   └── Project.java
│   │   │   ├── mapper/
│   │   │   ├── service/
│   │   │   └── dto/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── mapper/
│   │           ├── DevLogMapper.xml
│   │           └── ProjectMapper.xml
│   └── test/
└── pom.xml
```

### 레이어 설명

#### Controller Layer
- REST API 엔드포인트 정의
- 요청/응답 처리
- 입력 검증

#### Service Layer
- 비즈니스 로직 구현
- 트랜잭션 관리
- 데이터 변환

#### Persistence Layer (MyBatis)
- 데이터베이스 쿼리 실행
- SQL 매핑
- 결과 매핑

#### Domain Layer
- 엔티티 클래스
- 비즈니스 객체

## 프론트엔드 구조

### 디렉토리 구조
```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── api/
│   │   └── axios.js
│   ├── components/
│   │   └── Layout.jsx
│   ├── pages/
│   │   ├── Dashboard.jsx
│   │   ├── DevLogs.jsx
│   │   └── Projects.jsx
│   ├── App.js
│   ├── index.js
│   └── index.css
├── package.json
└── tailwind.config.js
```

### 컴포넌트 구조

#### Layout
- 네비게이션
- 공통 레이아웃
- 라우팅 구조

#### Pages
- Dashboard: 통계 및 요약 정보
- DevLogs: 개발 로그 관리
- Projects: 프로젝트 관리

#### API
- Axios 인스턴스 설정
- 인터셉터 구성
- API 호출 로직

## 데이터베이스 설계

### ERD
```
┌─────────────────────┐         ┌─────────────────────┐
│     projects        │         │     dev_logs        │
├─────────────────────┤         ├─────────────────────┤
│ id (PK)             │────┐    │ id (PK)             │
│ name                │    │    │ project_id (FK)     │
│ description         │    └────│ title               │
│ status              │         │ content             │
│ start_date          │         │ tags                │
│ end_date            │         │ log_date            │
│ created_at          │         │ created_at          │
│ updated_at          │         │ updated_at          │
└─────────────────────┘         └─────────────────────┘
```

### 테이블 관계
- `projects` : `dev_logs` = 1 : N
- Foreign Key: `dev_logs.project_id` → `projects.id`
- ON DELETE CASCADE

## API 설계

### RESTful 원칙
- 리소스 기반 URL
- HTTP 메서드 활용 (GET, POST, PUT, DELETE)
- 상태 코드 활용
- JSON 데이터 형식

### 엔드포인트 패턴
```
GET    /api/logs         - 목록 조회
GET    /api/logs/{id}    - 상세 조회
POST   /api/logs         - 생성
PUT    /api/logs/{id}    - 수정
DELETE /api/logs/{id}    - 삭제
```

## 보안

### CORS 설정
- 프론트엔드 도메인 허용
- 허용 메서드 제한
- Credentials 지원

### 향후 보안 강화 계획
- JWT 기반 인증
- 사용자 권한 관리
- API Rate Limiting
- SQL Injection 방지 (MyBatis 사용)

## 성능 최적화

### 데이터베이스
- 인덱스 활용
- 쿼리 최적화
- 커넥션 풀링

### 프론트엔드
- 코드 스플리팅
- Lazy Loading
- 메모이제이션

## 확장성

### 수평 확장
- 무상태 API 설계
- 로드 밸런싱 가능

### 수직 확장
- 데이터베이스 스케일업
- 캐싱 레이어 추가 가능

## 모니터링 및 로깅

### 로깅
- SLF4J + Logback
- 로그 레벨별 분리
- 파일 및 콘솔 출력

### 향후 계획
- 애플리케이션 모니터링
- 성능 메트릭 수집
- 에러 트래킹

---

## 최근 아키텍처 업데이트 (v1.1.0)

### 2025-12-31 완성

#### 구현 완료
- **REST API**: 37개 엔드포인트 모두 구현
- **프론트엔드**: 모든 주요 페이지 및 컴포넌트 완성
  - Dashboard (통계 및 최근 활동)
  - DevLogs (목록, 작성, 편집, 상세보기)
  - Projects (목록, 작성, 편집)
  - Statistics (주간, 월간, 프로젝트별, 기술 스택)
  - Settings (UI 준비 완료, API 연동 대기)
- **데이터베이스**: PostgreSQL 16 기반 스키마 완성
- **배포**: Docker Compose 기반 컨테이너화 완료

#### 성능 최적화
- **프론트엔드**:
  - React Portal 패턴으로 렌더링 성능 향상
  - IntersectionObserver 기반 무한 스크롤
  - 브라우저 캐싱 최적화
  - 차트 로딩 속도 개선

- **백엔드**:
  - MyBatis 쿼리 최적화 (명시적 타입 캐스팅)
  - PostgreSQL 인덱싱 최적화
  - 통계 쿼리 성능 개선

#### 안정성 강화
- **6가지 주요 버그 수정** (Critical 2건):
  1. 통계 데이터 0으로 표시
  2. Select 요소 가시성
  3. 차트 툴팁 가시성
  4. DateTime 파싱 에러
  5. 달력 Z-index 이슈
  6. 달력 텍스트 가시성

#### 향후 확장 계획

**v1.2.0 (Q1 2026)**
- 사용자 인증 시스템 (JWT)
- 멀티테넌트 지원
- 알림 기능 (이메일, 인앱)

**v2.0.0 (Q2 2026)**
- 팀 협업 기능
- 데이터 공유 및 권한 관리
- 고급 검색 (전문 검색)
- 데이터 내보내기 (PDF, CSV)

**v3.0.0 (Q3 2026)**
- 모바일 앱 (React Native)
- 오프라인 지원
- AI 기반 분석 및 추천
- 통합 (Slack, GitHub, Jira 등)

---

## 배포 아키텍처

### 개발 환경
```
개발자 PC
├── Backend (Spring Boot, Maven)
├── Frontend (React, npm)
└── Database (PostgreSQL, docker-compose)
```

### 프로덕션 환경
```
클라우드 서버 (AWS/Azure/GCP)
├── Docker Registry
├── Kubernetes Cluster (향후)
│   ├── Backend Pods
│   ├── Frontend Pods
│   └── Database StatefulSet
└── 로드 밸런서
    └── CDN (정적 파일)
```

---

## 참고 자료

### 기술 문서
- [SETUP.md](./SETUP.md) - 설치 가이드
- [DOCKER.md](./DOCKER.md) - Docker 가이드
- [OPERATIONS_MANUAL.md](./OPERATIONS_MANUAL.md) - 운영 매뉴얼
- [API.md](./API.md) / [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) - API 명세
- [UI_UX_GUIDE.md](./UI_UX_GUIDE.md) - UI/UX 가이드
- [README.md](./README.md) - 문서 네비게이션

### 공식 문서
- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://react.dev/)
- [PostgreSQL](https://www.postgresql.org/docs/)
- [MyBatis](https://mybatis.org/)
- [Tailwind CSS](https://tailwindcss.com/)
- [Docker](https://docs.docker.com/)

---

*Last Updated: 2025-12-31*
*DevLog Architecture v1.1.0*
