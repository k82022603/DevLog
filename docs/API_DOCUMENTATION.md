# DevLog API 상세 문서

> **빠른 시작**: API에 대한 간단한 개요는 [API.md](./API.md)를 참조하세요.
> 이 문서는 모든 37개 엔드포인트의 **상세한 구현 정보**를 제공합니다.

---

## 📌 목차

- [개요](#개요)
- [Base URL](#base-url)
- [인증](#인증)
- [공통 응답 형식](#공통-응답-형식)
- [에러 코드](#에러-코드)
- [API 엔드포인트](#api-엔드포인트)
  - [Health Check](#health-check)
  - [Projects API](#projects-api)
  - [DevLogs API](#devlogs-api)
  - [Statistics API](#statistics-api)

---

## 개요

DevLog API는 개발자의 일일 개발 로그를 관리하고 프로젝트를 추적하며 통계를 제공하는 RESTful API입니다.

**버전**: 1.0.0
**프로토콜**: HTTP/HTTPS
**데이터 형식**: JSON

---

## Base URL

### 로컬 개발 환경
```
http://localhost:8080
```

### 프로덕션
```
https://your-domain.com
```

모든 API 엔드포인트는 `/api` 접두사를 사용합니다.

---

## 인증

현재 버전에서는 인증이 구현되어 있지 않습니다. 향후 JWT 기반 인증이 추가될 예정입니다.

---

## 공통 응답 형식

### 성공 응답

```json
{
  "data": { /* 응답 데이터 */ }
}
```

또는 배열 형태:

```json
[
  { /* 항목 1 */ },
  { /* 항목 2 */ }
]
```

### 에러 응답

```json
{
  "status": 400,
  "message": "에러 메시지",
  "timestamp": "2025-12-31T10:00:00"
}
```

---

## 에러 코드

| 상태 코드 | 설명 |
|---------|------|
| 200 OK | 요청 성공 |
| 201 Created | 리소스 생성 성공 |
| 204 No Content | 요청 성공, 응답 본문 없음 (주로 삭제) |
| 400 Bad Request | 잘못된 요청 (유효성 검사 실패) |
| 404 Not Found | 리소스를 찾을 수 없음 |
| 500 Internal Server Error | 서버 내부 오류 |

### 일반적인 에러 메시지

- `"Project not found"` - 프로젝트를 찾을 수 없음
- `"DevLog not found"` - 로그를 찾을 수 없음
- `"Title is required"` - 제목이 필요함
- `"Invalid date format"` - 날짜 형식이 올바르지 않음

---

## API 엔드포인트

## Health Check

### GET /health

서버 상태를 확인합니다.

**요청 예시**
```bash
curl http://localhost:8080/health
```

**응답 예시**
```json
{
  "status": "OK",
  "message": "DevLog API is running",
  "timestamp": 1735632000000
}
```

---

## Projects API

### GET /api/projects

모든 프로젝트 목록을 조회합니다.

**요청 예시**
```bash
curl http://localhost:8080/api/projects
```

**응답 예시**
```json
[
  {
    "id": 1,
    "name": "DevLog 프로젝트",
    "description": "개발자를 위한 일일 개발 로그 시스템",
    "status": "ACTIVE",
    "progress": 75,
    "techStack": "Spring Boot,React,PostgreSQL",
    "startDate": "2025-01-01T00:00:00",
    "endDate": null,
    "createdAt": "2025-01-20T10:00:00",
    "updatedAt": "2025-01-20T10:00:00"
  },
  {
    "id": 2,
    "name": "모바일 앱 개발",
    "description": "React Native 기반 모바일 애플리케이션",
    "status": "ON_HOLD",
    "progress": 30,
    "techStack": "React Native,TypeScript",
    "startDate": "2025-12-01T00:00:00",
    "endDate": null,
    "createdAt": "2025-12-15T10:00:00",
    "updatedAt": "2025-12-15T10:00:00"
  }
]
```

**상태 값 (status)**
- `ACTIVE` - 진행 중
- `COMPLETED` - 완료됨
- `ON_HOLD` - 보류됨
- `ARCHIVED` - 아카이브됨

---

### GET /api/projects/{id}

특정 프로젝트의 상세 정보를 조회합니다.

**URL 파라미터**
- `id` (required) - 프로젝트 ID

**요청 예시**
```bash
curl http://localhost:8080/api/projects/1
```

**응답 예시**
```json
{
  "id": 1,
  "name": "DevLog 프로젝트",
  "description": "개발자를 위한 일일 개발 로그 시스템",
  "status": "ACTIVE",
  "progress": 75,
  "techStack": "Spring Boot,React,PostgreSQL",
  "startDate": "2025-01-01T00:00:00",
  "endDate": null,
  "createdAt": "2025-01-20T10:00:00",
  "updatedAt": "2025-01-20T10:00:00"
}
```

**에러 응답 (404)**
```json
{
  "status": 404,
  "message": "Project not found",
  "timestamp": "2025-12-31T10:00:00"
}
```

---

### POST /api/projects

새로운 프로젝트를 생성합니다.

**Request Body**
```json
{
  "name": "새 프로젝트",
  "description": "프로젝트 설명",
  "status": "ACTIVE",
  "progress": 0,
  "techStack": "Java,Spring Boot",
  "startDate": "2025-01-20T00:00:00"
}
```

**필수 필드**
- `name` - 프로젝트 이름 (최대 200자)
- `status` - 프로젝트 상태 (ACTIVE, COMPLETED, ON_HOLD, ARCHIVED)

**선택 필드**
- `description` - 프로젝트 설명
- `progress` - 진행률 (0-100)
- `techStack` - 기술 스택 (쉼표로 구분)
- `startDate` - 시작일
- `endDate` - 종료일

**요청 예시**
```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "새 프로젝트",
    "description": "프로젝트 설명",
    "status": "ACTIVE",
    "progress": 0,
    "techStack": "Java,Spring Boot",
    "startDate": "2025-01-20T00:00:00"
  }'
```

**응답 예시 (201 Created)**
```json
{
  "id": 3,
  "name": "새 프로젝트",
  "description": "프로젝트 설명",
  "status": "ACTIVE",
  "progress": 0,
  "techStack": "Java,Spring Boot",
  "startDate": "2025-01-20T00:00:00",
  "endDate": null,
  "createdAt": "2025-12-31T10:00:00",
  "updatedAt": "2025-12-31T10:00:00"
}
```

---

### PUT /api/projects/{id}

기존 프로젝트를 수정합니다.

**URL 파라미터**
- `id` (required) - 프로젝트 ID

**Request Body**
```json
{
  "name": "수정된 프로젝트",
  "description": "수정된 설명",
  "status": "COMPLETED",
  "progress": 100,
  "techStack": "Java,Spring Boot,PostgreSQL",
  "startDate": "2025-01-01T00:00:00",
  "endDate": "2025-12-31T00:00:00"
}
```

**요청 예시**
```bash
curl -X PUT http://localhost:8080/api/projects/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "수정된 프로젝트",
    "description": "수정된 설명",
    "status": "COMPLETED",
    "progress": 100
  }'
```

**응답 예시 (200 OK)**
```json
{
  "id": 1,
  "name": "수정된 프로젝트",
  "description": "수정된 설명",
  "status": "COMPLETED",
  "progress": 100,
  "techStack": "Java,Spring Boot,PostgreSQL",
  "startDate": "2025-01-01T00:00:00",
  "endDate": "2025-12-31T00:00:00",
  "createdAt": "2025-01-20T10:00:00",
  "updatedAt": "2025-12-31T10:00:00"
}
```

---

### DELETE /api/projects/{id}

프로젝트를 삭제합니다. 연관된 모든 개발 로그도 함께 삭제됩니다 (CASCADE).

**URL 파라미터**
- `id` (required) - 프로젝트 ID

**요청 예시**
```bash
curl -X DELETE http://localhost:8080/api/projects/1
```

**응답 예시 (204 No Content)**
```
(응답 본문 없음)
```

---

## DevLogs API

### GET /api/logs

개발 로그 목록을 조회합니다. 다양한 필터 옵션을 지원합니다.

**Query 파라미터**
- `page` (optional) - 페이지 번호 (기본값: 1)
- `size` (optional) - 페이지 크기 (기본값: 10)
- `projectId` (optional) - 프로젝트 ID로 필터링
- `startDate` (optional) - 시작 날짜 (yyyy-MM-dd 또는 yyyy-MM-ddTHH:mm:ss)
- `endDate` (optional) - 종료 날짜 (yyyy-MM-dd 또는 yyyy-MM-ddTHH:mm:ss)
- `keyword` (optional) - 제목 또는 내용 검색 키워드

**요청 예시 1: 기본 조회**
```bash
curl http://localhost:8080/api/logs
```

**요청 예시 2: 프로젝트별 필터링**
```bash
curl "http://localhost:8080/api/logs?projectId=1"
```

**요청 예시 3: 날짜 범위 필터링**
```bash
curl "http://localhost:8080/api/logs?startDate=2025-12-01&endDate=2025-12-31"
```

**요청 예시 4: 키워드 검색**
```bash
curl "http://localhost:8080/api/logs?keyword=React"
```

**요청 예시 5: 복합 필터**
```bash
curl "http://localhost:8080/api/logs?projectId=1&startDate=2025-12-01&page=1&size=20"
```

**응답 예시**
```json
[
  {
    "id": 1,
    "projectId": 1,
    "projectName": "DevLog 프로젝트",
    "title": "Spring Boot 프로젝트 초기 설정",
    "description": "프로젝트 구조 설정 및 의존성 추가",
    "content": "Spring Boot 3.2.1과 MyBatis를 이용한 백엔드 프로젝트 초기 설정 완료...",
    "tags": "Spring Boot,MyBatis,PostgreSQL",
    "mood": "GOOD",
    "startTime": "09:00:00",
    "endTime": "12:30:00",
    "workMinutes": 210,
    "logDate": "2025-12-30T00:00:00",
    "createdAt": "2025-12-30T12:30:00",
    "updatedAt": "2025-12-30T12:30:00"
  },
  {
    "id": 2,
    "projectId": 1,
    "projectName": "DevLog 프로젝트",
    "title": "프론트엔드 React 설정",
    "description": "Tailwind CSS 및 React Router 설정",
    "content": "React 18.2 프로젝트 생성 및 기본 라우팅 구조 완성...",
    "tags": "React,Tailwind CSS",
    "mood": "GREAT",
    "startTime": "14:00:00",
    "endTime": "18:00:00",
    "workMinutes": 240,
    "logDate": "2025-12-30T00:00:00",
    "createdAt": "2025-12-30T18:00:00",
    "updatedAt": "2025-12-30T18:00:00"
  }
]
```

**감정 상태 값 (mood)**
- `GREAT` - 😊 매우 좋음
- `GOOD` - 🙂 좋음
- `NEUTRAL` - 😐 보통
- `BAD` - 😞 나쁨
- `TERRIBLE` - 😫 매우 나쁨

---

### GET /api/logs/{id}

특정 개발 로그의 상세 정보를 조회합니다.

**URL 파라미터**
- `id` (required) - 로그 ID

**요청 예시**
```bash
curl http://localhost:8080/api/logs/1
```

**응답 예시**
```json
{
  "id": 1,
  "projectId": 1,
  "projectName": "DevLog 프로젝트",
  "title": "Spring Boot 프로젝트 초기 설정",
  "description": "프로젝트 구조 설정 및 의존성 추가",
  "content": "# Spring Boot 초기 설정\n\n## 1. 프로젝트 생성\n...",
  "tags": "Spring Boot,MyBatis,PostgreSQL",
  "mood": "GOOD",
  "startTime": "09:00:00",
  "endTime": "12:30:00",
  "workMinutes": 210,
  "logDate": "2025-12-30T00:00:00",
  "createdAt": "2025-12-30T12:30:00",
  "updatedAt": "2025-12-30T12:30:00"
}
```

**에러 응답 (404)**
```json
{
  "status": 404,
  "message": "DevLog not found",
  "timestamp": "2025-12-31T10:00:00"
}
```

---

### POST /api/logs

새로운 개발 로그를 작성합니다.

**Request Body**
```json
{
  "projectId": 1,
  "title": "새로운 기능 개발",
  "description": "사용자 인증 기능 구현",
  "content": "오늘은 JWT 기반 사용자 인증 기능을 구현했습니다...",
  "tags": "Authentication,Security,JWT",
  "mood": "GOOD",
  "startTime": "09:00:00",
  "endTime": "17:00:00",
  "logDate": "2025-12-31T00:00:00"
}
```

**필수 필드**
- `projectId` - 프로젝트 ID
- `title` - 로그 제목 (최대 500자)
- `content` - 로그 내용
- `logDate` - 로그 날짜

**선택 필드**
- `description` - 짧은 설명 (최대 1000자)
- `tags` - 태그 (쉼표로 구분)
- `mood` - 감정 상태 (GREAT, GOOD, NEUTRAL, BAD, TERRIBLE)
- `startTime` - 작업 시작 시간 (HH:mm:ss)
- `endTime` - 작업 종료 시간 (HH:mm:ss)

**요청 예시**
```bash
curl -X POST http://localhost:8080/api/logs \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": 1,
    "title": "새로운 기능 개발",
    "description": "사용자 인증 기능 구현",
    "content": "오늘은 JWT 기반 사용자 인증 기능을 구현했습니다...",
    "tags": "Authentication,Security,JWT",
    "mood": "GOOD",
    "startTime": "09:00:00",
    "endTime": "17:00:00",
    "logDate": "2025-12-31T00:00:00"
  }'
```

**응답 예시 (201 Created)**
```json
{
  "id": 15,
  "projectId": 1,
  "projectName": "DevLog 프로젝트",
  "title": "새로운 기능 개발",
  "description": "사용자 인증 기능 구현",
  "content": "오늘은 JWT 기반 사용자 인증 기능을 구현했습니다...",
  "tags": "Authentication,Security,JWT",
  "mood": "GOOD",
  "startTime": "09:00:00",
  "endTime": "17:00:00",
  "workMinutes": 480,
  "logDate": "2025-12-31T00:00:00",
  "createdAt": "2025-12-31T17:00:00",
  "updatedAt": "2025-12-31T17:00:00"
}
```

**참고**: `workMinutes`는 `startTime`과 `endTime`을 기반으로 자동 계산됩니다.

---

### PUT /api/logs/{id}

기존 개발 로그를 수정합니다.

**URL 파라미터**
- `id` (required) - 로그 ID

**Request Body**
```json
{
  "projectId": 1,
  "title": "수정된 제목",
  "description": "수정된 설명",
  "content": "수정된 내용...",
  "tags": "Updated,Tags",
  "mood": "GREAT",
  "startTime": "09:00:00",
  "endTime": "18:00:00",
  "logDate": "2025-12-31T00:00:00"
}
```

**요청 예시**
```bash
curl -X PUT http://localhost:8080/api/logs/1 \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": 1,
    "title": "수정된 제목",
    "content": "수정된 내용..."
  }'
```

**응답 예시 (200 OK)**
```json
{
  "id": 1,
  "projectId": 1,
  "projectName": "DevLog 프로젝트",
  "title": "수정된 제목",
  "description": "수정된 설명",
  "content": "수정된 내용...",
  "tags": "Updated,Tags",
  "mood": "GREAT",
  "startTime": "09:00:00",
  "endTime": "18:00:00",
  "workMinutes": 540,
  "logDate": "2025-12-31T00:00:00",
  "createdAt": "2025-12-30T12:30:00",
  "updatedAt": "2025-12-31T10:00:00"
}
```

---

### DELETE /api/logs/{id}

개발 로그를 삭제합니다.

**URL 파라미터**
- `id` (required) - 로그 ID

**요청 예시**
```bash
curl -X DELETE http://localhost:8080/api/logs/1
```

**응답 예시 (204 No Content)**
```
(응답 본문 없음)
```

---

## Statistics API

### GET /api/statistics/weekly/current

이번 주의 통계를 조회합니다 (월요일부터 일요일까지).

**요청 예시**
```bash
curl http://localhost:8080/api/statistics/weekly/current
```

**응답 예시**
```json
{
  "startDate": "2025-12-29",
  "endDate": "2026-01-04",
  "totalLogs": 18,
  "totalWorkMinutes": 2670,
  "avgWorkMinutes": 148,
  "activeProjects": 2,
  "dailyCounts": [
    {
      "date": "2025-12-29",
      "dayOfWeek": "MONDAY",
      "count": 3,
      "workMinutes": 450
    },
    {
      "date": "2025-12-30",
      "dayOfWeek": "TUESDAY",
      "count": 4,
      "workMinutes": 540
    },
    {
      "date": "2025-12-31",
      "dayOfWeek": "WEDNESDAY",
      "count": 3,
      "workMinutes": 420
    },
    {
      "date": "2026-01-01",
      "dayOfWeek": "THURSDAY",
      "count": 2,
      "workMinutes": 300
    },
    {
      "date": "2026-01-02",
      "dayOfWeek": "FRIDAY",
      "count": 3,
      "workMinutes": 480
    },
    {
      "date": "2026-01-03",
      "dayOfWeek": "SATURDAY",
      "count": 2,
      "workMinutes": 240
    },
    {
      "date": "2026-01-04",
      "dayOfWeek": "SUNDAY",
      "count": 1,
      "workMinutes": 240
    }
  ],
  "projectCounts": [
    {
      "projectId": 1,
      "projectName": "DevLog 프로젝트",
      "count": 12,
      "workMinutes": 1800
    },
    {
      "projectId": 2,
      "projectName": "모바일 앱 개발",
      "count": 6,
      "workMinutes": 870
    }
  ]
}
```

---

### GET /api/statistics/weekly/{year}/{week}

특정 주의 통계를 조회합니다.

**URL 파라미터**
- `year` (required) - 연도 (예: 2025)
- `week` (required) - 주 번호 (1-53)

**요청 예시**
```bash
curl http://localhost:8080/api/statistics/weekly/2025/52
```

**응답 형식**: `/weekly/current`와 동일

---

### GET /api/statistics/monthly/current

이번 달의 통계를 조회합니다.

**요청 예시**
```bash
curl http://localhost:8080/api/statistics/monthly/current
```

**응답 예시**
```json
{
  "year": 2025,
  "month": 12,
  "totalLogs": 45,
  "totalWorkMinutes": 6750,
  "avgWorkMinutes": 150,
  "activeProjects": 3,
  "workDays": 22,
  "dailyCounts": [
    {
      "date": "2025-12-01",
      "count": 2,
      "workMinutes": 300
    },
    {
      "date": "2025-12-02",
      "count": 3,
      "workMinutes": 450
    }
    // ... 나머지 날짜들
  ],
  "projectCounts": [
    {
      "projectId": 1,
      "projectName": "DevLog 프로젝트",
      "count": 25,
      "workMinutes": 3750
    },
    {
      "projectId": 2,
      "projectName": "모바일 앱 개발",
      "count": 15,
      "workMinutes": 2250
    },
    {
      "projectId": 3,
      "projectName": "데이터 분석 도구",
      "count": 5,
      "workMinutes": 750
    }
  ]
}
```

---

### GET /api/statistics/monthly/{year}/{month}

특정 달의 통계를 조회합니다.

**URL 파라미터**
- `year` (required) - 연도 (예: 2025)
- `month` (required) - 월 (1-12)

**요청 예시**
```bash
curl http://localhost:8080/api/statistics/monthly/2025/11
```

**응답 형식**: `/monthly/current`와 동일

---

### GET /api/statistics/project/{projectId}

특정 프로젝트의 전체 통계를 조회합니다.

**URL 파라미터**
- `projectId` (required) - 프로젝트 ID

**요청 예시**
```bash
curl http://localhost:8080/api/statistics/project/1
```

**응답 예시**
```json
{
  "projectId": 1,
  "projectName": "DevLog 프로젝트",
  "projectDescription": "개발자를 위한 일일 개발 로그 시스템",
  "projectStatus": "ACTIVE",
  "projectProgress": 75,
  "totalLogs": 85,
  "totalWorkMinutes": 12750,
  "avgWorkMinutes": 150,
  "techStackCounts": [
    {
      "techStack": "Spring Boot",
      "count": 45,
      "workMinutes": 6750
    },
    {
      "techStack": "React",
      "count": 40,
      "workMinutes": 6000
    }
  ],
  "moodCounts": [
    {
      "mood": "GREAT",
      "count": 30
    },
    {
      "mood": "GOOD",
      "count": 40
    },
    {
      "mood": "NEUTRAL",
      "count": 10
    },
    {
      "mood": "BAD",
      "count": 4
    },
    {
      "mood": "TERRIBLE",
      "count": 1
    }
  ]
}
```

---

### GET /api/statistics/tech-stack

전체 기술 스택의 사용 통계를 조회합니다.

**요청 예시**
```bash
curl http://localhost:8080/api/statistics/tech-stack
```

**응답 예시**
```json
{
  "techStacks": [
    {
      "techStack": "Spring Boot",
      "count": 65,
      "workMinutes": 9750,
      "projects": 2
    },
    {
      "techStack": "React",
      "count": 55,
      "workMinutes": 8250,
      "projects": 2
    },
    {
      "techStack": "PostgreSQL",
      "count": 45,
      "workMinutes": 6750,
      "projects": 2
    },
    {
      "techStack": "MyBatis",
      "count": 30,
      "workMinutes": 4500,
      "projects": 1
    },
    {
      "techStack": "Tailwind CSS",
      "count": 40,
      "workMinutes": 6000,
      "projects": 1
    }
  ]
}
```

---

## 예제 시나리오

### 시나리오 1: 새 프로젝트 생성 및 첫 로그 작성

```bash
# 1. 프로젝트 생성
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "새 프로젝트",
    "description": "프로젝트 설명",
    "status": "ACTIVE",
    "progress": 0,
    "techStack": "Java,Spring Boot",
    "startDate": "2025-12-31T00:00:00"
  }'

# 응답에서 projectId (예: 3)를 받음

# 2. 첫 번째 로그 작성
curl -X POST http://localhost:8080/api/logs \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": 3,
    "title": "프로젝트 초기 설정",
    "description": "개발 환경 구성",
    "content": "프로젝트 생성 및 Git 초기화...",
    "tags": "Setup,Init",
    "mood": "GOOD",
    "startTime": "09:00:00",
    "endTime": "11:00:00",
    "logDate": "2025-12-31T00:00:00"
  }'
```

### 시나리오 2: 특정 프로젝트의 이번 주 작업 조회

```bash
# 1. 이번 주 전체 통계 조회
curl http://localhost:8080/api/statistics/weekly/current

# 2. 특정 프로젝트 로그만 필터링
curl "http://localhost:8080/api/logs?projectId=1&startDate=2025-12-29&endDate=2026-01-04"
```

### 시나리오 3: 프로젝트 완료 처리

```bash
# 1. 프로젝트 상태 업데이트
curl -X PUT http://localhost:8080/api/projects/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "DevLog 프로젝트",
    "description": "개발자를 위한 일일 개발 로그 시스템",
    "status": "COMPLETED",
    "progress": 100,
    "techStack": "Spring Boot,React,PostgreSQL",
    "startDate": "2025-01-01T00:00:00",
    "endDate": "2025-12-31T00:00:00"
  }'

# 2. 프로젝트 통계 확인
curl http://localhost:8080/api/statistics/project/1
```

---

## 개발자 노트

### 날짜 형식

API는 다음 두 가지 날짜 형식을 모두 지원합니다:

1. **날짜만**: `yyyy-MM-dd` (예: `2025-12-31`)
2. **날짜 + 시간**: `yyyy-MM-ddTHH:mm:ss` (예: `2025-12-31T10:00:00`)

날짜만 제공하는 경우, 시간은 자동으로 `00:00:00`으로 설정됩니다.

### 작업 시간 계산

`workMinutes`는 `startTime`과 `endTime`을 기반으로 자동 계산됩니다:

```
workMinutes = (endTime - startTime) / 60
```

예시:
- `startTime`: 09:00:00
- `endTime`: 17:00:00
- `workMinutes`: 480 (8시간)

### 페이징

현재 `GET /api/logs` API는 기본적인 페이징을 지원합니다:

- `page`: 페이지 번호 (기본값: 1)
- `size`: 페이지 크기 (기본값: 10)

향후 전체 페이징 메타데이터 (totalElements, totalPages 등)를 포함하는 업데이트가 예정되어 있습니다.

### 데이터베이스 참고사항

PostgreSQL을 사용하는 경우, 컬럼명이 소문자로 반환됩니다. MyBatis 매퍼 파일에서는 `map-underscore-to-camel-case: true` 설정을 사용하지만, `resultType="map"`을 사용하는 통계 쿼리에서는 컬럼 별칭을 명시적으로 소문자로 작성해야 합니다.

---

## 변경 이력

### v1.0.0 (2025-12-31)
- 초기 API 릴리스
- Projects, DevLogs, Statistics 엔드포인트 구현
- 주간/월간 통계 기능
- 프로젝트별 통계 및 기술 스택 분석

---

## 문의 및 지원

API 사용 중 문제가 발생하거나 질문이 있으시면:

- **GitHub Issues**: [DevLog Issues](https://github.com/yourusername/devlog/issues)
- **Email**: your.email@example.com

---

**DevLog API Documentation v1.0.0**
*Last Updated: 2025-12-31*
