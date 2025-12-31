# DevLog API Documentation

## Base URL
```
http://localhost:8080/api
```

## Health Check

### GET /health
서버 상태 확인

**Response**
```json
{
  "status": "OK",
  "message": "DevLog API is running",
  "timestamp": 1234567890
}
```

## Projects API

### GET /projects
모든 프로젝트 목록 조회

**Response**
```json
[
  {
    "id": 1,
    "name": "DevLog 프로젝트",
    "description": "개발자를 위한 일일 개발 로그 시스템",
    "status": "ACTIVE",
    "startDate": "2025-01-01T00:00:00",
    "endDate": null,
    "createdAt": "2025-01-20T10:00:00",
    "updatedAt": "2025-01-20T10:00:00"
  }
]
```

### GET /projects/{id}
특정 프로젝트 조회

**Parameters**
- `id` (path): 프로젝트 ID

**Response**
```json
{
  "id": 1,
  "name": "DevLog 프로젝트",
  "description": "개발자를 위한 일일 개발 로그 시스템",
  "status": "ACTIVE",
  "startDate": "2025-01-01T00:00:00",
  "endDate": null,
  "createdAt": "2025-01-20T10:00:00",
  "updatedAt": "2025-01-20T10:00:00"
}
```

### POST /projects
새 프로젝트 생성

**Request Body**
```json
{
  "name": "새 프로젝트",
  "description": "프로젝트 설명",
  "status": "ACTIVE",
  "startDate": "2025-01-20T00:00:00"
}
```

**Response**
```json
{
  "id": 2,
  "name": "새 프로젝트",
  "description": "프로젝트 설명",
  "status": "ACTIVE",
  "startDate": "2025-01-20T00:00:00",
  "endDate": null,
  "createdAt": "2025-01-20T10:00:00",
  "updatedAt": "2025-01-20T10:00:00"
}
```

### PUT /projects/{id}
프로젝트 수정

**Parameters**
- `id` (path): 프로젝트 ID

**Request Body**
```json
{
  "name": "수정된 프로젝트",
  "description": "수정된 설명",
  "status": "COMPLETED",
  "startDate": "2025-01-01T00:00:00",
  "endDate": "2025-01-20T00:00:00"
}
```

### DELETE /projects/{id}
프로젝트 삭제

**Parameters**
- `id` (path): 프로젝트 ID

**Response**
```
204 No Content
```

## DevLogs API

### GET /logs
모든 개발 로그 조회

**Response**
```json
[
  {
    "id": 1,
    "projectId": 1,
    "title": "Spring Boot 프로젝트 초기 설정",
    "content": "Spring Boot 3.2.1과 MyBatis를 이용한 백엔드 프로젝트 초기 설정 완료",
    "tags": "Spring Boot,MyBatis,PostgreSQL",
    "logDate": "2025-01-20T00:00:00",
    "createdAt": "2025-01-20T10:00:00",
    "updatedAt": "2025-01-20T10:00:00"
  }
]
```

### GET /logs/{id}
특정 개발 로그 조회

**Parameters**
- `id` (path): 로그 ID

### POST /logs
새 개발 로그 작성

**Request Body**
```json
{
  "projectId": 1,
  "title": "새로운 기능 개발",
  "content": "오늘은 사용자 인증 기능을 구현했습니다.",
  "tags": "Authentication,Security",
  "logDate": "2025-01-20T00:00:00"
}
```

### PUT /logs/{id}
개발 로그 수정

**Parameters**
- `id` (path): 로그 ID

**Request Body**
```json
{
  "projectId": 1,
  "title": "수정된 제목",
  "content": "수정된 내용",
  "tags": "Updated,Tags",
  "logDate": "2025-01-20T00:00:00"
}
```

### DELETE /logs/{id}
개발 로그 삭제

**Parameters**
- `id` (path): 로그 ID

**Response**
```
204 No Content
```

## Status Codes

- `200 OK`: 성공
- `201 Created`: 생성 성공
- `204 No Content`: 삭제 성공
- `400 Bad Request`: 잘못된 요청
- `404 Not Found`: 리소스를 찾을 수 없음
- `500 Internal Server Error`: 서버 오류
