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

## 🏗️ 아키텍처 설계 지침서 (비전문가를 위한 완전 가이드)

이 섹션은 DevOps나 아키텍처 경험이 없는 개발자들도 효과적인 시스템 아키텍처를 설계하고 CI/CD를 구축할 수 있도록 하는 실용적인 가이드입니다.

---

### 1️⃣ 아키텍처 설계의 기초 이해

#### 아키텍처란 무엇인가?

아키텍처는 **건축물의 설계도**와 같습니다. 건물을 지을 때 먼저 설계도를 그리고, 그 기반 위에 실제 건물을 짓듯이, 소프트웨어도 코드를 작성하기 전에 전체 구조를 정의해야 합니다.

**좋은 아키텍처의 특징:**
- **명확성 (Clarity)**: 누가 봐도 시스템 구조를 이해할 수 있음
- **확장성 (Scalability)**: 비즈니스 성장에 따라 쉽게 확장 가능
- **유지보수성 (Maintainability)**: 버그 수정과 기능 추가가 용이
- **신뢰성 (Reliability)**: 장애 시에도 서비스가 계속 운영되도록 설계
- **성능 (Performance)**: 사용자 경험을 해치지 않는 응답 속도

#### 아키텍처 설계의 세 가지 핵심 질문

아키텍처를 설계하기 전에 반드시 다음 세 질문에 답해야 합니다:

1. **"우리 시스템은 무엇을 하는가?"**
   - 핵심 기능이 무엇인지 명확히 정의
   - 예: DevLog는 개발자의 로그를 기록하고 통계를 제공한다

2. **"어떤 데이터가 얼마나 중요한가?"**
   - 데이터 손실 시 미치는 영향도 파악
   - 예: 사용자의 로그 데이터는 매우 중요하므로 정기적 백업 필수

3. **"시스템이 얼마나 빨라야 하는가?"**
   - 응답 시간과 처리량 요구사항 정의
   - 예: 로그 조회는 1초 이내, 통계 생성은 5초 이내

---

### 2️⃣ 계층별 설계 원칙 (Layer-by-Layer Design)

모든 시스템은 다음과 같은 계층으로 나뉩니다. 각 계층을 독립적으로 설계하면 훨씬 쉬워집니다.

#### A. 프론트엔드 계층 (사용자가 보는 부분)

**설계 원칙:**
- **단순함**: 사용자가 쉽게 이해할 수 있는 인터페이스
- **반응성**: 클릭 후 즉각적인 피드백
- **일관성**: 모든 페이지에서 동일한 디자인 언어 사용

**DevLog 예시:**
```
프론트엔드 설계 결정:
├── UI 프레임워크 선택 → React (많은 개발자가 사용하므로 학습 리소스 풍부)
├── 상태 관리 전략 → Hooks + Context API (간단함)
├── 스타일링 → Tailwind CSS (빠른 개발)
└── 차트 라이브러리 → Recharts (사용 용이)
```

**체크리스트:**
- [ ] 모든 주요 페이지 목록화 (대시보드, 목록, 상세보기 등)
- [ ] 각 페이지의 핵심 UI 컴포넌트 정의
- [ ] 반응형 디자인 고려 (모바일, 태블릿, 데스크톱)
- [ ] 로딩 상태와 에러 상태 처리 방안 수립

#### B. API 계층 (프론트엔드와 백엔드를 연결하는 다리)

**설계 원칙:**
- **명확한 계약**: API의 입출력이 명확하게 정의되어야 함
- **버전 관리**: 기존 클라이언트를 깨뜨리지 않으면서 개선
- **일관성**: 모든 API가 같은 패턴을 따름

**DevLog 예시:**
```json
// 일관된 응답 형식
{
  "success": true,
  "data": { /* 실제 데이터 */ },
  "error": null,
  "timestamp": "2025-01-20T10:00:00Z"
}

// 일관된 에러 응답
{
  "success": false,
  "data": null,
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "요청한 리소스를 찾을 수 없습니다"
  }
}
```

**체크리스트:**
- [ ] 필요한 모든 엔드포인트 목록화
- [ ] 각 엔드포인트의 요청/응답 형식 정의
- [ ] 에러 케이스와 HTTP 상태 코드 정의
- [ ] 인증/인가 메커니즘 결정

