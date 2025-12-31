# CLAUDE.md - DevLog 프로젝트 가이드

> 이 문서는 Claude AI가 DevLog 프로젝트를 개발할 때 참고하는 핵심 가이드입니다.

## 목차
- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [코딩 컨벤션](#코딩-컨벤션)
- [데이터베이스 규칙](#데이터베이스-규칙)
- [API 설계 원칙](#api-설계-원칙)
- [프론트엔드 개발 가이드](#프론트엔드-개발-가이드)
- [브랜치 전략](#브랜치-전략)
- [테스트 전략](#테스트-전략)
- [중요 사항](#중요-사항)
- [참고 자료](#참고-자료)

---

## 프로젝트 개요

### 목적
**DevLog**는 개발자를 위한 일일 개발 로그 및 프로젝트 관리 시스템입니다.

### 핵심 기능
1. **일일 개발 로그 작성**
   - 날짜별 개발 활동 기록
   - 마크다운 지원
   - 태그 기반 분류

2. **프로젝트 관리**
   - 프로젝트별 로그 그룹화
   - 프로젝트 상태 관리 (ACTIVE, COMPLETED, ON_HOLD, ARCHIVED)
   - 프로젝트별 통계

3. **대시보드 및 분석**
   - 개발 활동 통계
   - 시각화된 차트
   - 주간/월간 리포트

### 사용자 시나리오
- 개발자가 매일 작업 내용을 기록
- 프로젝트별로 진행 상황 추적
- 과거 작업 내용 검색 및 참조
- 개발 패턴 분석

---

## 기술 스택

### 백엔드
- **Framework**: Spring Boot 3.2.1
- **Language**: Java 17
- **Persistence**: MyBatis 3.0.3
- **Database**: H2 (개발), PostgreSQL 15 (프로덕션)
- **Build Tool**: Maven 3.8+

### 프론트엔드
- **Framework**: React 18.2
- **Styling**: Tailwind CSS 3.4+
- **UI Components**: Custom (Glassmorphism 디자인)
- **Charts**: Recharts 2.10+
- **HTTP Client**: Axios 1.6+
- **Icons**: Lucide React 0.305+
- **Routing**: React Router DOM 6.21+

### 개발 환경
- **Node.js**: 18+
- **Java**: 17
- **IDE**: IntelliJ IDEA / VS Code

---

## 프로젝트 구조

### 전체 구조
```
devlog/
├── backend/              # Spring Boot 백엔드
├── frontend/             # React 프론트엔드
├── database/             # SQL 스크립트
├── docs/                 # 문서
│   ├── UI_UX_GUIDE.md   # UI/UX 개발 가이드 (상세)
│   ├── API.md           # API 명세
│   └── ...
├── README.md            # 프로젝트 소개
└── CLAUDE.md            # 이 파일
```

### 백엔드 구조
```
backend/src/main/java/com/vibecoding/devlog/
├── DevLogApplication.java           # 메인 애플리케이션
├── config/                          # 설정 클래스
│   ├── WebConfig.java              # CORS, 인터셉터 등
│   └── DatabaseConfig.java         # DB 설정 (필요시)
├── controller/                      # REST 컨트롤러
│   ├── HealthCheckController.java  # 헬스 체크
│   ├── DevLogController.java       # 로그 API
│   └── ProjectController.java      # 프로젝트 API
├── service/                         # 비즈니스 로직
│   ├── DevLogService.java
│   └── ProjectService.java
├── mapper/                          # MyBatis 매퍼 인터페이스
│   ├── DevLogMapper.java
│   └── ProjectMapper.java
├── domain/                          # 도메인 모델
│   ├── DevLog.java
│   └── Project.java
├── dto/                             # DTO 클래스
│   ├── request/
│   │   ├── DevLogCreateRequest.java
│   │   └── ProjectCreateRequest.java
│   └── response/
│       ├── DevLogResponse.java
│       └── ProjectResponse.java
└── exception/                       # 예외 처리
    ├── GlobalExceptionHandler.java
    └── ResourceNotFoundException.java
```

### 프론트엔드 구조
```
frontend/src/
├── api/                    # API 호출
│   ├── axios.js           # Axios 설정
│   ├── devlog.js          # DevLog API
│   └── project.js         # Project API
├── components/             # 재사용 컴포넌트
│   ├── Layout.jsx         # 레이아웃
│   ├── Logo.jsx           # 로고
│   ├── Card.jsx           # 카드 컴포넌트
│   └── Button.jsx         # 버튼 컴포넌트
├── pages/                  # 페이지 컴포넌트
│   ├── Dashboard.jsx      # 대시보드
│   ├── DevLogs.jsx        # 로그 목록
│   └── Projects.jsx       # 프로젝트 목록
├── hooks/                  # 커스텀 훅
│   ├── useDevLogs.js
│   └── useProjects.js
├── utils/                  # 유틸리티
│   ├── formatters.js      # 날짜, 텍스트 포맷
│   └── validators.js      # 유효성 검사
├── App.js                  # 메인 앱
└── index.js               # 엔트리 포인트
```

---

## 코딩 컨벤션

### Java/Spring Boot

#### 1. 네이밍 규칙
```java
// 클래스: PascalCase
public class DevLogService { }

// 메서드: camelCase, 동사로 시작
public DevLog createDevLog() { }
public List<DevLog> findAllDevLogs() { }

// 변수: camelCase
private String logTitle;
private Long projectId;

// 상수: UPPER_SNAKE_CASE
public static final String DEFAULT_STATUS = "ACTIVE";

// 패키지: 소문자, 단수형
com.vibecoding.devlog.service
```

#### 2. 레이어별 역할

**Controller Layer**
- HTTP 요청/응답 처리만 담당
- 비즈니스 로직 X
- DTO 사용 (도메인 객체 직접 반환 X)

```java
@RestController
@RequestMapping("/logs")
public class DevLogController {

    private final DevLogService devLogService;

    @GetMapping
    public ResponseEntity<List<DevLogResponse>> getAllDevLogs() {
        List<DevLog> logs = devLogService.findAll();
        List<DevLogResponse> responses = logs.stream()
            .map(DevLogResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<DevLogResponse> createDevLog(
            @Valid @RequestBody DevLogCreateRequest request) {
        DevLog created = devLogService.create(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(DevLogResponse.from(created));
    }
}
```

**Service Layer**
- 비즈니스 로직 구현
- 트랜잭션 관리
- 여러 Mapper 조합 가능

```java
@Service
@Transactional(readOnly = true)
public class DevLogService {

    private final DevLogMapper devLogMapper;
    private final ProjectMapper projectMapper;

    @Transactional
    public DevLog create(DevLog devLog) {
        // 비즈니스 로직
        validateDevLog(devLog);

        // 프로젝트 존재 확인
        if (devLog.getProjectId() != null) {
            Project project = projectMapper.findById(devLog.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        }

        devLogMapper.insert(devLog);
        return devLog;
    }

    private void validateDevLog(DevLog devLog) {
        if (devLog.getTitle() == null || devLog.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
    }
}
```

**Mapper Layer**
- SQL 실행만 담당
- 단순 CRUD

```java
@Mapper
public interface DevLogMapper {
    List<DevLog> findAll();
    Optional<DevLog> findById(Long id);
    void insert(DevLog devLog);
    void update(DevLog devLog);
    void delete(Long id);
}
```

#### 3. 예외 처리

```java
// 커스텀 예외
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

// 글로벌 예외 핸들러
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException e) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
```

#### 4. DTO 패턴

```java
// Request DTO
public record DevLogCreateRequest(
    @NotNull(message = "Project ID is required")
    Long projectId,

    @NotBlank(message = "Title is required")
    @Size(max = 500)
    String title,

    @NotBlank(message = "Content is required")
    String content,

    String tags,

    @NotNull(message = "Log date is required")
    LocalDateTime logDate
) {
    public DevLog toEntity() {
        DevLog devLog = new DevLog();
        devLog.setProjectId(projectId);
        devLog.setTitle(title);
        devLog.setContent(content);
        devLog.setTags(tags);
        devLog.setLogDate(logDate);
        return devLog;
    }
}

// Response DTO
public record DevLogResponse(
    Long id,
    Long projectId,
    String title,
    String content,
    String tags,
    LocalDateTime logDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static DevLogResponse from(DevLog devLog) {
        return new DevLogResponse(
            devLog.getId(),
            devLog.getProjectId(),
            devLog.getTitle(),
            devLog.getContent(),
            devLog.getTags(),
            devLog.getLogDate(),
            devLog.getCreatedAt(),
            devLog.getUpdatedAt()
        );
    }
}
```

### React/JavaScript

#### 1. 컴포넌트 작성 규칙

```jsx
// 함수형 컴포넌트 사용
// Props는 구조 분해
// PropTypes 또는 TypeScript 사용 권장

import React from 'react';
import PropTypes from 'prop-types';

const DevLogCard = ({ log, onEdit, onDelete }) => {
  const handleEdit = () => {
    onEdit(log.id);
  };

  return (
    <div className="glass rounded-2xl p-6 card-hover">
      <h3 className="text-xl font-semibold text-white mb-2">
        {log.title}
      </h3>
      <p className="text-white/80 mb-4">{log.content}</p>
      <div className="flex space-x-2">
        <button onClick={handleEdit}>편집</button>
        <button onClick={() => onDelete(log.id)}>삭제</button>
      </div>
    </div>
  );
};

DevLogCard.propTypes = {
  log: PropTypes.shape({
    id: PropTypes.number.isRequired,
    title: PropTypes.string.isRequired,
    content: PropTypes.string.isRequired,
  }).isRequired,
  onEdit: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default DevLogCard;
```

#### 2. 커스텀 훅 작성

```javascript
// hooks/useDevLogs.js
import { useState, useEffect } from 'react';
import { devLogApi } from '../api/devlog';

export const useDevLogs = () => {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchLogs = async () => {
    try {
      setLoading(true);
      const response = await devLogApi.getAll();
      setLogs(response.data);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const createLog = async (logData) => {
    try {
      const response = await devLogApi.create(logData);
      setLogs([response.data, ...logs]);
      return response.data;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  useEffect(() => {
    fetchLogs();
  }, []);

  return {
    logs,
    loading,
    error,
    createLog,
    refreshLogs: fetchLogs,
  };
};
```

#### 3. Tailwind CSS 사용 규칙

```jsx
// 유틸리티 클래스 우선 사용
// 자주 사용하는 조합은 컴포넌트로 분리
// 반응형 디자인 항상 고려

const Button = ({ children, variant = 'primary', size = 'md', ...props }) => {
  const baseClasses = 'rounded-xl font-medium transition-all duration-300';

  const variants = {
    primary: 'bg-gradient-to-r from-purple-500 to-blue-500 text-white hover:shadow-glow',
    secondary: 'glass text-white hover:bg-white/20',
    danger: 'bg-red-500 text-white hover:bg-red-600',
  };

  const sizes = {
    sm: 'px-3 py-1.5 text-sm',
    md: 'px-4 py-2 text-base',
    lg: 'px-6 py-3 text-lg',
  };

  return (
    <button
      className={`${baseClasses} ${variants[variant]} ${sizes[size]}`}
      {...props}
    >
      {children}
    </button>
  );
};
```

#### 4. API 호출 규칙

```javascript
// api/devlog.js
import axios from './axios';

export const devLogApi = {
  getAll: () => axios.get('/logs'),

  getById: (id) => axios.get(`/logs/${id}`),

  create: (data) => axios.post('/logs', data),

  update: (id, data) => axios.put(`/logs/${id}`, data),

  delete: (id) => axios.delete(`/logs/${id}`),

  // 프로젝트별 로그 조회
  getByProject: (projectId) => axios.get(`/logs?projectId=${projectId}`),

  // 날짜 범위로 조회
  getByDateRange: (startDate, endDate) =>
    axios.get(`/logs?startDate=${startDate}&endDate=${endDate}`),
};
```

---

## 데이터베이스 규칙

### 테이블 네이밍
- 소문자, 복수형 사용
- 언더스코어로 단어 구분
- 예: `dev_logs`, `projects`

### 컬럼 네이밍
- 소문자, 언더스코어로 구분
- 예: `project_id`, `created_at`, `log_date`

### 필수 컬럼
모든 테이블에 다음 컬럼 포함:
```sql
id BIGINT AUTO_INCREMENT PRIMARY KEY,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
```

### 인덱스 규칙
- 외래 키에는 항상 인덱스 생성
- 자주 검색하는 컬럼에 인덱스 추가
- 인덱스 네이밍: `idx_{table}_{column}`

```sql
CREATE INDEX idx_dev_logs_project_id ON dev_logs(project_id);
CREATE INDEX idx_dev_logs_log_date ON dev_logs(log_date);
```

### 외래 키 규칙
- ON DELETE CASCADE 사용 (하위 데이터 자동 삭제)
- 제약 조건 네이밍: `fk_{table}_{referenced_table}`

```sql
CONSTRAINT fk_dev_logs_projects
    FOREIGN KEY (project_id)
    REFERENCES projects(id)
    ON DELETE CASCADE
```

### MyBatis 매퍼 작성 규칙

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.vibecoding.devlog.mapper.DevLogMapper">

    <!-- ResultMap 정의 -->
    <resultMap id="devLogResultMap" type="DevLog">
        <id property="id" column="id"/>
        <result property="projectId" column="project_id"/>
        <result property="title" column="title"/>
        <result property="content" column="content"/>
        <result property="tags" column="tags"/>
        <result property="logDate" column="log_date"/>
        <result property="createdAt" column="created_at"/>
        <result property="updatedAt" column="updated_at"/>
    </resultMap>

    <!-- 동적 SQL 사용 -->
    <select id="findByCondition" resultMap="devLogResultMap">
        SELECT * FROM dev_logs
        <where>
            <if test="projectId != null">
                AND project_id = #{projectId}
            </if>
            <if test="startDate != null">
                AND log_date &gt;= #{startDate}
            </if>
            <if test="endDate != null">
                AND log_date &lt;= #{endDate}
            </if>
            <if test="tags != null and tags != ''">
                AND tags LIKE CONCAT('%', #{tags}, '%')
            </if>
        </where>
        ORDER BY log_date DESC, created_at DESC
    </select>

    <!-- useGeneratedKeys로 자동생성 ID 반환 -->
    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO dev_logs (
            project_id, title, content, tags, log_date,
            created_at, updated_at
        )
        VALUES (
            #{projectId}, #{title}, #{content}, #{tags}, #{logDate},
            NOW(), NOW()
        )
    </insert>

</mapper>
```

---

## API 설계 원칙

### RESTful 원칙 준수

```
GET    /api/logs              # 목록 조회
GET    /api/logs/{id}         # 상세 조회
POST   /api/logs              # 생성
PUT    /api/logs/{id}         # 전체 수정
PATCH  /api/logs/{id}         # 부분 수정
DELETE /api/logs/{id}         # 삭제

GET    /api/projects          # 프로젝트 목록
GET    /api/projects/{id}     # 프로젝트 상세
POST   /api/projects          # 프로젝트 생성
PUT    /api/projects/{id}     # 프로젝트 수정
DELETE /api/projects/{id}     # 프로젝트 삭제

# 하위 리소스 접근
GET    /api/projects/{id}/logs    # 특정 프로젝트의 로그 목록
```

### HTTP 상태 코드

```
200 OK                 # 성공
201 Created            # 생성 성공
204 No Content         # 삭제 성공 (응답 본문 없음)
400 Bad Request        # 잘못된 요청
404 Not Found          # 리소스 없음
500 Internal Error     # 서버 오류
```

### 요청/응답 형식

**요청 (POST /api/logs)**
```json
{
  "projectId": 1,
  "title": "Spring Boot 초기 설정",
  "content": "프로젝트 초기 설정을 완료했습니다.",
  "tags": "Spring Boot,Setup",
  "logDate": "2025-01-20T10:00:00"
}
```

**성공 응답 (201 Created)**
```json
{
  "id": 1,
  "projectId": 1,
  "title": "Spring Boot 초기 설정",
  "content": "프로젝트 초기 설정을 완료했습니다.",
  "tags": "Spring Boot,Setup",
  "logDate": "2025-01-20T10:00:00",
  "createdAt": "2025-01-20T10:00:00",
  "updatedAt": "2025-01-20T10:00:00"
}
```

**에러 응답 (400 Bad Request)**
```json
{
  "status": 400,
  "message": "Title is required",
  "timestamp": "2025-01-20T10:00:00"
}
```

### 페이징 및 정렬

```
GET /api/logs?page=1&size=20&sort=logDate,desc

# 응답
{
  "content": [...],
  "page": 1,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 필터링 및 검색

```
GET /api/logs?projectId=1
GET /api/logs?tags=Spring Boot
GET /api/logs?startDate=2025-01-01&endDate=2025-01-31
GET /api/logs?search=설정
```

---

## 프론트엔드 개발 가이드

> **주의**: 프론트엔드 UI/UX에 대한 상세한 가이드는 `docs/UI_UX_GUIDE.md`를 참고하세요. 여기서는 개발 원칙만 요약합니다.

### 개발 원칙

#### 디자인 시스템 준수
- 색상 시스템: 주색상 RGB(97, 61, 132) / #614184, Glassmorphism 디자인
- 타이포그래피: 상세한 규칙은 `docs/UI_UX_GUIDE.md` 참고
- 컴포넌트 재사용: 기존 컴포넌트 활용, 새 컴포넌트는 설계 문서 참고 후 구현

#### 상태 관리
- **간단한 상태**: `useState`, `useReducer`
- **전역 상태**: Context API 또는 Zustand
- **서버 상태**: React Query (향후 도입 권장)
- 상태 관리 패턴은 `docs/UI_UX_GUIDE.md`의 "State Management" 섹션 참고

#### 폼 처리
- 유효성 검사: 입력값 검증 필수
- 에러 메시지: 사용자 친화적 메시지 제공
- 접근성: label 요소 사용, 포커스 관리

#### 컴포넌트 개발
```jsx
// 컴포넌트 구조
import { useState } from 'react';

const MyComponent = ({ prop1, prop2, onAction }) => {
  const [state, setState] = useState(initialValue);

  const handleAction = (value) => {
    // 처리 로직
    onAction(value);
  };

  return (
    <div className="glass rounded-2xl p-6 card-hover">
      {/* 컴포넌트 JSX */}
    </div>
  );
};

export default MyComponent;
```

자세한 컴포넌트 가이드, 디자인 시스템, 반응형 디자인 등은 `docs/UI_UX_GUIDE.md`의 다음 섹션들을 참고하세요:
- 색상 시스템
- 타이포그래피
- 컴포넌트 가이드
- 반응형 디자인
- 애니메이션
- 접근성
- 개발 워크플로우

---

## 브랜치 전략

### Git Flow 간소화 버전

```
main              # 프로덕션 브랜치
  ├── develop     # 개발 브랜치
      ├── feature/user-authentication
      ├── feature/log-search
      └── bugfix/dashboard-stats
```

### 브랜치 네이밍
```
feature/{기능명}     # 새 기능
bugfix/{버그명}      # 버그 수정
hotfix/{긴급수정}    # 긴급 수정
refactor/{리팩토링}  # 리팩토링
```

### 커밋 메시지 규칙
```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
style: 코드 포맷팅 (기능 변경 없음)
refactor: 코드 리팩토링
test: 테스트 코드
chore: 빌드 설정 등

예시:
feat: Add DevLog creation API
fix: Fix dashboard statistics calculation
docs: Update API documentation
```

---

## 테스트 전략

### 백엔드 테스트

#### 1. Unit Test (Service Layer)
```java
@ExtendWith(MockitoExtension.class)
class DevLogServiceTest {

    @Mock
    private DevLogMapper devLogMapper;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private DevLogService devLogService;

    @Test
    void createDevLog_Success() {
        // given
        DevLog devLog = createTestDevLog();
        when(projectMapper.findById(1L))
            .thenReturn(Optional.of(createTestProject()));

        // when
        DevLog created = devLogService.create(devLog);

        // then
        verify(devLogMapper).insert(devLog);
        assertNotNull(created);
    }
}
```

#### 2. Integration Test (Controller)
```java
@SpringBootTest
@AutoConfigureMockMvc
class DevLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllDevLogs_ReturnsListOfLogs() throws Exception {
        mockMvc.perform(get("/api/logs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}
```

### 프론트엔드 테스트

#### 1. Component Test (React Testing Library)
```javascript
import { render, screen, fireEvent } from '@testing-library/react';
import DevLogCard from './DevLogCard';

test('renders log title and content', () => {
  const log = {
    id: 1,
    title: 'Test Log',
    content: 'Test Content',
  };

  render(<DevLogCard log={log} onEdit={() => {}} onDelete={() => {}} />);

  expect(screen.getByText('Test Log')).toBeInTheDocument();
  expect(screen.getByText('Test Content')).toBeInTheDocument();
});
```

---

## 중요 사항

### 보안
1. **SQL Injection 방지**
   - MyBatis PreparedStatement 사용
   - 사용자 입력 직접 쿼리 삽입 금지

2. **XSS 방지**
   - React는 기본적으로 XSS 방지
   - dangerouslySetInnerHTML 사용 최소화

3. **CORS 설정**
   - 개발: localhost:3000 허용
   - 프로덕션: 특정 도메인만 허용

4. **환경 변수 관리**
   - .env 파일 사용
   - .gitignore에 추가
   - 민감 정보 절대 하드코딩 금지

### 성능 최적화

#### 백엔드
1. **데이터베이스**
   - 적절한 인덱스 사용
   - N+1 문제 주의 (조인 활용)
   - 페이징 구현

2. **캐싱**
   - 자주 조회되는 데이터 캐싱 고려
   - Spring Cache 활용

#### 프론트엔드
1. **번들 크기 최적화**
   - Code Splitting
   - Lazy Loading
   - 불필요한 라이브러리 제거

2. **렌더링 최적화**
   - React.memo 활용
   - useMemo, useCallback 적절히 사용
   - 가상 스크롤링 (긴 목록)

### 에러 처리
1. **백엔드**
   - GlobalExceptionHandler로 중앙 집중식 처리
   - 의미 있는 에러 메시지
   - 적절한 HTTP 상태 코드

2. **프론트엔드**
   - try-catch로 API 에러 처리
   - 사용자 친화적 에러 메시지
   - 에러 바운더리 사용

### 로깅
```java
// 백엔드
@Slf4j
public class DevLogService {
    public DevLog create(DevLog devLog) {
        log.info("Creating dev log: {}", devLog.getTitle());
        try {
            // ...
        } catch (Exception e) {
            log.error("Failed to create dev log", e);
            throw e;
        }
    }
}
```

### 문서화
1. **API 문서**
   - docs/API.md 최신 상태 유지
   - 예제 요청/응답 포함

2. **코드 주석**
   - 복잡한 로직에만 주석
   - 왜(Why) 설명, 무엇(What)은 코드로

3. **README 업데이트**
   - 설치 방법
   - 실행 방법
   - 주요 기능

---

## 개발 워크플로우

### 1. 새 기능 개발 시

```bash
# 1. develop 브랜치에서 feature 브랜치 생성
git checkout develop
git pull origin develop
git checkout -b feature/log-search

# 2. 개발 진행
# - 백엔드 API 먼저 개발
# - 단위 테스트 작성
# - 프론트엔드 UI 개발
# - 통합 테스트

# 3. 커밋
git add .
git commit -m "feat: Add log search functionality"

# 4. Push 및 PR 생성
git push origin feature/log-search
# GitHub에서 Pull Request 생성

# 5. 코드 리뷰 후 머지
# 6. feature 브랜치 삭제
git branch -d feature/log-search
```

### 2. 버그 수정 시

```bash
git checkout develop
git checkout -b bugfix/dashboard-stats
# 수정 작업
git commit -m "fix: Correct dashboard statistics calculation"
git push origin bugfix/dashboard-stats
```

### 3. 긴급 수정 시

```bash
git checkout main
git checkout -b hotfix/critical-bug
# 수정 작업
git commit -m "hotfix: Fix critical security issue"
# main과 develop 둘 다 머지
```

---

## 체크리스트

### 코드 작성 전
- [ ] 요구사항 명확히 이해
- [ ] 유사 기능 구현 사례 확인
- [ ] 테이블 스키마 확인 (DB 변경 필요 시)

### 코드 작성 중
- [ ] 네이밍 컨벤션 준수
- [ ] 레이어별 역할 준수
- [ ] DTO 사용 (Controller에서 도메인 직접 반환 X)
- [ ] 예외 처리 적절히 구현
- [ ] 로깅 추가

### 코드 작성 후
- [ ] 단위 테스트 작성
- [ ] API 테스트 (Postman, curl)
- [ ] UI 동작 확인
- [ ] 에러 케이스 확인
- [ ] 문서 업데이트

### PR 생성 전
- [ ] 불필요한 코드 제거
- [ ] console.log 제거
- [ ] 주석 정리
- [ ] 커밋 메시지 확인
- [ ] 충돌 해결

---

## 참고 자료

### 공식 문서
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MyBatis Documentation](https://mybatis.org/mybatis-3/)
- [React Documentation](https://react.dev/)
- [Tailwind CSS Documentation](https://tailwindcss.com/)

### 프로젝트 문서
- `README.md`: 프로젝트 개요 및 시작 가이드
- `CLAUDE.md`: Claude AI 개발 가이드 (이 문서)
- `docs/UI_UX_GUIDE.md`: UI/UX 개발자 가이드 (색상, 타이포그래피, 컴포넌트, 반응형 디자인 등)
- `docs/SETUP.md`: 상세 설치 가이드
- `docs/API.md`: API 명세서
- `docs/ARCHITECTURE.md`: 아키텍처 문서
- `database/README.md`: 데이터베이스 가이드

---

## 최근 버그 수정 및 개선 사항

### 2025-12-31 업데이트 (v1.1.0)

#### 버그 수정

1. **대시보드 통계 0으로 표시되는 문제 (Critical)**
   - **문제**: PostgreSQL이 컬럼명을 소문자로 반환하여 통계가 0으로 표시됨
   - **원인**: MyBatis `map-underscore-to-camel-case` 설정은 Entity 매핑에만 적용되고, `resultType="map"`에는 적용되지 않음
   - **해결**: StatisticsMapper.xml의 모든 통계 쿼리에서 컬럼 별칭을 소문자로 변경하고 `::integer` 타입 캐스팅 추가
   - **파일**: `backend/src/main/resources/mapper/StatisticsMapper.xml`, `backend/src/main/java/com/vibecoding/devlog/service/StatisticsService.java`

2. **차트 툴팁 텍스트 보이지 않는 문제**
   - **문제**: Recharts 툴팁이 검은 배경에 검은 텍스트로 표시되어 보이지 않음
   - **해결**: 모든 Tooltip 컴포넌트에 `itemStyle={{ color: '#fff' }}` 및 `labelStyle={{ color: '#fff' }}` 추가
   - **파일**: `frontend/src/pages/Statistics.jsx` (4개 툴팁), `frontend/src/pages/Dashboard.jsx` (1개 툴팁)

3. **파이 차트 레이블 보이지 않는 문제**
   - **문제**: 파이 차트의 레이블이 어두운 배경에 검은색으로 표시됨
   - **해결**: 커스텀 라벨 렌더링 함수 구현, 흰색 텍스트 + text-shadow 적용
   - **파일**: `frontend/src/pages/Statistics.jsx`

4. **로그 편집 시 TypeError 발생**
   - **문제**: `Cannot read properties of undefined (reading 'substring')`
   - **원인**: 백엔드가 `startTime`/`endTime`을 "HH:mm:ss" 형식으로 반환하는데, 프론트엔드가 ISO datetime 형식을 기대
   - **해결**: 두 가지 형식 모두 처리하도록 파싱 로직 수정
   - **파일**: `frontend/src/pages/LogForm.jsx:104-105`

5. **달력 컴포넌트가 다른 요소 뒤에 숨는 문제 (Critical)**
   - **문제**: DateNavigator 달력이 다른 콘텐츠 뒤에 숨어 사용 불가
   - **원인**: `.glass` 클래스 또는 부모 컨테이너의 `overflow` 또는 스택 컨텍스트 문제로 z-index만으로 해결 불가
   - **해결**: React Portal을 사용하여 달력을 `document.body`에 렌더링, `fixed` 포지셔닝 + `getBoundingClientRect()`로 위치 계산
   - **파일**: `frontend/src/components/DateNavigator.jsx`

6. **달력 요일/날짜 레이블 보이지 않는 문제**
   - **문제**: Glassmorphism 배경에서 흰색 텍스트가 잘 보이지 않음
   - **해결**: 텍스트 색상을 완전 불투명 흰색으로 변경, `font-bold` 적용, `text-shadow` 추가
   - **파일**: `frontend/src/components/DateNavigator.jsx`

7. **프로젝트 드롭다운 배경색 불일치**
   - **문제**: 검은색 배경이 전체 디자인과 어울리지 않음
   - **해결**: `rgba(30, 30, 50, 0.95)`로 변경하여 glassmorphism 테마와 일치
   - **파일**: `frontend/src/pages/LogForm.jsx`

#### 개선 사항

1. **PostgreSQL 데이터 타입 명시**
   - 모든 통계 쿼리에 `::integer` 타입 캐스팅 추가하여 명시적 타입 보장
   - NULL 값 처리를 위한 `COALESCE()` 함수 사용

2. **브라우저 캐싱 설정 최적화**
   - 개발 환경에서 JS/CSS 파일 캐싱 비활성화
   - 이미지/폰트만 1년 캐싱 적용
   - **파일**: `frontend/nginx.conf`

3. **프론트엔드 UI 개선**
   - 차트 가시성 향상 (툴팁, 레이블 모두 흰색 + text-shadow)
   - 달력 사용성 개선 (Portal 패턴으로 항상 최상위 표시)
   - 프로젝트 드롭다운 디자인 통일 (배경색 변경: #614184)

4. **UI/UX 개발자 가이드 작성**
   - 종합 UI/UX 개발 가이드 문서 작성 (`docs/UI_UX_GUIDE.md`)
   - 색상 시스템, 타이포그래피, 컴포넌트, 레이아웃, 반응형 디자인, 애니메이션, 접근성 등 상세 가이드 포함
   - CLAUDE.md 프론트엔드 섹션을 간소화하고 UI_UX_GUIDE.md로 참조 통일

#### 알려진 이슈

현재 알려진 이슈 없음.

#### 다음 개선 계획

1. **인증/인가 시스템**
   - JWT 기반 사용자 인증 구현
   - 사용자별 프로젝트 및 로그 분리

2. **알림 기능**
   - 일일 로그 작성 리마인더
   - 프로젝트 마감일 알림

3. **데이터 내보내기**
   - PDF 리포트 생성
   - CSV 내보내기

4. **검색 기능 강화**
   - 전문 검색 (Full-text search)
   - 고급 필터 (태그 조합, 날짜 범위, 감정 상태 등)

#### 참고 사항

- **PostgreSQL 컬럼명 규칙**:
  - PostgreSQL은 컬럼명을 소문자로 반환
  - MyBatis에서 `resultType="map"` 사용 시 소문자 별칭 필수
  - `map-underscore-to-camel-case: true`는 Entity 매핑에만 적용

- **React Portal 사용 시**:
  - `createPortal(children, container)`로 DOM 계층 외부에 렌더링
  - `fixed` 포지셔닝 + `getBoundingClientRect()`로 위치 계산
  - 상태 관리에 `useRef` 사용하여 버튼 참조 유지

- **Recharts 스타일링**:
  - `contentStyle`: 툴팁 컨테이너 스타일
  - `itemStyle`: 각 항목 텍스트 스타일
  - `labelStyle`: 라벨 텍스트 스타일
  - 모두 명시해야 정확한 색상 적용

---

## 마지막으로

이 문서는 **살아있는 문서(Living Document)**입니다.
프로젝트가 진행되면서 새로운 규칙이나 패턴이 생기면 이 문서를 업데이트하세요.

**질문이나 개선 사항이 있다면 주저하지 말고 제안하세요!**

---

*Last Updated: 2025-12-31*
*Version: 1.1.0*
