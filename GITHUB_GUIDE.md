# DevLog GitHub Repository Guide

> DevLog 프로젝트의 GitHub에 올린 과정과 사용 방법을 설명하는 가이드입니다.

---

## 📚 목차

- [프로젝트 개요](#프로젝트-개요)
- [GitHub 업로드 과정](#github-업로드-과정)
- [Repository 구조](#repository-구조)
- [시작하기](#시작하기)
- [주요 브랜치 전략](#주요-브랜치-전략)
- [기여 가이드](#기여-가이드)
- [문제 해결](#문제-해결)

---

## 프로젝트 개요

### 🎯 DevLog란?

**DevLog**는 개발자를 위한 종합 개발 로그 및 프로젝트 관리 시스템입니다.

- **일일 개발 로그**: 개발 활동을 매일 기록
- **프로젝트 관리**: 여러 프로젝트를 체계적으로 관리
- **통계 분석**: 개발 패턴을 시각화하여 분석
- **기술 추적**: 사용한 기술 스택을 태그로 관리

### 📊 기술 스택

```
Frontend  │ Backend     │ Database  │ DevOps
──────────┼─────────────┼───────────┼──────────
React 18  │ Spring Boot │ PostgreSQL│ Docker
Tailwind  │ Java 17     │ H2 (Dev)  │ Docker
Recharts  │ MyBatis     │           │ Compose
```

### 🌐 Repository URL

```
https://github.com/k82022603/DevLog
```

---

## GitHub 업로드 과정

### 1️⃣ 준비 단계

#### 1.1 프로젝트 정리

```bash
# 불필요한 디렉토리 제거
rm -rf backend/target
rm -rf frontend/node_modules
rm -rf frontend/build

# 민감한 정보 확인 및 제거
find . -name ".env" -not -name ".env.example"
```

#### 1.2 .gitignore 설정

**생성된 .gitignore 포함 항목**:

```gitignore
# 빌드 결과물
backend/target/
frontend/build/
frontend/node_modules/

# 환경 설정
.env
.env.local
.env.*.local

# IDE
.idea/
.vscode/
*.iml

# OS
.DS_Store
Thumbs.db

# 로그
*.log
docker-build.log
```

**파일 위치**: `/.gitignore`

### 2️⃣ Git 초기화 및 커밋

#### 2.1 Git 설정

```bash
# Git 초기화
git init

# 사용자 정보 설정
git config user.name "DevLog Developer"
git config user.email "devlog@vibecoding.com"

# Main 브랜치로 변경
git branch -m main
```

#### 2.2 첫 커밋 작성

```bash
# 모든 파일 스테이징
git add .

# 상세 커밋 메시지
git commit -m "Initial commit: DevLog project - Full-stack development log and project management system

- Backend: Spring Boot 3.2.1 with MyBatis and PostgreSQL
- Frontend: React 18.2 with Tailwind CSS and Recharts
- Features: Daily log tracking, project management, statistics dashboard
- Architecture: Docker containerized full-stack application
- Documentation: README, CLAUDE.md, UI_UX_GUIDE.md, and comprehensive API docs"
```

**커밋 통계**:
- 108개 파일 추가
- 23,129줄 추가
- 주요 항목:
  - Backend Java 소스코드
  - Frontend React 컴포넌트
  - 데이터베이스 스키마
  - 종합 문서

### 3️⃣ GitHub 원격 저장소 연결

#### 3.1 원격 저장소 추가

```bash
# Origin 추가
git remote add origin https://github.com/k82022603/DevLog.git

# 원격 저장소 확인
git remote -v
```

#### 3.2 코드 푸시

```bash
# Main 브랜치 푸시
git push -u origin main
```

**인증 방법**:

**방법 1: Personal Access Token (권장)**
```bash
# GitHub Personal Access Token 생성
# Settings > Developer settings > Personal access tokens

# 입력 시
Username: YOUR_GITHUB_USERNAME
Password: YOUR_PERSONAL_ACCESS_TOKEN
```

**방법 2: SSH 키 설정**
```bash
# SSH 키 생성
ssh-keygen -t ed25519 -C "your-email@example.com"

# 공개 키를 GitHub에 등록
cat ~/.ssh/id_ed25519.pub

# SSH URL로 원격 저장소 변경
git remote set-url origin git@github.com:k82022603/DevLog.git
```

---

## Repository 구조

### 📁 전체 구조

```
DevLog/
├── 📄 README.md                    # 프로젝트 개요 및 사용 가이드
├── 📄 CLAUDE.md                    # Claude AI 개발 가이드
├── 📄 GITHUB_GUIDE.md             # GitHub 가이드 (이 파일)
├── 📄 .gitignore                  # Git 무시 파일 설정
├── 📄 LICENSE                     # MIT 라이선스 (권장)
├── 🐳 docker-compose.yml          # Docker Compose 설정
│
├── 📁 backend/                    # Spring Boot 백엔드
│   ├── 📄 pom.xml                # Maven 설정
│   ├── 🐳 Dockerfile             # 백엔드 Docker 이미지
│   ├── 📁 src/main/
│   │   ├── java/com/vibecoding/devlog/
│   │   │   ├── controller/       # REST API 컨트롤러
│   │   │   ├── service/          # 비즈니스 로직
│   │   │   ├── mapper/           # MyBatis 매퍼
│   │   │   ├── domain/           # 도메인 모델
│   │   │   ├── dto/              # DTO 클래스
│   │   │   └── config/           # 설정 클래스
│   │   └── resources/
│   │       ├── application.yml   # 설정 파일
│   │       └── mapper/           # MyBatis XML 맵퍼
│   └── .gitignore               # 백엔드 Git 무시 설정
│
├── 📁 frontend/                   # React 프론트엔드
│   ├── 📄 package.json           # npm 의존성
│   ├── 🐳 Dockerfile            # 프론트엔드 Docker 이미지
│   ├── 📄 nginx.conf            # Nginx 설정
│   ├── 📄 tailwind.config.js    # Tailwind CSS 설정
│   ├── 📁 src/
│   │   ├── pages/               # 페이지 컴포넌트
│   │   ├── components/          # 공유 컴포넌트
│   │   ├── services/            # API 서비스
│   │   ├── utils/               # 유틸리티 함수
│   │   ├── App.js               # 메인 앱
│   │   └── index.js             # 엔트리 포인트
│   ├── 📁 public/               # 정적 파일
│   └── .gitignore              # 프론트엔드 Git 무시 설정
│
├── 📁 database/                  # 데이터베이스 스크립트
│   ├── 📄 schema.sql            # 테이블 생성 SQL
│   ├── 📄 seed.sql              # 초기 데이터 SQL
│   ├── 📄 test-data-week.sql   # 테스트 데이터
│   ├── 📄 SCHEMA.md             # 스키마 문서
│   └── 📄 README.md             # 데이터베이스 가이드
│
├── 📁 docs/                      # 프로젝트 문서
│   ├── 📄 README.md             # 문서 목차
│   ├── 📄 SETUP.md              # 설치 가이드
│   ├── 📄 API.md                # API 명세서
│   ├── 📄 ARCHITECTURE.md       # 아키텍처 문서
│   ├── 📄 DOCKER.md             # Docker 가이드
│   ├── 📄 UI_UX_GUIDE.md        # UI/UX 개발 가이드
│   ├── 📄 OPERATIONS_MANUAL.md  # 운영 매뉴얼
│   ├── 📄 MANUAL-TEST-SCENARIOS.md # 테스트 시나리오
│   └── 📁 중간점검/             # 프로젝트 진행 문서
│
├── 📁 scripts/                   # 유틸리티 스크립트
│   ├── 📄 test-api.sh          # API 테스트 (Linux/Mac)
│   ├── 📄 test-api.bat         # API 테스트 (Windows)
│   ├── 📄 Test-API.ps1         # API 테스트 (PowerShell)
│   └── 📄 README.md             # 스크립트 가이드
│
└── 📄 .env.example              # 환경 변수 예시
```

### 📄 주요 파일 설명

| 파일/폴더 | 용도 | 중요도 |
|----------|------|--------|
| `README.md` | 프로젝트 개요, 시작 가이드 | ⭐⭐⭐⭐⭐ |
| `CLAUDE.md` | AI 개발자용 가이드 | ⭐⭐⭐⭐ |
| `GITHUB_GUIDE.md` | GitHub 사용 방법 | ⭐⭐⭐⭐ |
| `docker-compose.yml` | 전체 서비스 실행 설정 | ⭐⭐⭐⭐⭐ |
| `docs/UI_UX_GUIDE.md` | UI/UX 개발 가이드 | ⭐⭐⭐⭐ |
| `docs/API.md` | API 명세서 | ⭐⭐⭐⭐⭐ |
| `.gitignore` | Git 무시 설정 | ⭐⭐⭐ |

---

## 시작하기

### 📥 Repository 클론

```bash
# HTTPS로 클론
git clone https://github.com/k82022603/DevLog.git

# SSH로 클론
git clone git@github.com:k82022603/DevLog.git

# 디렉토리로 이동
cd DevLog
```

### 🚀 개발 환경 실행

#### 방법 1: Docker Compose (권장)

```bash
# 모든 서비스 실행
docker-compose up -d

# 서비스 확인
docker-compose ps

# 로그 확인
docker-compose logs -f
```

**접근 URL**:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- API Health Check: http://localhost:8080/api/health
- PostgreSQL: localhost:5432

#### 방법 2: 로컬 개발

**백엔드 실행**:
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**프론트엔드 실행**:
```bash
cd frontend
npm install
npm start
```

### 🧪 API 테스트

```bash
# Linux/Mac
bash scripts/test-api.sh

# Windows (CMD)
scripts\test-api.bat

# Windows (PowerShell)
powershell -ExecutionPolicy Bypass -File "scripts/Test-API.ps1"
```

---

## 주요 브랜치 전략

### 브랜치 구조

```
main (프로덕션)
 ├── develop (개발 메인)
 │   ├── feature/log-search
 │   ├── feature/statistics-dashboard
 │   └── bugfix/dashboard-stats
 └── hotfix/critical-bug
```

### 브랜치 설명

| 브랜치 | 목적 | 생성 | 병합 대상 |
|-------|------|------|---------|
| `main` | 프로덕션 준비 완료 | Release | - |
| `develop` | 개발 메인 브랜치 | Feature 병합 | main |
| `feature/*` | 새 기능 개발 | develop에서 | develop |
| `bugfix/*` | 버그 수정 | develop에서 | develop |
| `hotfix/*` | 긴급 수정 | main에서 | main, develop |

### 브랜치 관리 명령어

```bash
# develop 브랜치 생성 및 이동
git checkout -b develop

# feature 브랜치 생성
git checkout -b feature/log-search

# 브랜치 목록 확인
git branch -a

# 원격 브랜치 푸시
git push -u origin feature/log-search

# develop으로 돌아가기
git checkout develop

# 최신 내용 가져오기
git pull origin develop

# feature 병합
git merge feature/log-search

# feature 브랜치 삭제
git branch -d feature/log-search
```

---

## 기여 가이드

### 🤝 기여하는 방법

#### 1단계: Fork

```bash
# GitHub에서 "Fork" 버튼 클릭
# 또는 gh CLI 사용
gh repo fork k82022603/DevLog
```

#### 2단계: Clone

```bash
git clone https://github.com/YOUR_USERNAME/DevLog.git
cd DevLog
```

#### 3단계: Feature 브랜치 생성

```bash
git checkout develop
git pull origin develop
git checkout -b feature/your-feature-name
```

#### 4단계: 코드 작성

```bash
# 코드 수정, 추가 등...
# CLAUDE.md와 UI_UX_GUIDE.md의 코드 스타일 따르기
```

#### 5단계: Commit

```bash
git add .
git commit -m "feat: Add your feature description

Detailed description of changes..."
```

**Commit 메시지 규칙**:
```
feat:     새로운 기능 추가
fix:      버그 수정
docs:     문서 수정
style:    코드 포맷팅 (기능 변경 없음)
refactor: 코드 리팩토링
test:     테스트 코드
chore:    빌드 설정 등
```

#### 6단계: Push

```bash
git push origin feature/your-feature-name
```

#### 7단계: Pull Request

```bash
# GitHub에서 Pull Request 생성
# 또는 gh CLI 사용
gh pr create --title "Your PR Title" --body "Description..."
```

#### 8단계: Review 및 Merge

- Maintainer의 코드 리뷰 대기
- 요청사항 반영
- Merge 완료

### 📋 기여 체크리스트

```
□ develop 브랜치에서 최신 코드로 시작
□ feature 브랜치 생성
□ 코드 작성 및 테스트
□ CLAUDE.md 가이드라인 준수
□ console.log 제거
□ Commit 메시지 명확하게 작성
□ Pull Request 생성
□ 코드 리뷰 반영
□ Merge 완료
□ feature 브랜치 삭제
```

---

## 문제 해결

### 🔧 Git 관련 문제

#### 문제: "fatal: not a git repository"

```bash
# 해결: 프로젝트 디렉토리 확인
cd DevLog
ls -la | grep .git

# .git 디렉토리 없으면
git init
```

#### 문제: "Permission denied (publickey)"

```bash
# SSH 키 설정
ssh-keygen -t ed25519 -C "your-email@example.com"

# 공개 키를 GitHub에 등록
# Settings > SSH and GPG keys > New SSH key

# SSH URL로 변경
git remote set-url origin git@github.com:k82022603/DevLog.git
```

#### 문제: "Authentication failed"

```bash
# Personal Access Token 생성
# GitHub Settings > Developer settings > Personal access tokens

# Git 자격증명 매니저로 업데이트
git config --global credential.helper store
git pull  # 다시 시도, 토큰 입력
```

### 🐳 Docker 관련 문제

#### 문제: 포트 이미 사용 중

```bash
# 포트 충돌 확인
lsof -i :3000
lsof -i :8080
lsof -i :5432

# Docker 컨테이너 중지
docker-compose down

# 특정 포트로 다시 실행
docker-compose -f docker-compose.yml up -d
```

#### 문제: Docker 이미지 빌드 실패

```bash
# 기존 이미지 제거
docker-compose down --volumes
docker system prune

# 캐시 없이 재빌드
docker-compose build --no-cache

# 다시 실행
docker-compose up -d
```

### 💻 개발 환경 문제

#### 문제: Node.js 모듈 설치 실패

```bash
cd frontend

# 캐시 정리
npm cache clean --force

# 의존성 재설치
rm -rf node_modules package-lock.json
npm install

# 개발 서버 시작
npm start
```

#### 문제: Maven 빌드 실패

```bash
cd backend

# Maven 캐시 정리
mvn clean

# 의존성 다시 다운로드
mvn install -U

# 빌드
mvn clean install
```

### 📞 추가 지원

문제가 해결되지 않으면:

1. **README.md** - 일반적인 문제 해결
2. **CLAUDE.md** - 개발 가이드 및 규칙
3. **docs/** - 상세한 기술 문서
4. **GitHub Issues** - 버그 보고 및 기능 요청

---

## 📊 Repository 통계

### 프로젝트 규모

```
Language     │ Files │ Lines
─────────────┼───────┼──────────
Java         │   25  │ ~3,500
JavaScript   │   18  │ ~4,200
SQL          │    5  │ ~800
YAML/Config  │   10  │ ~600
Markdown     │   12  │ ~5,000
```

### 커밋 히스토리

```
Initial Commit: 108 files, 23,129 additions
Branch Count: 1 (main)
License: MIT
```

---

## 📜 라이선스

DevLog는 **MIT License** 하에 배포됩니다.

```
MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software...
```

자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

---

## 🙏 감사의 말

DevLog를 사용해주시고, 기여해주시는 모든 분들께 감사드립니다!

## 💬 의견 및 피드백

- 📧 Email: devlog@vibecoding.com
- 🐙 GitHub Issues: [Report Bug](https://github.com/k82022603/DevLog/issues)
- 💡 Discussions: [Suggest Feature](https://github.com/k82022603/DevLog/discussions)

---

**Last Updated**: 2025-12-31
**Version**: 1.0.0
**Maintainer**: k82022603