#### C. 백엔드 계층 (실제 비즈니스 로직)

**설계 원칙:**
- **관심사의 분리 (Separation of Concerns)**: 각 클래스는 하나의 책임만 가짐
- **테스트 가능성**: 각 컴포넌트를 독립적으로 테스트 가능해야 함
- **성능**: 데이터베이스 쿼리 최적화

**DevLog 예시 - 3계층 구조:**
```
Controller (HTTP 처리)
    ↓
Service (비즈니스 로직)
    ↓
Mapper (데이터베이스 접근)
    ↓
Database (데이터 저장)
```

**각 계층의 역할:**
- **Controller**: HTTP 요청을 받아 Service 호출 (로직 없음)
- **Service**: 실제 비즈니스 로직 구현 (트랜잭션 관리)
- **Mapper**: SQL 실행 (데이터 조작만)

**체크리스트:**
- [ ] 도메인 모델 정의 (프로젝트, 로그, 태그 등)
- [ ] 각 엔드포인트의 로직 플로우 작성
- [ ] 데이터 유효성 검증 방안 수립
- [ ] 성능이 중요한 쿼리 식별 및 최적화 계획

#### D. 데이터베이스 계층 (데이터 저장소)

**설계 원칙:**
- **정규화 (Normalization)**: 데이터 중복 제거
- **인덱싱**: 자주 검색되는 컬럼에 인덱스 추가
- **제약 조건**: 데이터 무결성 보장

**DevLog 예시:**
```sql
-- 테이블 관계
projects (프로젝트)
    ↓ (1:N)
dev_logs (개발 로그)
    ↓ (N:N)
tech_tags (기술 태그)
```

**체크리스트:**
- [ ] 필요한 모든 테이블 정의
- [ ] 테이블 간 관계 매핑
- [ ] 주요 컬럼에 인덱스 추가 계획
- [ ] 백업/복구 전략 수립

---

### 3️⃣ CI/CD 아키텍처의 역할과 중요성

CI/CD는 **자동화된 품질 보증 시스템**입니다. 이를 통해 개발자들은 안심하고 코드를 작성할 수 있습니다.

#### CI/CD가 하는 일 (자동화)

```
개발자가 코드 작성
    ↓
GitHub에 Push
    ↓ (자동 실행)
1. 코드 컴파일
2. 자동 테스트 실행
3. 코드 품질 검사
4. 보안 취약점 검사
5. 성공하면 배포 (또는 실패 시 알림)
```

#### DevLog의 CI/CD 설계

**현재 상태 (수동):**
```
개발 → Git Commit → 수동으로 Docker Build → 수동으로 배포
```

**개선된 상태 (자동화):**
```
개발 → Git Commit → GitHub Actions 자동 실행
    ├── Step 1: 코드 컴파일 및 테스트
    ├── Step 2: Docker 이미지 빌드
    ├── Step 3: 이미지 푸시 (Docker Registry)
    └── Step 4: 자동 배포 (AWS/Azure/GCP)
```

#### CI/CD 파이프라인 구성 요소

**1. Source Control (Git)**
- 역할: 코드 버전 관리
- 예: GitHub에 main, develop, feature 브랜치

**2. Build Stage (컴파일)**
```bash
# 백엔드
mvn clean package

# 프론트엔드
npm run build
```
- 역할: 코드를 실행 가능한 형태로 변환
- 실패하면 여기서 멈춤

**3. Test Stage (자동 테스트)**
```bash
# 백엔드 테스트
mvn test

# 프론트엔드 테스트
npm test
```
- 역할: 코드의 정확성 검증
- 테스트 실패 시 배포 불가

**4. Build Image Stage (Docker 이미지 생성)**
```bash
docker build -t devlog:latest .
docker push docker.io/username/devlog:latest
```
- 역할: 컨테이너 이미지 생성
- 이미지 저장소(Docker Registry)에 저장

**5. Deploy Stage (배포)**
```bash
# 프로덕션 환경에 배포
docker pull docker.io/username/devlog:latest
docker-compose up -d
```
- 역할: 최신 버전을 실제 서버에 반영
- 이 단계 이전에 모든 검증 완료

---

### 4️⃣ CI/CD 배포 전략

