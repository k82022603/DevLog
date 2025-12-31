# DevLog API 명세서

> 이 문서는 DevLog REST API의 간단한 개요입니다.
> **전체 API 상세 내용은 [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)를 참조하세요.**

---

## 📌 API 개요

### Base URL
```
http://localhost:8080/api
```

### API 버전
- 현재 버전: v1.0
- 최종 업데이트: 2025-12-31

### 총 엔드포인트
- **전체 엔드포인트**: 37개
- 상세 정보: [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

---

## 🔗 빠른 링크

| 카테고리 | 엔드포인트 수 | 상세 문서 |
|---------|-------------|---------|
| Health Check | 1개 | [API_DOCUMENTATION.md#health-check](./API_DOCUMENTATION.md#health-check) |
| Projects API | 7개 | [API_DOCUMENTATION.md#projects-api](./API_DOCUMENTATION.md#projects-api) |
| DevLogs API | 12개 | [API_DOCUMENTATION.md#devlogs-api](./API_DOCUMENTATION.md#devlogs-api) |
| Statistics API | 14개 | [API_DOCUMENTATION.md#statistics-api](./API_DOCUMENTATION.md#statistics-api) |
| Tags API | 3개 | [API_DOCUMENTATION.md#tags-api](./API_DOCUMENTATION.md#tags-api) |

---

## 🚀 빠른 시작

### Health Check
```bash
curl http://localhost:8080/api/health
```

**Response** (200 OK):
```json
{
  "status": "OK",
  "message": "DevLog API is running",
  "timestamp": 1234567890
}
```

### 프로젝트 목록 조회
```bash
curl http://localhost:8080/api/projects
```

**Response** (200 OK):
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

### 개발 로그 목록 조회
```bash
curl http://localhost:8080/api/logs
```

**Response** (200 OK):
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

---

## 📖 상세 문서

### 전체 API 명세
더 상세한 API 문서는 **[API_DOCUMENTATION.md](./API_DOCUMENTATION.md)**를 참조하세요.

### 포함 내용
- 모든 37개 엔드포인트의 상세 설명
- 요청/응답 예제
- 에러 처리
- 데이터 모델 정의

---

## 🔗 관련 문서

- **API 상세 명세**: [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
- **아키텍처**: [ARCHITECTURE.md](./ARCHITECTURE.md)
- **설치 가이드**: [SETUP.md](./SETUP.md)
- **UI/UX 가이드**: [UI_UX_GUIDE.md](./UI_UX_GUIDE.md)
- **문서 네비게이션**: [README.md](./README.md)

---

## 🧪 API 테스트

### cURL로 테스트
```bash
# Health Check
curl -X GET http://localhost:8080/api/health

# 프로젝트 목록
curl -X GET http://localhost:8080/api/projects

# 개발 로그 목록
curl -X GET http://localhost:8080/api/logs
```

### Postman으로 테스트
1. Postman 앱 열기
2. "New Request" 클릭
3. URL 입력: `http://localhost:8080/api/health`
4. "Send" 클릭

---

## 📝 API 개선 이력

| 버전 | 날짜 | 변경사항 |
|------|------|---------|
| 1.0 | 2025-12-31 | API 명세서 작성 (37개 엔드포인트) |

---

## 📞 문의 및 지원

- **버그 리포트**: [GitHub Issues](https://github.com/k82022603/DevLog/issues)
- **기술 문의**: [GitHub Discussions](https://github.com/k82022603/DevLog/discussions)

---

*Last Updated: 2025-12-31*
*DevLog API v1.0*
