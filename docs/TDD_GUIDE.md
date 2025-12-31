# TDD 개발자 가이드 (Test-Driven Development Guide)

> 이 가이드는 DevLog 프로젝트에서 TDD 방식으로 백엔드를 개발하기 위한 실용적인 지침입니다.

## 목차
- [TDD란 무엇인가?](#tdd란-무엇인가)
- [왜 TDD를 사용하는가?](#왜-tdd를-사용하는가)
- [Red-Green-Refactor 사이클](#red-green-refactor-사이클)
- [테스트 작성 원칙](#테스트-작성-원칙)
- [테스트 구조 (AAA 패턴)](#테스트-구조-aaa-패턴)
- [Mock vs 실제 객체](#mock-vs-실제-객체)
- [Unit Test vs Integration Test](#unit-test-vs-integration-test)
- [JUnit 5 필수 개념](#junit-5-필수-개념)
- [Mockito 패턴](#mockito-패턴)
- [AssertJ 사용법](#assertj-사용법)
- [DevLog 테스트 사례](#devlog-테스트-사례)
- [테스트 네이밍 규칙](#테스트-네이밍-규칙)
- [코드 커버리지](#코드-커버리지)
- [일반적인 함정과 해결책](#일반적인-함정과-해결책)
- [체크리스트](#체크리스트)

---

## TDD란 무엇인가?

**Test-Driven Development (TDD)**는 **테스트를 먼저 작성하고, 그 테스트를 통과하는 코드를 작성하는** 개발 방식입니다.

### 전통적 개발 vs TDD 개발

```
❌ 전통적 개발 순서:
코드 작성 → 테스트 작성 → 버그 발견 → 수정

✅ TDD 개발 순서:
테스트 작성 → 코드 작성 → 리팩토링 → 다음 테스트
```

### TDD의 3가지 핵심 규칙

1. **실패하는 테스트가 없으면 코드를 작성하지 않는다**
2. **컴파일되지 않을 정도로만 테스트를 작성한다**
3. **실패하는 테스트를 통과하기 위해서만 코드를 작성한다**

---

## 왜 TDD를 사용하는가?

### TDD의 5가지 핵심 이점

#### 1. 🐛 버그 감소
```
전통 개발: 버그 발견 후 수정 (비용 크다)
TDD: 작성 중 버그 예방 (비용 작다)
```

#### 2. 📋 명확한 요구사항 정의
테스트를 먼저 작성하면 "무엇을 구현해야 하는가"가 명확해집니다.

```java
// 테스트를 작성하면서 요구사항을 명확히 함
@Test
@DisplayName("프로젝트 생성 시 상태는 ACTIVE로 기본값 설정")
void createProject_DefaultStatusIsActive() {
    // 요구사항: 프로젝트 생성 시 자동으로 ACTIVE 상태가 되어야 함
    Project newProject = new Project();
    newProject.setName("새 프로젝트");

    Project created = projectService.create(newProject);

    assertThat(created.getStatus()).isEqualTo("ACTIVE");
}
```

#### 3. 🔒 회귀 버그 방지
기존 기능 수정 후 모든 테스트를 실행하여 다른 기능이 깨지지 않았는지 확인합니다.

#### 4. 📚 문서 역할
테스트는 "어떻게 이 클래스를 사용하는가"에 대한 최고의 문서입니다.

```java
// 이 테스트를 읽으면 ProjectService의 사용 방법이 명확함
@Test
@DisplayName("존재하지 않는 프로젝트 조회 시 빈 Optional 반환")
void findById_ReturnsEmptyOptional_WhenNotFound() {
    when(projectMapper.findById(999L)).thenReturn(null);

    Optional<Project> result = projectService.findById(999L);

    assertThat(result).isEmpty();
}
```

#### 5. 🏗️ 설계 개선
테스트 가능한 코드를 먼저 고민하면 자동으로 좋은 설계가 됩니다.

---

## Red-Green-Refactor 사이클

TDD의 가장 핵심적인 패턴은 **Red-Green-Refactor** 사이클입니다.

```
┌──────────────────────────────────────────────────────┐
│                                                      │
│         RED (실패)                                    │
│  ┌────────────────────────────────┐                 │
│  │ 1. 실패하는 테스트 작성         │                 │
│  │ 2. 테스트 실행 (빨간색 표시)   │                 │
│  └────────────────────────────────┘                 │
│         ↓                                            │
│  GREEN (성공)                                        │
│  ┌────────────────────────────────┐                 │
│  │ 1. 테스트 통과 코드 작성        │                 │
│  │ 2. 테스트 실행 (초록색 표시)   │                 │
│  └────────────────────────────────┘                 │
│         ↓                                            │
│  REFACTOR (개선)                                     │
│  ┌────────────────────────────────┐                 │
│  │ 1. 코드 정리 및 최적화          │                 │
│  │ 2. 테스트는 여전히 통과        │                 │
│  └────────────────────────────────┘                 │
│         ↓                                            │
│    다음 테스트로 반복...                              │
│                                                      │
└──────────────────────────────────────────────────────┘
```

### 단계별 예시: 프로젝트 생성 기능

#### 단계 1️⃣ RED - 실패하는 테스트 작성

```java
@Test
@DisplayName("프로젝트 생성 성공 - 유효한 데이터")
void createProject_Success() {
    // Given
    Project newProject = new Project();
    newProject.setName("새 프로젝트");
    newProject.setDescription("설명");

    doNothing().when(projectMapper).insert(any(Project.class));

    // When
    Project result = projectService.create(newProject);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("새 프로젝트");
    assertThat(result.getStatus()).isEqualTo("ACTIVE");
    verify(projectMapper, times(1)).insert(newProject);
}
```

**결과**: ❌ 테스트 실패 (아직 create() 메서드 구현 안됨)

#### 단계 2️⃣ GREEN - 최소한의 코드로 테스트 통과

```java
@Service
@Transactional
public class ProjectService {

    private final ProjectMapper projectMapper;

    public Project create(Project project) {
        // 최소한의 코드로 테스트 통과
        project.setStatus("ACTIVE");
        project.setProgress(0);
        projectMapper.insert(project);
        return project;
    }
}
```

**결과**: ✅ 테스트 성공

#### 단계 3️⃣ REFACTOR - 코드 개선 (요구사항이 더 있다면)

```java
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectMapper projectMapper;
    private static final String DEFAULT_STATUS = "ACTIVE";
    private static final int DEFAULT_PROGRESS = 0;

    @Transactional
    public Project create(Project project) {
        // 유효성 검사 추가
        validateProjectName(project.getName());

        // 기본값 설정
        project.setStatus(DEFAULT_STATUS);
        project.setProgress(DEFAULT_PROGRESS);

        // DB 저장
        projectMapper.insert(project);

        return project;
    }

    private void validateProjectName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("프로젝트 이름은 필수입니다");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("프로젝트 이름은 100자를 초과할 수 없습니다");
        }
    }
}
```

---

## 테스트 작성 원칙

### 1. 한 번에 하나의 행동만 테스트

```java
// ❌ 나쁜 예: 여러 행동을 한 번에 테스트
@Test
void createProject() {
    Project newProject = new Project();
    newProject.setName("새 프로젝트");

    Project created = projectService.create(newProject);

    assertThat(created).isNotNull();
    assertThat(created.getName()).isEqualTo("새 프로젝트");
    assertThat(created.getStatus()).isEqualTo("ACTIVE");
    assertThat(created.getProgress()).isEqualTo(0);
    assertThat(created.getId()).isNotNull();
    // ... 너무 많은 assertion
}

// ✅ 좋은 예: 한 가지 행동만 테스트
@Test
@DisplayName("프로젝트 생성 시 상태는 ACTIVE로 기본값 설정")
void createProject_DefaultStatusIsActive() {
    Project newProject = new Project();
    newProject.setName("새 프로젝트");

    Project created = projectService.create(newProject);

    assertThat(created.getStatus()).isEqualTo("ACTIVE");
}
```

### 2. 테스트는 독립적이어야 함

```java
// ❌ 나쁜 예: 테스트들이 순서에 의존
class ProjectServiceTest {
    private static Project createdProject;

    @Test
    void createProject() {
        createdProject = projectService.create(new Project());
    }

    @Test
    void updateProject() {
        // createProject() 실행 후에만 작동 (의존성!)
        projectService.update(createdProject.getId(), ...);
    }
}

// ✅ 좋은 예: 각 테스트가 독립적
class ProjectServiceTest {

    @Test
    void createProject() {
        Project newProject = new Project();
        Project created = projectService.create(newProject);
        assertThat(created).isNotNull();
    }

    @Test
    void updateProject() {
        // 이 테스트는 이전 테스트에 의존하지 않음
        when(projectMapper.findById(1L)).thenReturn(testProject);
        projectService.update(1L, updateData);
        verify(projectMapper).update(any());
    }
}
```

### 3. 테스트는 결정적이어야 함

```java
// ❌ 나쁜 예: 결과가 환경에 따라 달라짐
@Test
void findProjectsByDate() {
    // 현재 날짜를 사용하므로, 내일 실행하면 다른 결과가 나옴
    LocalDateTime today = LocalDateTime.now();
    List<Project> projects = projectService.findByDate(today);
    assertThat(projects).hasSize(3); // 오늘 3개면, 내일엔 다를 수 있음
}

// ✅ 좋은 예: 고정된 날짜 사용
@Test
void findProjectsByDate() {
    LocalDateTime fixedDate = LocalDateTime.of(2025, 1, 20, 10, 0, 0);
    when(projectMapper.findByDate(fixedDate)).thenReturn(testProjects);

    List<Project> projects = projectService.findByDate(fixedDate);

    assertThat(projects).hasSize(1);
}
```

### 4. 테스트 이름은 의도를 명확히 표현

```java
// ❌ 나쁜 예: 이름만 봐서는 뭘 테스트하는지 모름
@Test
void test1() { }

@Test
void createProjectTest() { }

// ✅ 좋은 예: 이름과 @DisplayName으로 의도를 명확히
@Test
@DisplayName("프로젝트 생성 실패 - 이름이 빈 문자열인 경우")
void createProject_Fail_EmptyName() {
    // 어떤 상황(Empty Name)에서
    // 무엇(프로젝트 생성)을
    // 기대하는가(실패)
}
```

---

## 테스트 구조 (AAA 패턴)

모든 테스트는 **Arrange-Act-Assert (AAA)** 패턴을 따릅니다.

```
┌─────────────────────────────────────────────┐
│          AAA (3A) 패턴                      │
├─────────────────────────────────────────────┤
│                                             │
│  1️⃣ ARRANGE (준비)                          │
│     - 테스트 데이터 생성                    │
│     - Mock 설정                             │
│     - 테스트 전제 조건 설정                 │
│                                             │
│  2️⃣ ACT (실행)                              │
│     - 테스트할 기능 호출                    │
│     - 하나의 메서드만 호출                  │
│                                             │
│  3️⃣ ASSERT (검증)                           │
│     - 결과 검증                             │
│     - 예상 동작 확인                        │
│                                             │
└─────────────────────────────────────────────┘
```

### 실제 예시

```java
@Test
@DisplayName("프로젝트 생성 성공")
void createProject_Success() {
    // ====== ARRANGE (준비) ======
    Project newProject = new Project();
    newProject.setName("새 프로젝트");
    newProject.setDescription("설명");

    doNothing().when(projectMapper).insert(any(Project.class));

    // ====== ACT (실행) ======
    Project result = projectService.create(newProject);

    // ====== ASSERT (검증) ======
    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("새 프로젝트");
    assertThat(result.getStatus()).isEqualTo("ACTIVE");
    verify(projectMapper, times(1)).insert(newProject);
}
```

### 또 다른 예시: 실패 케이스

```java
@Test
@DisplayName("프로젝트 생성 실패 - 프로젝트 이름이 빈 문자열")
void createProject_Fail_EmptyName() {
    // ====== ARRANGE ======
    Project invalidProject = new Project();
    invalidProject.setName("   "); // 빈 문자열

    // ====== ACT & ASSERT ======
    assertThatThrownBy(() -> projectService.create(invalidProject))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("프로젝트 이름은 필수입니다");

    verify(projectMapper, never()).insert(any());
}
```

---

## Mock vs 실제 객체

### Mock 객체란?

Mock은 **실제 객체의 동작을 시뮬레이션**하는 테스트용 객체입니다.

```
┌──────────────────────────────────────────────┐
│           테스트할 코드와 의존성              │
├──────────────────────────────────────────────┤
│                                              │
│  ┌─────────────────────────┐                │
│  │  ProjectService         │                │
│  │ (테스트할 코드)         │                │
│  └──────────┬──────────────┘                │
│             │ 의존                          │
│             ↓                               │
│  ┌─────────────────────────┐                │
│  │  ProjectMapper (Mock)   │                │
│  │ (실제가 아닌 흉내)      │                │
│  └─────────────────────────┘                │
│                                              │
└──────────────────────────────────────────────┘
```

### 언제 Mock을 사용하는가?

```java
// ✅ Mock을 사용해야 하는 경우

// 1. 데이터베이스 의존성
@Test
void createProject() {
    // Mock: 실제 DB 대신 가짜 Mapper 사용
    when(projectMapper.insert(any())).thenReturn(true);
    projectService.create(newProject);
    verify(projectMapper).insert(any());
}

// 2. 외부 API 의존성
@Test
void importFromExternalAPI() {
    // Mock: 외부 API 대신 고정된 응답 반환
    when(externalApiClient.getProjects()).thenReturn(mockProjects);
    List<Project> projects = projectService.importProjects();
    assertThat(projects).hasSize(3);
}

// 3. 느린 작업
@Test
void generateReport() {
    // Mock: 실제 PDF 생성 대신 가짜 결과 반환
    when(pdfGenerator.generate(any())).thenReturn("mock.pdf");
    String result = reportService.generateReport(projectId);
    assertThat(result).isEqualTo("mock.pdf");
}

// ❌ Mock을 사용하지 말아야 하는 경우

// 1. 테스트할 핵심 로직
@Test
void calculateProjectProgress() {
    // Real: 실제 계산 로직을 테스트해야 함
    Project project = new Project();
    project.setProgress(50);

    int result = projectService.calculateProgress(project);
    assertThat(result).isEqualTo(50);
}

// 2. 간단한 유틸리티
@Test
void formatDate() {
    // Real: 실제 포맷팅 로직을 테스트
    LocalDate date = LocalDate.of(2025, 1, 20);
    String result = dateFormatter.format(date);
    assertThat(result).isEqualTo("2025-01-20");
}
```

### Mock 설정 패턴

```java
class ProjectServiceTest {

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        // Mock 객체들 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void example() {
        // 패턴 1: 값을 반환하는 Mock
        when(projectMapper.findById(1L)).thenReturn(testProject);

        // 패턴 2: 예외를 throw하는 Mock
        when(projectMapper.findById(999L)).thenThrow(
            new ResourceNotFoundException("Not found")
        );

        // 패턴 3: void 메서드를 Mock
        doNothing().when(projectMapper).delete(1L);

        // 패턴 4: 여러 번 호출될 때 다른 값 반환
        when(projectMapper.findAll())
            .thenReturn(emptyList())
            .thenReturn(listWithOneProject());
    }
}
```

---

## Unit Test vs Integration Test

### Unit Test (단위 테스트)

**정의**: 하나의 클래스/메서드를 **격리하여** 테스트합니다.

```java
@ExtendWith(MockitoExtension.class)  // ← Unit Test의 표시
@DisplayName("ProjectService 단위 테스트")
class ProjectServiceTest {

    @Mock
    private ProjectMapper projectMapper;  // ← 의존성을 Mock으로 격리

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProject_Success() {
        // 준비
        Project newProject = new Project();
        newProject.setName("새 프로젝트");
        when(projectMapper.insert(any())).thenReturn(true);

        // 실행
        Project result = projectService.create(newProject);

        // 검증
        assertThat(result.getName()).isEqualTo("새 프로젝트");
        verify(projectMapper, times(1)).insert(any());
    }
}
```

**특징**:
- ✅ 빠름 (Mock 사용)
- ✅ 명확함 (한 가지만 테스트)
- ✅ 격리됨 (다른 클래스 영향 없음)
- ❌ 실제 동작과 다를 수 있음

### Integration Test (통합 테스트)

**정의**: 여러 클래스의 **상호작용**을 함께 테스트합니다.

```java
@SpringBootTest  // ← Integration Test의 표시
@AutoConfigureMockMvc
@DisplayName("ProjectController 통합 테스트")
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;  // ← Spring 컨텍스트 사용

    @MockBean
    private ProjectService projectService;

    @Test
    @DisplayName("GET /api/projects - 프로젝트 목록 조회")
    void getAllProjects_Success() throws Exception {
        // 준비
        List<Project> projects = Arrays.asList(testProject);
        when(projectService.findAll()).thenReturn(projects);

        // 실행 & 검증
        mockMvc.perform(get("/api/projects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("DevLog"));
    }
}
```

**특징**:
- ✅ 실제와 가까움
- ✅ 통합 검증 가능
- ❌ 느림
- ❌ 복잡함

### 테스트 피라미드

```
        △
       /|\
      / | \
     /  |  \  10% - E2E Test (느림, 비용 큼)
    /   |   \       (실제 앱 전체)
   / 20%|    \
  /     |     \ - Integration Test
 / Unit |      \ (컨트롤러+서비스)
/______Test_____
    70%         - Unit Test (빠름, 많음)
                (단일 메서드)
```

DevLog 테스트 전략:
- **70%**: Unit Test (ProjectServiceTest, DevLogServiceTest, TechTagServiceTest)
- **20%**: Integration Test (Controller 테스트)
- **10%**: E2E Test (전체 플로우 테스트)

---

## JUnit 5 필수 개념

### 1. 기본 애노테이션

```java
@ExtendWith(MockitoExtension.class)
class ExampleTest {

    @Test  // ← 이 메서드가 테스트 메서드임을 표시
    @DisplayName("프로젝트 생성 성공")  // ← 테스트 이름 (한글 가능)
    void createProject_Success() {
        // 테스트 코드
    }

    @BeforeEach  // ← 각 테스트 전에 실행
    void setUp() {
        // 테스트 데이터 초기화
        testProject = new Project();
        testProject.setId(1L);
    }

    @AfterEach  // ← 각 테스트 후에 실행
    void tearDown() {
        // 테스트 후 정리 (거의 사용 안함)
    }

    @BeforeAll  // ← 모든 테스트 실행 전에 한 번만 실행 (static)
    static void setupClass() {
        // 클래스 레벨 초기화
    }

    @AfterAll  // ← 모든 테스트 후에 한 번만 실행 (static)
    static void tearDownClass() {
        // 클래스 레벨 정리
    }
}
```

### 2. 테스트 그룹화

```java
@Nested
@DisplayName("프로젝트 생성")
class CreateTests {

    @Test
    void success() { }

    @Test
    void failEmptyName() { }
}

@Nested
@DisplayName("프로젝트 조회")
class ReadTests {

    @Test
    void findAll() { }

    @Test
    void findById() { }
}

@Nested
@DisplayName("프로젝트 수정")
class UpdateTests {

    @Test
    void success() { }
}
```

### 3. 매개변수화된 테스트

```java
@ParameterizedTest  // ← 여러 파라미터로 같은 테스트 실행
@ValueSource(strings = {"", "   ", "\t", "\n"})
@DisplayName("빈 문자열로 프로젝트 생성 실패")
void createProject_FailWithEmptyStrings(String emptyString) {
    assertThatThrownBy(() -> projectService.create(newProject))
        .isInstanceOf(IllegalArgumentException.class);
}

// 더 복잡한 매개변수
@ParameterizedTest
@CsvSource({
    "100, true",    // 100은 유효함
    "50,  true",    // 50은 유효함
    "-1,  false",   // -1은 유효하지 않음
    "101, false"    // 101은 유효하지 않음
})
@DisplayName("진행률 검증")
void validateProgress(int progress, boolean isValid) {
    if (isValid) {
        assertDoesNotThrow(() -> projectService.updateProgress(1L, progress));
    } else {
        assertThrows(IllegalArgumentException.class,
            () -> projectService.updateProgress(1L, progress));
    }
}
```

---

## Mockito 패턴

### 1. Mock 생성 및 주입

```java
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    // 패턴 1: @Mock + @InjectMocks
    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    // projectService는 자동으로 projectMapper를 주입받음
}
```

### 2. 메서드 호출 설정 (Stubbing)

```java
// 패턴 1: 값 반환
when(projectMapper.findById(1L)).thenReturn(testProject);

// 패턴 2: 컬렉션 반환
when(projectMapper.findAll()).thenReturn(Arrays.asList(
    testProject1,
    testProject2
));

// 패턴 3: 예외 발생
when(projectMapper.findById(999L))
    .thenThrow(new IllegalArgumentException("Not found"));

// 패턴 4: 여러 번 호출 시 다른 값
when(projectMapper.findAll())
    .thenReturn(emptyList())
    .thenReturn(listWithData());

// 패턴 5: 인자에 관계없이 반응
when(projectMapper.findById(anyLong())).thenReturn(testProject);

// 패턴 6: void 메서드
doNothing().when(projectMapper).delete(1L);
doThrow(new RuntimeException()).when(projectMapper).insert(null);
```

### 3. 호출 검증 (Verification)

```java
// 패턴 1: 호출됨을 검증
verify(projectMapper).insert(newProject);

// 패턴 2: 호출 횟수 검증
verify(projectMapper, times(1)).findById(1L);
verify(projectMapper, times(2)).update(any());

// 패턴 3: 호출되지 않음을 검증
verify(projectMapper, never()).delete(any());

// 패턴 4: 호출 순서 검증
InOrder inOrder = inOrder(projectMapper);
inOrder.verify(projectMapper).findById(1L);
inOrder.verify(projectMapper).update(any());

// 패턴 5: 상세한 호출 검증
verify(projectMapper).insert(argThat(project ->
    project.getName().equals("새 프로젝트")
));
```

### 4. 인자 매칭 (ArgumentMatchers)

```java
// any() - 모든 타입의 인자
when(projectMapper.insert(any(Project.class))).thenReturn(true);

// anyLong(), anyString(), anyInt() 등
when(projectMapper.findById(anyLong())).thenReturn(testProject);

// eq() - 특정 값과 일치
when(projectMapper.findByStatus(eq("ACTIVE"))).thenReturn(projects);

// argThat() - 복잡한 조건
when(projectMapper.update(argThat(p -> p.getId() > 0)))
    .thenReturn(true);

// contains() - 문자열 포함
when(projectMapper.search(contains("dev")))
    .thenReturn(projects);
```

---

## AssertJ 사용법

### 기본 문법

```java
import static org.assertj.core.api.Assertions.*;

class AssertJExamples {

    @Test
    void basicAssertions() {
        Project project = createTestProject();

        // null 검사
        assertThat(project).isNotNull();
        assertThat(project).isNull();

        // 동등성 검사
        assertThat(project.getName()).isEqualTo("DevLog");
        assertThat(project.getName()).isNotEqualTo("Other");

        // 대소 비교
        assertThat(project.getProgress()).isGreaterThan(0);
        assertThat(project.getProgress()).isLessThan(100);
        assertThat(project.getProgress()).isBetween(0, 100);

        // 문자열 검사
        assertThat(project.getName())
            .startsWith("Dev")
            .endsWith("Log")
            .contains("Log");
    }

    @Test
    void collectionAssertions() {
        List<Project> projects = Arrays.asList(
            createProject("Project 1"),
            createProject("Project 2")
        );

        // 컬렉션 검사
        assertThat(projects)
            .hasSize(2)
            .isNotEmpty()
            .contains(projects.get(0))
            .doesNotContain(new Project());

        // 모든 항목이 조건을 만족하는지 검사
        assertThat(projects)
            .allMatch(p -> p.getStatus().equals("ACTIVE"));

        // 적어도 하나가 조건을 만족하는지 검사
        assertThat(projects)
            .anyMatch(p -> p.getId() > 5);
    }

    @Test
    void exceptionAssertions() {
        // 예외 발생 검증
        assertThatThrownBy(() -> projectService.create(invalidProject))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("프로젝트 이름은 필수입니다")
            .hasMessageContaining("이름");

        // 특정 예외 타입 검증
        assertThatIllegalArgumentException()
            .isThrownBy(() -> projectService.create(invalidProject))
            .withMessageContaining("필수");
    }
}
```

### Optional 검사

```java
@Test
void optionalAssertions() {
    Optional<Project> found = projectService.findById(1L);
    Optional<Project> notFound = projectService.findById(999L);

    assertThat(found).isPresent();
    assertThat(found)
        .hasValue(testProject)
        .get()
        .extracting(Project::getName)
        .isEqualTo("DevLog");

    assertThat(notFound).isEmpty();
}
```

---

## DevLog 테스트 사례

### 사례 1: ProjectService.create() 테스트

#### 요구사항:
- ✅ 유효한 이름으로 프로젝트 생성 성공
- ✅ 이름이 null이면 실패
- ✅ 이름이 빈 문자열이면 실패
- ✅ 이름이 100자를 초과하면 실패

#### 테스트 코드:

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectService.create() 테스트")
class ProjectServiceCreateTest {

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    // 성공 케이스
    @Test
    @DisplayName("유효한 데이터로 프로젝트 생성 성공")
    void createProject_Success() {
        // ARRANGE
        Project newProject = new Project();
        newProject.setName("새 프로젝트");
        newProject.setDescription("설명");
        doNothing().when(projectMapper).insert(any(Project.class));

        // ACT
        Project result = projectService.create(newProject);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("새 프로젝트");
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
        assertThat(result.getProgress()).isEqualTo(0);
        verify(projectMapper, times(1)).insert(newProject);
    }

    // 실패 케이스 1: null 이름
    @Test
    @DisplayName("이름이 null이면 프로젝트 생성 실패")
    void createProject_Fail_NullName() {
        // ARRANGE
        Project invalidProject = new Project();
        invalidProject.setName(null);

        // ACT & ASSERT
        assertThatThrownBy(() -> projectService.create(invalidProject))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("프로젝트 이름은 필수입니다");

        verify(projectMapper, never()).insert(any());
    }

    // 실패 케이스 2: 빈 문자열
    @Test
    @DisplayName("이름이 빈 문자열이면 프로젝트 생성 실패")
    void createProject_Fail_EmptyName() {
        // ARRANGE
        Project invalidProject = new Project();
        invalidProject.setName("   ");

        // ACT & ASSERT
        assertThatThrownBy(() -> projectService.create(invalidProject))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("프로젝트 이름은 필수입니다");

        verify(projectMapper, never()).insert(any());
    }

    // 실패 케이스 3: 너무 긴 이름
    @Test
    @DisplayName("이름이 100자를 초과하면 프로젝트 생성 실패")
    void createProject_Fail_NameTooLong() {
        // ARRANGE
        Project invalidProject = new Project();
        invalidProject.setName("a".repeat(101));

        // ACT & ASSERT
        assertThatThrownBy(() -> projectService.create(invalidProject))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("프로젝트 이름은 100자를 초과할 수 없습니다");

        verify(projectMapper, never()).insert(any());
    }
}
```

### 사례 2: ProjectController.getProjectById() 테스트

#### 요구사항:
- ✅ 존재하는 프로젝트 조회 성공 (200 OK)
- ✅ 존재하지 않는 프로젝트 조회 실패 (404 Not Found)

#### 테스트 코드:

```java
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProjectController.getProjectById() 테스트")
class ProjectControllerGetByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("DevLog");
        testProject.setDescription("개발자 로그 시스템");
    }

    // 성공 케이스
    @Test
    @DisplayName("존재하는 프로젝트 조회 성공 (200 OK)")
    void getProjectById_Success() throws Exception {
        // ARRANGE
        when(projectService.findById(1L))
            .thenReturn(Optional.of(testProject));

        // ACT & ASSERT
        mockMvc.perform(get("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("DevLog"))
            .andExpect(jsonPath("$.description")
                .value("개발자 로그 시스템"));

        verify(projectService, times(1)).findById(1L);
    }

    // 실패 케이스
    @Test
    @DisplayName("존재하지 않는 프로젝트 조회 실패 (404 Not Found)")
    void getProjectById_Fail_NotFound() throws Exception {
        // ARRANGE
        when(projectService.findById(999L))
            .thenReturn(Optional.empty());

        // ACT & ASSERT
        mockMvc.perform(get("/api/projects/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(projectService, times(1)).findById(999L);
    }
}
```

---

## 테스트 네이밍 규칙

### DevLog 프로젝트 네이밍 규칙

```
[MethodName]_[ExpectedBehavior]_[GivenCondition]

또는

test[MethodName][Scenario]
```

#### 예시 1: ProjectService

```
✅ Good:
- createProject_Success()
- createProject_Fail_EmptyName()
- createProject_Fail_NameTooLong()
- findById_Success()
- findById_Fail_NotFound()
- updateProgress_Fail_NegativeProgress()
- updateProgress_Fail_ExcessiveProgress()

❌ Bad:
- createProjectTest()
- test1()
- testCreate()
- projectTest()
```

#### 예시 2: @DisplayName과 함께 사용

```java
// 테스트 메서드 이름은 기계적, 간결하게
@Test
void createProject_Fail_EmptyName() {
    // @DisplayName은 의도를 명확하게
    @DisplayName("프로젝트 생성 실패 - 이름이 빈 문자열인 경우")
    // ...
}
```

#### 패턴별 네이밍

```
CREATE/Insert:
- create[Resource]_Success()
- create[Resource]_Fail_[Reason]()

READ/Select:
- find[Resource]_Success()
- find[Resource]By[Criteria]_Success()
- find[Resource]_Fail_NotFound()

UPDATE:
- update[Resource]_Success()
- update[Resource]_Fail_NotFound()
- update[Property]_Success()
- update[Property]_Fail_[Reason]()

DELETE:
- delete[Resource]_Success()
- delete[Resource]_Fail_NotFound()

SEARCH:
- search[Resources]_Success()
- search[Resources]_EmptyResult()

COUNT:
- count[Resources]_Success()
- countBy[Criteria]_Success()
```

---

## 코드 커버리지

### 코드 커버리지란?

코드 커버리지는 **테스트가 얼마나 많은 코드를 실행하는가**를 측정합니다.

```
커버리지 = (테스트된 코드 라인) / (전체 코드 라인) × 100%
```

### DevLog 커버리지 목표

```
┌─────────────────────────────────────┐
│        코드 커버리지 목표             │
├─────────────────────────────────────┤
│                                     │
│  Service Layer:  ≥ 80% (중요!)    │
│  Controller:     ≥ 70%             │
│  Mapper/DAO:     ≥ 60%             │
│  Utility:        ≥ 70%             │
│  Exception:      ≥ 50%             │
│                                     │
│  전체 백엔드:     ≥ 70%             │
│                                     │
└─────────────────────────────────────┘
```

### JaCoCo로 코드 커버리지 측정

#### 1. Maven 설정 (pom.xml)

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

#### 2. 테스트 실행

```bash
# 테스트 실행 및 커버리지 리포트 생성
mvn clean test

# 생성된 리포트 위치
target/site/jacoco/index.html  # 브라우저에서 확인
```

#### 3. 커버리지 리포트 해석

```
Line Coverage:    98%  (라인 실행 여부)
Branch Coverage:  85%  (if/else 등 분기점)
Method Coverage:  95%  (메서드 호출 여부)
Class Coverage:   100% (클래스 로딩 여부)
```

---

## 일반적인 함정과 해결책

### 함정 1️⃣: 너무 많은 Mocking

```java
// ❌ 나쁜 예: 거의 모든 것을 Mock (테스트 가치 없음)
@Mock
private List<Project> projects;

@Mock
private String projectName;

@Test
void test() {
    when(projects.size()).thenReturn(1);
    when(projectName.equals("Dev")).thenReturn(true);
    // 이건 뭘 테스트하는가?
}

// ✅ 좋은 예: 필요한 것만 Mock
@Mock
private ProjectMapper projectMapper;

@InjectMocks
private ProjectService projectService;

@Test
void createProject_Success() {
    Project newProject = new Project();
    newProject.setName("새 프로젝트");

    when(projectMapper.insert(any())).thenReturn(true);
    Project result = projectService.create(newProject);

    assertThat(result.getName()).isEqualTo("새 프로젝트");
}
```

**해결책**: Mock은 의존성(DB, API)에만 사용. 테스트 대상 로직은 실제로 실행.

### 함정 2️⃣: 테스트 간 상태 공유

```java
// ❌ 나쁜 예: 테스트 간 의존성
class ProjectServiceTest {
    private static List<Project> sharedProjects = new ArrayList<>();

    @Test
    void addProject() {
        sharedProjects.add(new Project("Project 1"));
    }

    @Test
    void findProject() {
        // addProject()가 먼저 실행됐는지 확신할 수 없음!
        assertThat(sharedProjects.size()).isEqualTo(1);
    }
}

// ✅ 좋은 예: 각 테스트가 독립적
class ProjectServiceTest {
    @Test
    void addProject() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project("Project 1"));
        assertThat(projects.size()).isEqualTo(1);
    }

    @Test
    void findProject() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project("Project 1"));
        assertThat(projects.size()).isEqualTo(1);
    }
}
```

**해결책**: @BeforeEach에서 매번 새로운 테스트 데이터 초기화.

### 함정 3️⃣: 데이터베이스와 실제 통신

```java
// ❌ 나쁜 예: 실제 DB와 통신 (느리고 불안정)
@Test
void createProject() {
    Project newProject = new Project();
    newProject.setName("새 프로젝트");

    // 실제 DB에 INSERT!
    projectService.create(newProject);

    // 실제 DB에서 SELECT!
    Optional<Project> found = projectService.findById(1L);
    assertThat(found).isPresent();
}

// ✅ 좋은 예: Mock으로 DB 격리
@ExtendWith(MockitoExtension.class)
@Test
void createProject() {
    Project newProject = new Project();
    newProject.setName("새 프로젝트");

    when(projectMapper.insert(any())).thenReturn(true);

    projectService.create(newProject);

    verify(projectMapper, times(1)).insert(any());
}
```

**해결책**: 단위 테스트는 Mock 사용. 실제 DB는 통합 테스트에서만.

### 함정 4️⃣: 예외 처리를 테스트하지 않음

```java
// ❌ 나쁜 예: 정상 케이스만 테스트
@Test
void createProject() {
    Project newProject = new Project();
    newProject.setName("새 프로젝트");

    Project result = projectService.create(newProject);
    assertThat(result).isNotNull();
}

// ✅ 좋은 예: 예외 케이스도 테스트
@Test
void createProject_Success() {
    Project newProject = new Project();
    newProject.setName("새 프로젝트");

    Project result = projectService.create(newProject);
    assertThat(result).isNotNull();
}

@Test
void createProject_Fail_NullName() {
    Project invalidProject = new Project();
    invalidProject.setName(null);

    assertThatThrownBy(() -> projectService.create(invalidProject))
        .isInstanceOf(IllegalArgumentException.class);
}

@Test
void createProject_Fail_EmptyName() {
    Project invalidProject = new Project();
    invalidProject.setName("");

    assertThatThrownBy(() -> projectService.create(invalidProject))
        .isInstanceOf(IllegalArgumentException.class);
}
```

**해결책**: 정상 케이스 + 경계값 + 예외 케이스 모두 테스트.

### 함정 5️⃣: Assertion이 너무 많거나 너무 적음

```java
// ❌ 나쁜 예 1: Assertion이 너무 많음
@Test
void createProject() {
    Project result = projectService.create(newProject);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
    assertThat(result.getName()).isNotNull();
    assertThat(result.getDescription()).isNotNull();
    assertThat(result.getStatus()).isNotNull();
    assertThat(result.getProgress()).isNotNull();
    assertThat(result.getCreatedAt()).isNotNull();
    // ... 너무 많음
}

// ❌ 나쁜 예 2: Assertion이 너무 적음
@Test
void createProject() {
    Project result = projectService.create(newProject);
    assertThat(result).isNotNull();  // 너무 약함
}

// ✅ 좋은 예: 한 가지 행동에 적절한 수의 assertion
@Test
@DisplayName("프로젝트 생성 시 기본값 설정")
void createProject_DefaultsSet() {
    Project newProject = new Project();
    newProject.setName("새 프로젝트");

    Project result = projectService.create(newProject);

    assertThat(result.getStatus()).isEqualTo("ACTIVE");
    assertThat(result.getProgress()).isEqualTo(0);
}

@Test
@DisplayName("프로젝트 생성 시 입력값 보존")
void createProject_InputPreserved() {
    Project newProject = new Project();
    newProject.setName("새 프로젝트");
    newProject.setDescription("설명");

    Project result = projectService.create(newProject);

    assertThat(result.getName()).isEqualTo("새 프로젝트");
    assertThat(result.getDescription()).isEqualTo("설명");
}
```

**해결책**: 한 테스트는 한 가지 행동만 검증. 3-5개 assertion 목표.

---

## 체크리스트

### TDD 시작하기 전

- [ ] 프로젝트 요구사항 명확히 이해
- [ ] 테스트할 메서드 식별
- [ ] 성공/실패 케이스 나열
- [ ] Mock 필요 여부 판단

### 테스트 작성 중

- [ ] 실패하는 테스트부터 작성
- [ ] AAA 패턴 따르기 (Arrange-Act-Assert)
- [ ] @DisplayName으로 의도 명확히 표현
- [ ] 한 테스트는 한 가지 행동만 검증
- [ ] Mock vs 실제 객체 올바르게 사용

### 테스트 완료 후

- [ ] 모든 테스트가 통과하는가?
- [ ] 코드 커버리지 ≥ 70%?
- [ ] 테스트가 명확한가? (다른 사람이 이해 가능?)
- [ ] 중복 코드 제거 (리팩토링)
- [ ] 테스트도 코드 - 마찬가지로 유지보수 필요

### 새 기능 추가 시

- [ ] 기존 테스트는 모두 통과하는가?
- [ ] 새로운 테스트 추가
- [ ] 엣지 케이스 고려
- [ ] 문서 업데이트

---

## 참고 자료

### 공식 문서
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/assertj-core-features-highlight.html)
- [Spring Boot Test Documentation](https://spring.io/guides/gs/testing-web/)

### 추천 도서
- "Test Driven Development: By Example" - Kent Beck
- "Working Effectively with Legacy Code" - Michael Feathers
- "Growing Object-Oriented Software, Guided by Tests" - Steve Freeman

### DevLog 테스트 파일
- `ProjectServiceTest.java`: 프로젝트 서비스 단위 테스트 (28개 테스트)
- `ProjectControllerIntegrationTest.java`: 프로젝트 컨트롤러 통합 테스트 (12개 테스트)
- `DevLogServiceTest.java`: 개발 로그 서비스 단위 테스트 (20개 테스트)
- `StatisticsServiceTest.java`: 통계 서비스 단위 테스트 (예정)
- `TechTagServiceTest.java`: 기술 태그 서비스 단위 테스트 (예정)

---

## 최종 조언

### TDD의 핵심

```
테스트는 코드가 아니라 "사양(Specification)"입니다.
테스트를 읽으면 이 코드가 무엇을 하는지 명확하게 알 수 있어야 합니다.
```

### 가장 흔한 실수

```
1. "나중에 테스트 작성하자" → 영원히 안함
2. 너무 많은 Mock → 테스트 가치 없음
3. 테스트 간 의존성 → 불안정한 테스트
4. 테스트를 다시 테스트 → 오버 엔지니어링
```

### 성공적인 TDD의 조건

```
✅ 테스트를 먼저 작성한다
✅ 작은 단위로 자주 테스트한다
✅ 테스트를 신뢰한다
✅ 테스트는 문서이자 안전망이다
```

---

*Last Updated: 2025-12-31*
*Version: 1.0*

Happy Testing! 🧪