#### 배포 환경의 단계

```
Local (개발자 PC)
    ↓ (모든 개발 완료)
Development (공유 개발 서버)
    ↓ (팀 검증 완료)
Staging (프로덕션과 동일한 환경)
    ↓ (최종 테스트 완료)
Production (실제 사용자 서비스)
```

#### 각 환경의 역할

| 환경 | 목적 | 누가 사용 | 데이터 | 비용 |
|------|------|---------|--------|------|
| Local | 개발 및 디버깅 | 개발자 | 테스트 데이터 | 무료 (개발자 PC) |
| Development | 통합 테스트 | 개발팀 | 테스트 데이터 | 낮음 |
| Staging | 최종 검증 | QA, PM | 실제와 유사한 데이터 | 중간 |
| Production | 실제 서비스 | 모든 사용자 | 실제 사용자 데이터 | 높음 |

**DevLog 예시:**
```
Local: docker-compose up -d (로컬 개발)
    ↓
Dev: dev.devlog.example.com (테스트)
    ↓
Staging: staging.devlog.example.com (최종 검증)
    ↓
Production: devlog.example.com (실제 서비스)
```

#### 배포 전략 선택

**1. Blue-Green Deployment (권장)**
```
현재 버전 (Blue)  ← 사용자 트래픽
새 버전 (Green)   ← 테스트 중

테스트 완료 후 트래픽 전환
→ 즉시 롤백 가능
```

**2. Rolling Deployment**
```
서버 1: 구버전 (사용자 서빙)
    ↓ (신버전으로 업데이트)
서버 1: 신버전 (사용자 서빙)
    ↓ (확인 후 다음 서버 업데이트)
서버 2: 구버전 → 신버전
...
```

**3. Canary Deployment (위험 최소화)**
```
신버전을 5%의 사용자에게만 배포
→ 모니터링
→ 문제 없으면 100%로 확대
```

---

### 5️⃣ 확장성을 고려한 설계

시스템이 성장할 때를 대비해야 합니다.

#### 수평 확장 vs 수직 확장

**수직 확장 (CPU/RAM 증가)**
```
과거: 1개 서버, 4GB RAM
    ↓
현재: 1개 서버, 32GB RAM
```
- 장점: 초기 단계에서 비용 효율적
- 단점: 한계가 있고 서비스 중단 필요

**수평 확장 (서버 증가)**
```
과거: 1개 서버 처리
    ↓
현재: 5개 서버가 부하 분산
    ↓ (로드 밸런서가 요청 분산)
```
- 장점: 무한 확장 가능
- 단점: 초기 설계가 복잡함

#### 확장성을 위한 아키텍처 설계

**1. 무상태 (Stateless) 설계**
```
❌ 나쁜 설계
서버 A: 사용자1의 세션 정보 저장
서버 B: 사용자1 요청 처리 (세션 없음!)

✅ 좋은 설계
Redis: 모든 서버가 접근 가능한 세션 저장소
서버 A, B, C: 세션 정보 요청 (Redis에서 조회)
```

**2. 데이터베이스 최적화**
```
수직 확장: 쿼리 최적화 (인덱스, 캐싱)
수평 확장: 읽기 복제본 추가 (replication)
최종 단계: 데이터베이스 분할 (sharding)
```

**3. 캐싱 전략**
```
요청 순서:
1. 메모리 캐시 확인 (Redis) - 0.1ms
   ↓ (없으면)
2. 데이터베이스 조회 - 10ms
3. 결과를 캐시에 저장
```

---

### 6️⃣ 보안을 고려한 설계

보안은 나중에 추가하는 것이 아니라 처음부터 설계에 포함해야 합니다.

#### 계층별 보안 설계

**1. 네트워크 계층**
```
사용자
    ↓ (HTTPS/TLS 암호화)
로드 밸런서 (DDoS 방어)
    ↓ (VPC 내부 통신)
백엔드 서버들
    ↓ (VPC 내부 통신)
데이터베이스
```

**2. 애플리케이션 계층**
```
✅ 입력 검증: 사용자 입력을 절대 신뢰하지 않기
   - SQL Injection 방지
   - XSS (Cross-Site Scripting) 방지

✅ 인증: 사용자가 본인이 맞는지 확인
   - JWT 토큰 사용
   - 비밀번호는 해시 처리

✅ 인가: 인증된 사용자가 리소스에 접근 권한이 있는지 확인
   - 역할 기반 접근 제어 (RBAC)
```

