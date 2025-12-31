# DevLog 문서 가이드

> 이 문서는 DevLog 프로젝트의 모든 기술 문서를 정리한 네비게이션 가이드입니다.

---

## 📚 문서 목차

### 🚀 빠른 시작
- **새로운 사용자**: [SETUP.md](./SETUP.md) 또는 [Docker 실행](#docker를-사용한-빠른-시작)
- **개발자**: [ARCHITECTURE.md](./ARCHITECTURE.md) → [UI_UX_GUIDE.md](./UI_UX_GUIDE.md)
- **운영자**: [OPERATIONS_MANUAL.md](./OPERATIONS_MANUAL.md) → [DOCKER.md](./DOCKER.md)
- **API 사용자**: [API.md](./API.md) 또는 [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

### 📖 전체 문서 목록

#### 1. **SETUP.md** - 설치 및 실행 가이드
```
대상: 개발자, 운영자
용도: 프로젝트 초기 설정 및 실행 방법
내용:
  • 사전 요구사항 (Java, Node.js, PostgreSQL, Maven)
  • 데이터베이스 설정 (PostgreSQL)
  • 백엔드 설정 및 실행 (Maven)
  • 프론트엔드 설정 및 실행 (npm)
  • 전체 시스템 동작 확인
```
👉 [SETUP.md 보기](./SETUP.md)

#### 2. **ARCHITECTURE.md** - 시스템 아키텍처
```
대상: 개발자, 아키텍트
용도: 전체 시스템 설계 및 구조 이해
내용:
  • 기술 스택 상세 (Spring Boot, React, PostgreSQL)
  • 아키텍처 다이어그램 (3-tier 구조)
  • 백엔드 구조 및 계층 설명
  • 프론트엔드 구조 및 컴포넌트 관계
  • 데이터베이스 스키마 개요
  • 통신 흐름 및 API 연동
```
👉 [ARCHITECTURE.md 보기](./ARCHITECTURE.md)

#### 3. **UI_UX_GUIDE.md** - UI/UX 개발자 가이드
```
대상: 프론트엔드 개발자, 디자이너
용도: 프론트엔드 개발 시 준수할 가이드라인
내용:
  • 디자인 철학 (Glassmorphism, 다크 테마)
  • 색상 시스템 (Primary: RGB 97,61,132)
  • 타이포그래피 및 아이콘
  • 컴포넌트 가이드 (버튼, 카드, 입력 필드 등)
  • 레이아웃 및 그리드 시스템
  • 반응형 디자인 규칙
  • 애니메이션 및 트랜지션
  • 접근성 (Accessibility) 가이드
  • 개발 워크플로우
  • 최근 UI 개선 사항 및 버그 수정
```
👉 [UI_UX_GUIDE.md 보기](./UI_UX_GUIDE.md)

#### 4. **API.md** - API 명세서
```
대상: 프론트엔드 개발자, API 사용자
용도: REST API 엔드포인트 상세 명세
내용:
  • Health Check API
  • Projects API (CRUD)
  • DevLogs API (CRUD)
  • Statistics API
  • 요청/응답 형식
  • 에러 처리
```
👉 [API.md 보기](./API.md)

#### 5. **API_DOCUMENTATION.md** - API 상세 문서
```
대상: 백엔드 개발자, API 설계자
용도: API 상세 구현 문서
내용:
  • API 엔드포인트 목록 (37개)
  • 각 엔드포인트 상세 설명
  • 요청 및 응답 예제
  • 데이터 모델 정의
  • 에러 코드 및 처리
```
👉 [API_DOCUMENTATION.md 보기](./API_DOCUMENTATION.md)

#### 6. **DOCKER.md** - Docker 환경 설정
```
대상: 운영자, DevOps 엔지니어
용도: Docker 및 Docker Compose 사용 가이드
내용:
  • Docker Compose 구성 요소 (PostgreSQL, pgAdmin)
  • 서비스 시작/중지 명령어
  • 로그 확인 및 디버깅
  • 데이터베이스 초기화
  • 볼륨 관리
```
👉 [DOCKER.md 보기](./DOCKER.md)

#### 7. **OPERATIONS_MANUAL.md** - 운영자 매뉴얼
```
대상: 시스템 관리자, 운영자
용도: 프로덕션 시스템 운영 및 유지보수
내용:
  • 시스템 요구사항 (하드웨어, 소프트웨어)
  • 초기 설치 및 설정
  • 일상 운영 (서비스 시작/중지, 상태 확인)
  • 모니터링 (로그, 성능 지표)
  • 백업 및 복구 절차
  • 성능 최적화
  • 보안 관리
  • 문제 해결
  • 업그레이드 및 유지보수
```
👉 [OPERATIONS_MANUAL.md 보기](./OPERATIONS_MANUAL.md)

#### 8. **MANUAL-TEST-SCENARIOS.md** - 테스트 시나리오
```
대상: QA 엔지니어, 테스터
용도: 수동 테스트 시나리오 및 체크리스트
내용:
  • 각 페이지별 테스트 시나리오
  • 기능별 테스트 케이스
  • 엣지 케이스 및 에러 처리 테스트
```
👉 [MANUAL-TEST-SCENARIOS.md 보기](./MANUAL-TEST-SCENARIOS.md)

---

## 🔗 관련 문서 (루트 디렉토리)

### 프로젝트 문서
- **[README.md](../README.md)** - 프로젝트 개요, 주요 기능, 기능명세
- **[CLAUDE.md](../CLAUDE.md)** - Claude AI를 위한 개발 가이드 및 컨벤션
- **[LICENSE](../LICENSE)** - MIT License

### GitHub 문서
- **[GITHUB_GUIDE.md](../GITHUB_GUIDE.md)** - GitHub 저장소 사용 가이드
- **[GITHUB_PUSH_INSTRUCTIONS.md](../GITHUB_PUSH_INSTRUCTIONS.md)** - GitHub Push 단계별 지침
- **[DEPLOYMENT_COMPLETE.md](../DEPLOYMENT_COMPLETE.md)** - 배포 완료 보고서
- **[GITHUB_COMPLETE_GUIDE.md](../GITHUB_COMPLETE_GUIDE.md)** - GitHub 통합 가이드

### 프로젝트 회고
- **[PROJECT_RETROSPECTIVE.md](../PROJECT_RETROSPECTIVE.md)** - 프로젝트 회고 및 분석

---

## 🐳 Docker를 사용한 빠른 시작

### 최소 요구사항
- Docker 20.10+
- Docker Compose 2.0+

### 빠른 실행 (3단계)

```bash
# 1. 저장소 클론
git clone https://github.com/k82022603/DevLog.git
cd DevLog

# 2. Docker Compose로 백엔드 빌드 및 데이터베이스 시작
docker-compose up -d

# 3. 프론트엔드 실행 (별도 터미널)
cd frontend
npm install
npm start
```

**접속 정보**
- 프론트엔드: http://localhost:3000
- 백엔드 API: http://localhost:8080/api
- pgAdmin (DB 관리): http://localhost:5050
  - 이메일: admin@devlog.com
  - 비밀번호: admin123

자세한 내용은 [DOCKER.md](./DOCKER.md)를 참조하세요.

---

## 👥 역할별 추천 문서

### 🎯 프론트엔드 개발자
1. [ARCHITECTURE.md](./ARCHITECTURE.md) - 전체 시스템 이해
2. [API.md](./API.md) - 백엔드 API 이해
3. [UI_UX_GUIDE.md](./UI_UX_GUIDE.md) - UI/UX 개발 가이드
4. [SETUP.md](./SETUP.md) - 개발 환경 설정
5. [MANUAL-TEST-SCENARIOS.md](./MANUAL-TEST-SCENARIOS.md) - 테스트 시나리오

### 🎯 백엔드 개발자
1. [ARCHITECTURE.md](./ARCHITECTURE.md) - 전체 시스템 이해
2. [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) - API 상세 명세
3. [CLAUDE.md](../CLAUDE.md) - 개발 가이드 및 컨벤션
4. [SETUP.md](./SETUP.md) - 개발 환경 설정
5. [MANUAL-TEST-SCENARIOS.md](./MANUAL-TEST-SCENARIOS.md) - 테스트 시나리오

### 🎯 DevOps / 시스템 관리자
1. [DOCKER.md](./DOCKER.md) - Docker 환경 설정
2. [OPERATIONS_MANUAL.md](./OPERATIONS_MANUAL.md) - 운영 및 유지보수
3. [SETUP.md](./SETUP.md) - 초기 설정 (전체)
4. [ARCHITECTURE.md](./ARCHITECTURE.md) - 시스템 구조 이해

### 🎯 제품 관리자 / 기획자
1. [README.md](../README.md) - 프로젝트 개요 및 기능명세
2. [MANUAL-TEST-SCENARIOS.md](./MANUAL-TEST-SCENARIOS.md) - 기능 테스트
3. [PROJECT_RETROSPECTIVE.md](../PROJECT_RETROSPECTIVE.md) - 프로젝트 분석

### 🎯 새로운 팀원
1. [README.md](../README.md) - 프로젝트 이해
2. [SETUP.md](./SETUP.md) - 개발 환경 구성
3. [ARCHITECTURE.md](./ARCHITECTURE.md) - 시스템 설계
4. [CLAUDE.md](../CLAUDE.md) - 개발 규칙 및 컨벤션

---

## 📊 최근 업데이트 내역

### v1.1.0 (2025-12-31)

#### 문서 업데이트
- `docs/README.md` 새로 작성 (네비게이션 가이드)
- `docs/UI_UX_GUIDE.md` 최근 버그 수정 사항 추가
  - 대시보드 통계 0으로 표시되는 문제 해결
  - 차트 툴팁 가시성 개선
  - 달력 컴포넌트 Portal 패턴 적용
  - 모든 select 요소 색상 일관성 개선

#### 주요 개선사항
- Docker 기반 배포 시스템 완성
- 37개 REST API 구현 완료
- PostgreSQL 16으로 업그레이드
- 프론트엔드 성능 최적화
- 접근성 개선

자세한 내용은 [PROJECT_RETROSPECTIVE.md](../PROJECT_RETROSPECTIVE.md) 참조

---

## 🔍 문서 검색 및 색인

### 주요 개념별 문서

| 개념 | 관련 문서 |
|------|---------|
| 시스템 구조 | [ARCHITECTURE.md](./ARCHITECTURE.md) |
| API 엔드포인트 | [API.md](./API.md), [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) |
| UI/UX 스타일 | [UI_UX_GUIDE.md](./UI_UX_GUIDE.md) |
| 색상 팔레트 | [UI_UX_GUIDE.md#색상-시스템](./UI_UX_GUIDE.md#색상-시스템) |
| 환경 설정 | [SETUP.md](./SETUP.md), [DOCKER.md](./DOCKER.md) |
| 배포 및 운영 | [OPERATIONS_MANUAL.md](./OPERATIONS_MANUAL.md) |
| 개발 규칙 | [CLAUDE.md](../CLAUDE.md) |
| 테스트 | [MANUAL-TEST-SCENARIOS.md](./MANUAL-TEST-SCENARIOS.md) |
| GitHub 관리 | [GITHUB_GUIDE.md](../GITHUB_GUIDE.md) |

---

## 🚨 중요 링크

### 외부 리소스
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [React 공식 문서](https://react.dev/)
- [PostgreSQL 공식 문서](https://www.postgresql.org/docs/)
- [Docker 공식 문서](https://docs.docker.com/)
- [Tailwind CSS 공식 문서](https://tailwindcss.com/)

### 프로젝트 리소스
- GitHub 저장소: https://github.com/k82022603/DevLog
- 배포 주소: (준비 중)

---

## 📝 문서 기여 가이드

새로운 문서를 추가하거나 기존 문서를 수정할 때:

1. **명확한 제목과 목차** 작성
2. **코드 예제** 포함 (해당하는 경우)
3. **이미지/다이어그램** 추가 (시각화 필요 시)
4. 이 **README.md**에 새 문서 링크 추가
5. **마지막 수정 날짜** 표시

---

## 📞 문의 및 지원

- 문서 오류나 개선사항: GitHub Issues
- 기술적 질문: GitHub Discussions
- 프로젝트 피드백: [PROJECT_RETROSPECTIVE.md](../PROJECT_RETROSPECTIVE.md) 참조

---

**마지막 업데이트**: 2025-12-31
**버전**: v1.1.0
**상태**: 활성 관리 중
