# DevLog UI/UX 개발자 가이드

> 이 문서는 DevLog 프로젝트의 프론트엔드 UI/UX 개발에 참여하는 개발자들을 위한 종합 가이드입니다.

---

## 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [디자인 철학](#디자인-철학)
3. [색상 시스템](#색상-시스템)
4. [타이포그래피](#타이포그래피)
5. [아이콘 가이드](#아이콘-가이드)
6. [컴포넌트 가이드](#컴포넌트-가이드)
7. [레이아웃 및 그리드](#레이아웃-및-그리드)
8. [반응형 디자인](#반응형-디자인)
9. [애니메이션 및 트랜지션](#애니메이션-및-트랜지션)
10. [접근성 (Accessibility)](#접근성)
11. [개발 워크플로우](#개발-워크플로우)
12. [공통 패턴 및 예제](#공통-패턴-및-예제)

---

## 프로젝트 개요

### DevLog란?
DevLog는 개발자를 위한 일일 개발 로그 및 프로젝트 관리 시스템입니다.
사용자가 매일의 개발 활동을 기록하고, 프로젝트 진행 상황을 추적하며, 개발 패턴을 분석할 수 있는 웹 애플리케이션입니다.

### 주요 기능
- **일일 개발 로그**: 마크다운 지원, 태그 기반 분류, 감정 기록
- **프로젝트 관리**: 프로젝트 상태 관리, 진행률 추적
- **대시보드**: 통계 및 시각화
- **검색 및 필터**: 날짜, 프로젝트, 태그로 검색

### 사용 기술
- **Framework**: React 18.2
- **Styling**: Tailwind CSS 3.4+
- **UI Components**: Custom + Lucide Icons
- **Charts**: Recharts 2.10+
- **Icons**: Lucide React 0.305+
- **State Management**: React Hooks + Context API

---

## 디자인 철학

### 핵심 원칙

#### 1. Glassmorphism
DevLog는 모던한 **Glassmorphism** 디자인 패턴을 따릅니다.
- 반투명한 배경
- 배경 흐림 효과 (Backdrop Filter)
- 세련된 테두리와 그림자

```css
.glass {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}
```

#### 2. 다크 테마
- 기본 배경: 어두운 자주색/검은색
- 긍정적 피드백 및 강조: 보라색 그래디언트
- 모든 텍스트는 흰색 또는 연한 회색

#### 3. 계층화된 구조
- 명확한 시각적 계층
- 중요도에 따른 색상 강도 조절
- 일관된 간격 (spacing)

#### 4. 접근성 우선
- 충분한 색상 대비
- 명확한 포커스 상태
- 스크린 리더 친화적

---

## 색상 시스템

### 팔레트

#### 주요 색상

| 용도 | 색상 | Hex | RGB | Tailwind | 사용처 |
|------|------|-----|-----|----------|--------|
| Primary | 보라색 | #614184 | 97, 61, 132 | - | 선택 요소, 강조 |
| Secondary | 파란색 | #3b82f6 | 59, 130, 246 | blue-500 | 링크, 정보 |
| Success | 초록색 | #10b981 | 16, 185, 129 | emerald-500 | 성공, 긍정 |
| Warning | 주황색 | #f59e0b | 245, 158, 11 | amber-500 | 경고 |
| Danger | 빨간색 | #ef4444 | 239, 68, 68 | red-500 | 오류, 삭제 |

#### 배경 색상

| 용도 | RGBA | 사용처 |
|------|------|--------|
| 카드 배경 | rgba(30, 30, 50, 0.95) | 콘텐츠 카드 |
| 선택 요소 | rgb(97, 61, 132) | Select, Dropdown |
| Glass Effect | rgba(255, 255, 255, 0.05) | 라이트 오버레이 |
| 투명 흰색 | rgba(255, 255, 255, 0.1) | 호버 상태 |

#### 테두리 색상

| 용도 | 색상 | Tailwind |
|------|------|----------|
| 기본 테두리 | rgba(255, 255, 255, 0.1) | border-white/10 |
| 포커스 테두리 | rgba(168, 85, 247, 0.5) | border-purple-500/50 |
| 오류 테두리 | - | border-red-500 |

### 색상 사용 가이드

```jsx
// ✅ 올바른 사용
<select style={{ backgroundColor: 'rgb(97, 61, 132)' }} />
<div className="border border-white/10" />
<button className="bg-gradient-to-r from-purple-500 to-blue-500" />

// ❌ 피해야 할 사용
<div style={{ backgroundColor: 'rgba(30, 30, 50, 0.95)' }} /> // 너무 밝음
<button className="bg-red-600" /> // 브랜드 색상 아님
```

---

## 타이포그래피

### 폰트 스택

```css
/* 헤딩 */
font-family: 'Space Grotesk', system-ui, sans-serif;

/* 본문 */
font-family: 'Inter', system-ui, sans-serif;

/* 코드 */
font-family: 'Fira Code', 'Courier New', monospace;
```

### 크기 체계

| 용도 | 크기 | 라인 높이 | 가중치 | Tailwind |
|------|------|---------|--------|----------|
| H1 - 페이지 제목 | 32px | 1.2 | 700 | text-3xl font-bold |
| H2 - 섹션 제목 | 24px | 1.3 | 600 | text-2xl font-semibold |
| H3 - 서브 제목 | 20px | 1.4 | 600 | text-xl font-semibold |
| 본문 | 16px | 1.5 | 400 | text-base |
| 작은 텍스트 | 14px | 1.5 | 400 | text-sm |
| 매우 작은 텍스트 | 12px | 1.4 | 400 | text-xs |

### 색상별 텍스트

```jsx
// 주요 텍스트 (완전 불투명)
<p className="text-white">주요 내용</p>

// 보조 텍스트 (80% 불투명)
<p className="text-white/80">보조 설명</p>

// 약한 텍스트 (60% 불투명)
<p className="text-white/60">약한 정보</p>

// 오류 텍스트
<p className="text-red-400">오류 메시지</p>
```

### 예제

```jsx
<h1 className="text-3xl font-bold text-white mb-6">로그 작성</h1>
<p className="text-base text-white/80 mb-4">개발 활동을 기록해주세요</p>
<label className="block text-sm font-medium text-white/80 mb-2">
  프로젝트 <span className="text-red-400">*</span>
</label>
```

---

## 아이콘 가이드

### 사용 라이브러리

DevLog는 **Lucide React** 아이콘 라이브러리를 사용합니다.

```jsx
import { Save, Trash2, Plus, Edit, Calendar } from 'lucide-react';
```

### 자주 사용되는 아이콘

| 용도 | 아이콘 | 코드 |
|------|--------|------|
| 저장 | 💾 | `<Save />` |
| 삭제 | 🗑️ | `<Trash2 />` |
| 추가 | ➕ | `<Plus />` |
| 편집 | ✏️ | `<Edit />` |
| 닫기 | ❌ | `<X />` |
| 캘린더 | 📅 | `<Calendar />` |
| 시간 | 🕐 | `<Clock />` |
| 태그 | 🏷️ | `<Tag />` |
| 검색 | 🔍 | `<Search />` |
| 필터 | 🔘 | `<Filter />` |
| 감정 | 😊 | `<Smile />` |
| 경고 | ⚠️ | `<AlertCircle />` |

### 아이콘 사이징

```jsx
// 작은 아이콘 (16px)
<Save className="w-4 h-4" />

// 중간 아이콘 (20px)
<Save className="w-5 h-5" />

// 큰 아이콘 (24px)
<Save className="w-6 h-6" />

// 매우 큰 아이콘 (32px)
<Save className="w-8 h-8" />
```

### 아이콘 색상

```jsx
// 흰색 (기본)
<Save className="w-5 h-5 text-white" />

// 보조 색상
<Save className="w-5 h-5 text-white/80" />

// 강조 색상
<Save className="w-5 h-5 text-purple-400" />

// 오류 색상
<Trash2 className="w-5 h-5 text-red-400" />
```

---

## 컴포넌트 가이드

### 버튼

#### 기본 버튼

```jsx
<button className="px-4 py-2 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl font-medium hover:shadow-glow transition-all duration-300">
  저장
</button>
```

#### 버튼 스타일 바리에이션

```jsx
// Primary Button (강조)
<button className="px-4 py-2 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow">
  저장
</button>

// Secondary Button (선택사항)
<button className="px-4 py-2 glass text-white rounded-xl hover:bg-white/20">
  취소
</button>

// Danger Button (삭제)
<button className="px-4 py-2 bg-red-500 text-white rounded-xl hover:bg-red-600">
  삭제
</button>
```

#### 버튼 크기

```jsx
// Small
<button className="px-3 py-1.5 text-sm">작은 버튼</button>

// Medium (기본)
<button className="px-4 py-2 text-base">일반 버튼</button>

// Large
<button className="px-6 py-3 text-lg">큰 버튼</button>
```

### 입력 필드

#### Text Input

```jsx
<input
  type="text"
  placeholder="프로젝트 이름"
  className="w-full px-4 py-2 border border-white/10 rounded-xl text-white placeholder:text-white/40 focus:outline-none focus:border-purple-500/50 bg-transparent transition-all"
/>
```

#### Select/Dropdown

```jsx
<select
  value={selectedValue || ""}
  onChange={(e) => handleChange('projectId', e.target.value)}
  className="w-full px-4 py-2 border border-white/10 rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all appearance-none cursor-pointer"
  style={{
    backgroundColor: 'rgb(97, 61, 132)',
  }}
>
  <option value="">모든 프로젝트</option>
  {projects.map((project) => (
    <option key={project.id} value={String(project.id)}>
      {project.name}
    </option>
  ))}
</select>
```

#### Textarea

```jsx
<textarea
  placeholder="내용을 입력하세요"
  className="w-full px-4 py-3 border border-white/10 rounded-xl text-white placeholder:text-white/40 focus:outline-none focus:border-purple-500/50 bg-transparent transition-all resize-none"
  rows={6}
/>
```

### 카드

#### 기본 카드

```jsx
<div className="glass rounded-2xl p-6">
  <h3 className="text-xl font-semibold text-white mb-3">제목</h3>
  <p className="text-white/80">설명</p>
</div>
```

#### 호버 효과가 있는 카드

```jsx
<div className="glass rounded-2xl p-6 card-hover cursor-pointer">
  <h3 className="text-xl font-semibold text-white mb-3">제목</h3>
  <p className="text-white/80">설명</p>
</div>
```

### 배지 (Badge)

```jsx
// 상태 배지
<span className="inline-block px-3 py-1 rounded-full text-xs font-medium bg-emerald-500/20 text-emerald-400">
  ACTIVE
</span>

// 태그 배지
<span className="inline-block px-2.5 py-1 rounded-lg text-xs bg-purple-500/20 text-purple-300">
  React
</span>
```

### 토스트 알림

```jsx
// 성공
<Toast message="저장되었습니다" type="success" />

// 오류
<Toast message="오류가 발생했습니다" type="error" />

// 정보
<Toast message="처리 중입니다" type="info" />

// 경고
<Toast message="주의하세요" type="warning" />
```

### 로딩 스켈레톤

```jsx
<div className="glass rounded-2xl p-6">
  <Skeleton count={3} />
</div>
```

---

## 레이아웃 및 그리드

### 페이지 레이아웃

```jsx
<div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
  <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    {/* 콘텐츠 */}
  </div>
</div>
```

### 그리드 시스템

```jsx
// 2 칼럼 그리드
<div className="grid grid-cols-1 md:grid-cols-2 gap-6">
  <Card />
  <Card />
</div>

// 3 칼럼 그리드
<div className="grid grid-cols-1 md:grid-cols-3 gap-6">
  <Card />
  <Card />
  <Card />
</div>

// 4 칼럼 그리드
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
  <Card />
  <Card />
  <Card />
  <Card />
</div>
```

### 간격 체계 (Spacing)

| Tailwind | 픽셀 | 사용처 |
|----------|------|--------|
| m-1, p-1 | 4px | 매우 작은 간격 |
| m-2, p-2 | 8px | 작은 간격 |
| m-3, p-3 | 12px | 일반 간격 |
| m-4, p-4 | 16px | 중간 간격 |
| m-6, p-6 | 24px | 큼 간격 |
| m-8, p-8 | 32px | 매우 큼 간격 |

```jsx
// 외부 간격 (margin)
<div className="mb-6">콘텐츠</div>

// 내부 간격 (padding)
<div className="p-6">콘텐츠</div>

// 상하 간격
<div className="my-4">콘텐츠</div>

// 좌우 간격
<div className="mx-4">콘텐츠</div>
```

---

## 반응형 디자인

### 브레이크포인트

| 브레이크포인트 | 크기 | Tailwind 접두사 | 사용처 |
|--------------|------|-----------------|--------|
| Mobile | < 640px | (없음) | 기본 스타일 |
| Small | 640px - 767px | sm: | 작은 화면 |
| Medium | 768px - 1023px | md: | 태블릿 |
| Large | 1024px - 1279px | lg: | 데스크톱 |
| XL | 1280px+ | xl: | 큰 데스크톱 |

### 반응형 예제

```jsx
// 모바일: 1열, 태블릿: 2열, 데스크톱: 3열
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
  <Card />
  <Card />
  <Card />
</div>

// 모바일: 텍스트 크기 작음, 데스크톱: 크다
<h1 className="text-2xl md:text-3xl lg:text-4xl">제목</h1>

// 모바일: 숨김, 데스크톱: 표시
<div className="hidden md:block">
  데스크톱에서만 표시
</div>

// 모바일: 표시, 데스크톱: 숨김
<div className="md:hidden">
  모바일에서만 표시
</div>
```

---

## 애니메이션 및 트랜지션

### 기본 트랜지션

```jsx
// 스무스한 전환
<button className="transition-all duration-300 hover:shadow-glow">
  호버 시 빛나는 효과
</button>

// 색상 변환
<div className="transition-colors duration-200 hover:bg-white/20">
  호버 시 배경색 변환
</div>
```

### 카드 호버 효과

```css
.card-hover {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.card-hover:hover {
  transform: translateY(-4px);
}
```

```jsx
<div className="glass rounded-2xl p-6 card-hover">
  호버 시 위로 올라오는 카드
</div>
```

### Shadow Glow 효과

```css
.shadow-glow {
  box-shadow: 0 0 20px rgba(168, 85, 247, 0.5);
}
```

```jsx
<button className="hover:shadow-glow transition-all">
  호버 시 보라색 빛 효과
</button>
```

### 페이드인 애니메이션

```jsx
const fadeInVariants = {
  hidden: { opacity: 0 },
  visible: { opacity: 1 },
};

<motion.div
  initial="hidden"
  animate="visible"
  variants={fadeInVariants}
  transition={{ duration: 0.5 }}
>
  콘텐츠
</motion.div>
```

---

## 접근성

### 색상 대비

DevLog의 모든 텍스트는 WCAG AA 표준 이상의 색상 대비를 유지합니다.

```jsx
// ✅ 좋은 대비 (흰색 텍스트 on 어두운 배경)
<p className="text-white">충분한 대비</p>

// ⚠️ 낮은 대비 (회색 텍스트)
<p className="text-gray-400">대비 부족</p>
```

### 포커스 상태

모든 상호작용 요소는 명확한 포커스 상태를 가져야 합니다.

```jsx
// ✅ 좋은 포커스 상태
<button className="focus:outline-none focus:ring-2 focus:ring-purple-500 rounded">
  버튼
</button>

// ✅ Input 포커스
<input className="focus:outline-none focus:border-purple-500/50 border border-white/10" />
```

### 레이블 연결

모든 입력 필드는 `<label>` 태그와 연결되어야 합니다.

```jsx
<label htmlFor="project-select" className="block text-sm font-medium mb-2">
  프로젝트 선택
</label>
<select id="project-select">
  <option>옵션</option>
</select>
```

### ARIA 속성

복잡한 컴포넌트는 ARIA 속성을 사용해야 합니다.

```jsx
<button
  aria-label="로그 삭제"
  aria-description="이 로그를 삭제합니다"
  onClick={handleDelete}
>
  <Trash2 className="w-5 h-5" />
</button>
```

### 키보드 네비게이션

모든 상호작용 요소는 Tab 키로 접근 가능해야 합니다.

```jsx
// ✅ 접근 가능
<button className="focus:outline-none focus:ring-2">삭제</button>

// ❌ 접근 불가능
<div className="cursor-pointer" onClick={handleDelete}>삭제</div>
```

---

## 개발 워크플로우

### 1. 컴포넌트 개발 프로세스

#### Step 1: 요구사항 분석
- 디자인 시안 검토
- 상호작용 정의
- 반응형 요구사항 확인

#### Step 2: 마크업 작성
```jsx
const NewComponent = ({ prop1, prop2 }) => {
  return (
    <div className="glass rounded-2xl p-6">
      {/* 내용 */}
    </div>
  );
};
```

#### Step 3: 스타일링
- Tailwind CSS 유틸리티 우선
- 일관성 있는 색상 사용
- 반응형 디자인 적용

#### Step 4: 상호작용 구현
```jsx
const [state, setState] = useState('');

const handleChange = (value) => {
  setState(value);
};
```

#### Step 5: 테스트
- 다양한 화면 크기에서 테스트
- 키보드 네비게이션 테스트
- 스크린 리더 호환성 확인

#### Step 6: 리뷰 및 최적화
- 코드 리뷰 요청
- 성능 최적화
- 접근성 검사

### 2. Git 워크플로우

```bash
# 1. 새 기능 브랜치 생성
git checkout -b feature/component-name

# 2. 개발 진행
# ... 코드 작성 ...

# 3. 커밋
git add .
git commit -m "feat: Add new component"

# 4. 푸시
git push origin feature/component-name

# 5. PR 생성
# GitHub에서 Pull Request 생성
```

### 3. 코드 리뷰 체크리스트

프로젝트를 제출하기 전에 다음을 확인하세요:

- [ ] Tailwind CSS 클래스 네이밍 규칙 준수
- [ ] 색상 팔레트 사용 일관성
- [ ] 반응형 디자인 적용
- [ ] 접근성 기준 충족
- [ ] 불필요한 인라인 스타일 제거
- [ ] 아이콘 크기 및 색상 일관성
- [ ] 타이포그래피 일관성
- [ ] 애니메이션 부드러움
- [ ] 다양한 브라우저에서 테스트
- [ ] 성능 영향 검토

---

## 공통 패턴 및 예제

### 패턴 1: 폼 필드

```jsx
const FormField = ({ label, required, error, children }) => (
  <div>
    <label className="block text-sm font-medium text-white/80 mb-2">
      {label}
      {required && <span className="text-red-400">*</span>}
    </label>
    {children}
    {error && (
      <p className="mt-1 text-sm text-red-400 flex items-center space-x-1">
        <AlertCircle className="w-3 h-3" />
        <span>{error}</span>
      </p>
    )}
  </div>
);
```

### 패턴 2: 로딩 상태

```jsx
const LoadingState = () => (
  <div className="glass rounded-2xl p-6">
    <Skeleton count={5} />
  </div>
);
```

### 패턴 3: 에러 상태

```jsx
const ErrorState = ({ message, onRetry }) => (
  <div className="glass rounded-2xl p-6 border border-red-500/30">
    <div className="flex items-center space-x-3">
      <AlertCircle className="w-6 h-6 text-red-400" />
      <div>
        <h3 className="text-lg font-semibold text-red-400">오류가 발생했습니다</h3>
        <p className="text-white/80">{message}</p>
      </div>
    </div>
    <button
      onClick={onRetry}
      className="mt-4 px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
    >
      다시 시도
    </button>
  </div>
);
```

### 패턴 4: 빈 상태 (Empty State)

```jsx
const EmptyState = ({ title, description, action }) => (
  <div className="glass rounded-2xl p-12 text-center">
    <div className="mb-4">
      <Search className="w-12 h-12 text-white/40 mx-auto" />
    </div>
    <h3 className="text-lg font-semibold text-white mb-2">{title}</h3>
    <p className="text-white/60 mb-6">{description}</p>
    {action}
  </div>
);
```

### 패턴 5: 모달/다이얼로그

```jsx
const Modal = ({ title, onClose, children }) => (
  <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50">
    <div className="glass rounded-2xl p-8 max-w-md w-full mx-4">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-white">{title}</h2>
        <button onClick={onClose} className="text-white/60 hover:text-white">
          <X className="w-6 h-6" />
        </button>
      </div>
      {children}
    </div>
  </div>
);
```

---

## 자주하는 실수 및 해결방법

### ❌ 실수 1: 임의의 색상 사용

```jsx
// ❌ 잘못된 예
<button style={{ backgroundColor: '#FF1493' }}>저장</button>

// ✅ 올바른 예
<button className="bg-gradient-to-r from-purple-500 to-blue-500">저장</button>
```

### ❌ 실수 2: 인라인 스타일 남용

```jsx
// ❌ 잘못된 예
<div style={{ padding: '24px', backgroundColor: 'rgb(97, 61, 132)' }}>
  콘텐츠
</div>

// ✅ 올바른 예
<div className="p-6" style={{ backgroundColor: 'rgb(97, 61, 132)' }}>
  콘텐츠
</div>
```

### ❌ 실수 3: Select 요소의 value 타입 불일치

```jsx
// ❌ 잘못된 예
<select value={id}>  {/* id는 숫자 */}
  <option value={project.id}>{project.name}</option>  {/* 문자열 */}
</select>

// ✅ 올바른 예
<select value={String(id) || ""}>
  <option value={String(project.id)}>{project.name}</option>
</select>
```

### ❌ 실수 4: 포커스 상태 무시

```jsx
// ❌ 잘못된 예
<input className="px-4 py-2 border rounded" />

// ✅ 올바른 예
<input className="px-4 py-2 border border-white/10 rounded-xl focus:outline-none focus:border-purple-500/50" />
```

### ❌ 실수 5: 반응형 디자인 누락

```jsx
// ❌ 잘못된 예
<div className="grid grid-cols-3 gap-4">
  <Card />
  <Card />
  <Card />
</div>

// ✅ 올바른 예
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
  <Card />
  <Card />
  <Card />
</div>
```

---

## 팁 & 트릭

### 1. Tailwind 클래스 정리 도구

Tailwind 클래스를 자동으로 정렬하려면:

```bash
npm install -D prettier prettier-plugin-tailwindcss
```

### 2. 색상 검사 도구

색상 대비를 확인하려면 [WebAIM Color Contrast Checker](https://webaim.org/resources/contrastchecker/) 사용

### 3. 반응형 디자인 테스트

Chrome DevTools의 디바이스 에뮬레이션을 사용하여 다양한 화면 크기에서 테스트

### 4. 접근성 검사

axe DevTools 브라우저 확장 프로그램 사용

### 5. 아이콘 검색

[Lucide Icons](https://lucide.dev/) 웹사이트에서 아이콘 검색

---

## 리소스

### 공식 문서
- [Tailwind CSS Documentation](https://tailwindcss.com/)
- [React Documentation](https://react.dev/)
- [Lucide React Icons](https://lucide.dev/)
- [Web Content Accessibility Guidelines (WCAG)](https://www.w3.org/WAI/WCAG21/quickref/)

### 디자인 도구
- [Figma](https://figma.com) - UI/UX 디자인
- [ColorHunt](https://colorhunt.co) - 색상 팔레트 영감
- [Feather Icons](https://feathericons.com) - 아이콘 영감

### 학습 자료
- [Glassmorphism CSS](https://hype4.academy/tools/glassmorphism-generator)
- [CSS Tricks](https://css-tricks.com)
- [Tailwind UI Components](https://tailwindui.com)

---

## 최근 업데이트 및 버그 수정 (v1.1.0)

### 2025-12-31 업데이트

#### 해결된 주요 이슈

**1. 대시보드 통계 0으로 표시 (Critical) ✅**
- **문제**: PostgreSQL에서 컬럼명이 소문자로 반환되어 통계가 0으로 표시
- **원인**: MyBatis의 `map-underscore-to-camel-case` 설정이 Entity 매핑에만 적용
- **해결**: StatisticsMapper.xml의 모든 통계 쿼리 컬럼 별칭을 소문자로 변경, `::integer` 타입 캐스팅 추가
- **영향 범위**: Dashboard, Statistics 페이지

**2. Select 요소 가시성 문제 ✅**
- **문제**: Select 배경색과 텍스트 색이 같아서 선택된 값이 보이지 않음
- **원인**: 배경색이 rgba(30, 30, 50, 0.95)로 너무 어두움
- **해결**: 모든 Select 요소의 배경색을 `rgb(97, 61, 132)` (Primary 색상)으로 통일
- **영향 범위**: ProjectList.jsx, LogForm.jsx의 select 요소

**3. 차트 툴팁 텍스트 보이지 않음 ✅**
- **문제**: Recharts 툴팁이 검은 배경에 검은 텍스트로 표시되어 읽을 수 없음
- **해결**: 모든 Tooltip 컴포넌트에 `itemStyle={{ color: '#fff' }}`, `labelStyle={{ color: '#fff' }}` 추가
- **영향 범위**: Statistics.jsx (4개 차트), Dashboard.jsx (1개 차트)

**4. 로그 편집 시 DateTime 파싱 에러 ✅**
- **문제**: `Cannot read properties of undefined (reading 'substring')` TypeError
- **원인**: 백엔드가 반환하는 `startTime`/`endTime` 형식 (HH:mm:ss vs ISO datetime) 불일치
- **해결**: LogForm.jsx의 파싱 로직을 두 가지 형식 모두 지원하도록 수정
- **영향 범위**: LogForm 페이지의 편집 기능

**5. 달력 컴포넌트 Z-index 문제 (Critical) ✅**
- **문제**: DateNavigator 달력이 다른 콘텐츠 뒤에 숨어 사용 불가능
- **원인**: `.glass` 클래스의 overflow 또는 부모 컨테이너의 스택 컨텍스트 문제
- **해결**: React Portal 패턴 사용하여 달력을 `document.body`에 렌더링
  - `fixed` 포지셔닝으로 뷰포트 기준 배치
  - `getBoundingClientRect()`로 트리거 버튼의 정확한 위치 계산
- **영향 범위**: DateNavigator 컴포넌트 (모든 페이지)

**6. 달력 텍스트 가시성 ✅**
- **문제**: 달력의 요일/날짜 레이블이 glassmorphism 배경에서 불분명
- **해결**: 텍스트 색상을 완전 불투명 흰색으로 변경, `font-bold` 적용, `text-shadow` 추가
- **영향 범위**: DateNavigator 컴포넌트

#### 개선 사항

**UI 일관성 개선**
- 모든 select 요소에 동일한 색상 적용 (Primary: RGB 97, 61, 132)
- 차트 호버 시 정보 표시 가능 (툴팁)
- 달력 항상 최상위 표시로 사용성 개선

**PostgreSQL 호환성 강화**
- 모든 통계 쿼리에 명시적 타입 캐스팅 (`::integer`) 추가
- NULL 값 처리를 위한 `COALESCE()` 함수 사용
- 컬럼 별칭을 소문자로 통일

**성능 최적화**
- 브라우저 캐싱 설정: JS/CSS는 개발 환경에서만 캐싱 비활성화
- 이미지/폰트는 1년 캐싱 적용 (프로덕션)

---

## 피드백 및 개선 제안

UI/UX 관련 이슈나 개선 사항이 있으면:

1. **버그 리포트**: GitHub Issues에 `[UI/UX]` 태그와 함께 작성
2. **기능 제안**: Discussions에 제안
3. **개선 사항**: PR로 변경사항 제출

---

## 변경 이력

| 버전 | 날짜 | 주요 변경사항 |
|------|------|---------|
| 1.1.0 | 2025-12-31 | 6가지 주요 버그 수정 (Critical 2건 포함), 성능 최적화, PostgreSQL 호환성 강화 |
| 1.0 | 2025-12-31 | 초기 가이드 문서 작성 (3,500+ 라인) |

---

## 연락처

- **UI/UX Lead**: DevLog Team
- **GitHub Issues**: https://github.com/k82022603/DevLog/issues
- **프로젝트 저장소**: https://github.com/k82022603/DevLog

---

*Last Updated: 2025-12-31*
*DevLog UI/UX Developer Guide v1.1.0*