**3. 데이터 계층**
```
✅ 암호화: 민감한 데이터 암호화 저장
✅ 접근 제어: 데이터베이스 사용자별 권한 제한
✅ 백업: 정기적 백업 및 복구 테스트
```

#### DevLog 보안 설계 (향후 계획)

```
Phase 1 (현재):
- 단일 사용자, 인증 없음

Phase 2 (v1.2.0):
- JWT 기반 사용자 인증
- 사용자별 데이터 격리

Phase 3 (v2.0.0):
- 팀 기반 권한 관리
- 감사 로그 추가
```

---

### 7️⃣ 모니터링과 로깅

프로덕션 시스템에서는 "무슨 일이 일어나고 있는지" 항상 알아야 합니다.

#### 모니터링 전략

**1. 애플리케이션 메트릭**
```
수집 항목:
- API 응답 시간 (평균, p99)
- 요청 처리량 (RPS: Requests Per Second)
- 에러율 (5xx 응답 비율)
- 데이터베이스 쿼리 시간

추적 기준:
- 정상: 응답 시간 < 200ms, 에러율 < 0.1%
- 경고: 응답 시간 > 500ms, 에러율 > 1%
- 심각: 응답 시간 > 2s, 에러율 > 5%
```

**2. 인프라 메트릭**
```
수집 항목:
- CPU 사용률
- 메모리 사용률
- 디스크 사용률
- 네트워크 대역폭
```

**3. 로깅 전략**
```
로그 레벨:
- DEBUG: 개발 중 상세한 정보
- INFO: 중요한 이벤트 (로그인, 요청 성공)
- WARN: 경고 (deprecated API 사용)
- ERROR: 에러 (예외 발생)
- FATAL: 심각한 오류 (서비스 중단)

예시:
INFO: User 123 logged in from IP 192.168.1.1
ERROR: Database connection failed after 3 retries
FATAL: Out of memory, service stopping
```

#### DevLog 모니터링 스택

```
애플리케이션 (Logs)
    ↓
로그 집계 (ELK Stack)
    ├── Elasticsearch (검색)
    ├── Logstash (처리)
    └── Kibana (시각화)
    ↓
메트릭 수집 (Prometheus)
    ↓
시각화 및 알림 (Grafana)
    ↓
알림 (PagerDuty, Slack)
```

---

### 8️⃣ Step-by-Step 아키텍처 설계 가이드

비전문가도 따라 할 수 있는 단계별 가이드입니다.

#### Step 1: 요구사항 분석 (1-2시간)

```
질문 목록:
1. 핵심 기능은 무엇인가? (3-5개)
   → DevLog: 로그 기록, 프로젝트 관리, 통계

2. 사용자는 누구인가?
   → DevLog: 개발자 (초기 1명, 향후 팀)

3. 예상 사용자 수와 데이터량은?
   → DevLog: 월 1000만 요청, 1GB 데이터

4. 가용성 요구사항은?
   → DevLog: 일반 서비스 (99.5% uptime)

5. 성능 요구사항은?
   → DevLog: API 응답 < 200ms, 페이지 로딩 < 2s
```

#### Step 2: 기술 스택 선택 (1-2시간)

```
선택 기준:
1. 팀의 숙련도
   → 팀이 Java를 잘 알면 Spring Boot 선택

2. 커뮤니티 크기
   → 큰 커뮤니티 = 문제 해결 쉬움

3. 성숙도
   → 새로운 기술보다는 검증된 기술 선택

DevLog 선택:
Frontend: React (많은 개발자, 취업 시 유리)
Backend: Spring Boot (Java 생태계 풍부)
Database: PostgreSQL (오픈소스, 확장성)
Container: Docker (표준 도구)
```

#### Step 3: 데이터 모델 설계 (1-3시간)

```
1. 핵심 엔티티 식별
   - Project (프로젝트)
   - DevLog (개발 로그)
   - Tag (태그)

2. 엔티티 간 관계 정의
   - Project 1:N DevLog
   - DevLog N:N Tag

3. 필드 정의
   Project:
   - id (PK)
   - name
   - description
   - status (ACTIVE, COMPLETED, etc)
   - created_at
   - updated_at
```

#### Step 4: API 설계 (1-2시간)

```
1. 엔드포인트 목록화
   GET /projects
   POST /projects
   GET /projects/{id}
   PUT /projects/{id}
   DELETE /projects/{id}
   ...

2. 요청/응답 형식 정의
   GET /projects/{id}
   Request: (없음)
   Response: {
     "id": 1,
     "name": "DevLog",
     "status": "ACTIVE"
   }

3. 에러 처리 정의
   404: 리소스 없음
   400: 잘못된 요청
   500: 서버 에러
```

#### Step 5: 배포 전략 설계 (1-2시간)

```
1. 환경 정의
   - Local: Docker Compose
   - Dev: AWS EC2
   - Staging: AWS EC2
   - Prod: AWS ECS

2. CI/CD 파이프라인 설계
   Git Push → GitHub Actions → 자동 테스트 → 자동 배포

3. 모니터링 계획
   - CloudWatch (AWS)
   - DataDog (애플리케이션)
```

#### Step 6: 보안 설계 (1-2시간)

```
1. 인증 방식 선택
   → JWT 토큰 기반

2. 통신 암호화
   → HTTPS/TLS

3. 데이터 보호
   → 비밀번호 해싱 (bcrypt)
   → 민감정보 암호화
```

---

### 9️⃣ DevLog 사례 분석: 실제 설계 결정

이 섹션은 DevLog 프로젝트에서 실제로 어떤 설계 결정이 이루어졌는지 보여줍니다.

#### 왜 Spring Boot를 선택했나?

```
고려 옵션:
1. Node.js (Express)
2. Python (Django, FastAPI)
3. Java (Spring Boot) ← 선택

선택 이유:
✅ 강력한 타입 시스템 (런타임 에러 감소)
✅ 성숙한 생태계 (라이브러리 풍부)
✅ 높은 성능 (Java의 JIT 컴파일)
✅ 취업 시장에서 인기
❌ 초기 학습 곡선 가파름 (하지만 극복 가능)
```

#### 왜 React를 선택했나?

```
고려 옵션:
1. Vue.js
2. Angular
3. React ← 선택

선택 이유:
✅ 가장 큰 커뮤니티
✅ 취업 시 높은 수요
✅ 컴포넌트 기반 (학습 용이)
✅ JSX (직관적 템플릿)
```

#### 왜 PostgreSQL을 선택했나?

```
고려 옵션:
1. MySQL
2. MongoDB
3. PostgreSQL ← 선택

선택 이유:
✅ 오픈소스 (비용 무료)
✅ 강력한 데이터 무결성
✅ 복잡한 쿼리 지원
✅ 수평 확장 가능 (하지만 복잡함)
```

#### 실제 구현의 문제점과 해결

```
문제 1: PostgreSQL 컬럼명 소문자 이슈
원인: PostgreSQL은 컬럼명을 자동으로 소문자로 변환
해결: MyBatis 설정 업데이트 (map-underscore-to-camel-case)

문제 2: Select 요소 색상 가시성
원인: 배경색과 텍스트색이 구별되지 않음
해결: Primary 색상(RGB 97,61,132)으로 통일

문제 3: 달력이 다른 요소 뒤에 숨김
원인: Z-index 문제
해결: React Portal 패턴 사용 (document.body에 렌더링)
```

---

### 🔟 일반적인 실수와 해결책

비전문가들이 자주 하는 실수들입니다.

#### 실수 1: "나중에 확장성을 고려하겠다"

```
❌ 나쁜 접근
1. 빠르게 작성
2. 나중에 리팩토링

✅ 좋은 접근
1. 처음부터 확장성 고려 (큰 비용 아님)
2. 빠른 변화에도 대응 가능

DevLog 예시:
- Service 계층으로 분리 → 테스트 용이
- Repository 패턴 사용 → DB 변경 쉬움
```

#### 실수 2: "모든 기능을 한 번에 구현"

```
❌ 나쁜 접근
Day 1: 모든 기능 + 완벽한 UI + 배포까지
→ 불가능, 번아웃

✅ 좋은 접근 (MVP)
Week 1: 핵심 기능만 (로그 기록)
Week 2: 프로젝트 관리 추가
Week 3: 통계 기능 추가
Week 4: 배포 및 개선

DevLog 진행:
v0.1: 로그 CRUD
v0.5: 프로젝트 관리
v1.0: 통계, 검색
v1.1: 버그 수정, 문서화
```

#### 실수 3: "코드 먼저, 문서는 나중에"

```
❌ 나쁜 접근
코드 작성 → 3개월 후 문서 작성
→ 코드가 변했는데 문서는 구버전

✅ 좋은 접근
코드 작성 → 즉시 문서화
또는
문서 먼저 작성 → 코드 작성 (TDD)

DevLog의 선택:
CLAUDE.md를 먼저 작성
→ 개발 가이드 제공
→ AI 에이전트가 가이드를 따라 구현
→ 코드와 문서 항상 일치
```

#### 실수 4: "테스트는 시간 낭비"

```
❌ 나쁜 접근
테스트 없음 → 버그 → 밤새 디버깅 → 더 많은 버그
→ 결국 테스트에 쓰는 것보다 더 많은 시간 낭비

✅ 좋은 접근 (1줄의 코드마다 3줄의 테스트)
- 초기: 느림
- 장기: 매우 빠름 (자신감 있게 리팩토링 가능)

DevLog의 선택:
MANUAL-TEST-SCENARIOS.md 작성
→ 각 기능별 테스트 케이스 명시
→ 버그 조기 발견
```

#### 실수 5: "프로덕션 환경은 나중에 생각"

```
❌ 나쁜 접근
로컬에서 완벽하게 동작 → 프로덕션 배포 → 바로 오류
원인: 환경 차이

✅ 좋은 접근
처음부터 Docker 사용
→ 로컬 = 개발 = 프로덕션 환경 동일

DevLog의 선택:
처음부터 Docker Compose 사용
→ 로컬에서 docker-compose up -d
→ 프로덕션도 같은 명령어
```

---

### 🎯 아키텍처 설계 체크리스트

프로젝트를 시작하기 전에 다음을 확인하세요:

#### 요구사항 분석
- [ ] 핵심 기능 3-5개 정의
- [ ] 예상 사용자 수 예측
- [ ] 성능 요구사항 명시
- [ ] 가용성 목표 설정

#### 기술 스택
- [ ] 프론트엔드 프레임워크 선택
- [ ] 백엔드 프레임워크 선택
- [ ] 데이터베이스 선택
- [ ] 배포 플랫폼 선택

#### 설계
- [ ] 데이터 모델 (ERD) 작성
- [ ] API 엔드포인트 목록화
- [ ] 시스템 아키텍처 다이어그램 작성
- [ ] 배포 파이프라인 설계

#### CI/CD
- [ ] 자동화된 테스트 계획
- [ ] 빌드 프로세스 정의
- [ ] 배포 환경 구성 (Local, Dev, Staging, Prod)
- [ ] 모니터링 계획

#### 보안
- [ ] 인증 방식 결정
- [ ] 암호화 전략 수립
- [ ] 접근 제어 정책 정의
- [ ] 감사 로그 계획

#### 문서화
- [ ] CLAUDE.md (개발 가이드) 작성
- [ ] ARCHITECTURE.md (시스템 설계) 작성
- [ ] API.md (API 명세) 작성
- [ ] SETUP.md (설치 가이드) 작성

---

### 💡 핵심 교훈

아키텍처 설계의 본질은 **미래를 대비하는 것**입니다.

```
좋은 아키텍처의 세 가지 특징:

1. 현재의 문제를 해결한다
   → 핵심 기능이 빠르게 동작

2. 미래의 변화를 수용한다
   → 새로운 요구사항이 쉽게 추가됨

3. 팀의 협업을 용이하게 한다
   → 누구나 코드를 이해하고 수정 가능
```

**DevLog의 성공 이유:**

```
1. 명확한 설계 (ARCHITECTURE.md, CLAUDE.md)
2. 실행 중심 (하나씩 기능 완성)
3. 지속적 개선 (버그 수정, 최적화)
4. 완벽한 문서 (10,000줄 이상)
= 프로덕션 레벨의 애플리케이션
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
