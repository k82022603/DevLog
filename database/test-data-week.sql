-- ============================================================
-- DevLog Test Data for Week (2025-12-29 ~ 2026-01-04)
-- ============================================================
-- 주간 통계 테스트를 위한 데이터
-- ============================================================

-- ============================================================
-- DEV_LOGS - 주간 테스트 데이터 (2025-12-29 ~ 2026-01-04)
-- ============================================================

-- 12/29 (일요일) - 프로젝트 1, 2개 로그
INSERT INTO dev_logs (project_id, log_date, start_time, end_time, title, description, achievements, challenges, learnings, mood) VALUES
(1, '2025-12-29', '10:00', '13:00', 'API 엔드포인트 리팩토링',
 '기존 REST API 엔드포인트를 RESTful 원칙에 맞게 리팩토링했습니다.',
 '- API 구조 개선
- 응답 형식 통일
- 에러 처리 강화',
 '- 기존 코드와의 호환성 유지가 어려웠습니다.',
 '- REST API 설계 원칙을 체계적으로 학습했습니다.',
 'GOOD'),

(2, '2025-12-29', '14:00', '17:00', '포트폴리오 반응형 디자인 구현',
 'Tailwind CSS를 활용한 완전 반응형 디자인 구현 완료.',
 '- 모바일 레이아웃 최적화
- 태블릿 뷰 구현
- 브레이크포인트 설정',
 '- Safari 브라우저 호환성 이슈가 있었습니다.',
 '- CSS Grid와 Flexbox를 조합하면 복잡한 레이아웃도 쉽게 구현됩니다.',
 'GREAT');

-- 12/30 (월요일) - 프로젝트 1, 3개 로그
INSERT INTO dev_logs (project_id, log_date, start_time, end_time, title, description, achievements, challenges, learnings, mood) VALUES
(1, '2025-12-30', '09:00', '11:30', '통계 API 개발 시작',
 '주간/월간 통계 API를 개발하기 시작했습니다. PostgreSQL CTE를 활용한 복잡한 쿼리 작성.',
 '- StatisticsMapper.xml 작성
- 주간 통계 쿼리 구현
- DTO 클래스 설계',
 '- PostgreSQL의 날짜 함수가 MySQL과 달라 학습이 필요했습니다.',
 '- WITH RECURSIVE를 사용하면 주차별 데이터를 쉽게 생성할 수 있습니다.',
 'NEUTRAL'),

(1, '2025-12-30', '13:00', '16:00', 'MyBatis 동적 쿼리 작성',
 'MyBatis의 동적 SQL 기능을 활용하여 유연한 검색 쿼리를 구현했습니다.',
 '- <if> 태그를 사용한 조건부 쿼리
- <where> 태그로 WHERE 절 자동 처리
- <foreach>로 IN 절 구현',
 '- XML 특수문자 이스케이프 처리를 잊어서 에러가 발생했습니다.',
 '- CDATA 섹션을 사용하면 특수문자를 자유롭게 사용할 수 있습니다.',
 'GOOD'),

(1, '2025-12-30', '16:30', '18:00', 'Postman 테스트 컬렉션 작성',
 'API 테스트를 위한 Postman 컬렉션을 작성하고 환경 변수를 설정했습니다.',
 '- 모든 API 엔드포인트 테스트 작성
- 환경 변수 설정
- 테스트 스크립트 추가',
 '- 인증이 필요한 API는 아직 미구현 상태입니다.',
 '- Postman Collection Runner를 사용하면 전체 API를 한번에 테스트할 수 있습니다.',
 'GOOD');

-- 12/31 (화요일) - 프로젝트 1, 2, 총 4개 로그
INSERT INTO dev_logs (project_id, log_date, start_time, end_time, title, description, achievements, challenges, learnings, mood) VALUES
(1, '2025-12-31', '09:00', '12:00', '프론트엔드 Dashboard 컴포넌트 구현',
 'Recharts를 활용한 대시보드 차트 컴포넌트를 구현했습니다.',
 '- LineChart 구현
- BarChart 구현
- 실시간 데이터 연동',
 '- 차트 데이터 포맷 변환이 복잡했습니다.
- 반응형 차트 크기 조정이 어려웠습니다.',
 '- ResponsiveContainer를 사용하면 차트가 자동으로 리사이즈됩니다.',
 'GREAT'),

(2, '2025-12-31', '13:00', '15:30', 'Next.js 이미지 최적화',
 'Next.js Image 컴포넌트를 활용한 이미지 최적화 작업을 진행했습니다.',
 '- Image 컴포넌트로 전환
- 적절한 사이즈 설정
- lazy loading 적용',
 '- 외부 이미지 도메인 설정이 필요했습니다.',
 '- next.config.js에서 허용할 이미지 도메인을 명시해야 합니다.',
 'GOOD'),

(1, '2025-12-31', '16:00', '18:30', 'GitHub Actions CI/CD 설정',
 'GitHub Actions를 사용한 자동 배포 파이프라인을 구축했습니다.',
 '- 테스트 자동화
- 빌드 자동화
- Docker 이미지 생성 및 푸시',
 '- Docker Hub 인증 설정이 까다로웠습니다.
- 환경 변수 관리가 복잡했습니다.',
 '- GitHub Secrets를 활용하면 민감정보를 안전하게 관리할 수 있습니다.',
 'NEUTRAL'),

(1, '2025-12-31', '19:00', '20:00', '코드 리뷰 및 리팩토링',
 'Claude AI의 코드 리뷰를 통해 발견된 이슈들을 수정했습니다.',
 '- Critical Bug 1건 수정
- 중복 파일 제거
- 코드 품질 개선',
 '- 예상치 못한 버그가 발견되어 놀랐습니다.',
 '- 정기적인 코드 리뷰가 매우 중요합니다.',
 'GOOD');

-- 01/01 (수요일) - 프로젝트 1, 2개 로그
INSERT INTO dev_logs (project_id, log_date, start_time, end_time, title, description, achievements, challenges, learnings, mood) VALUES
(1, '2026-01-01', '14:00', '17:00', '새해 프로젝트 계획 수립',
 '2026년 DevLog 프로젝트 로드맵을 작성하고 우선순위를 정했습니다.',
 '- Q1 목표 설정
- 기능 우선순위 정리
- 기술 스택 검토',
 '- 구현할 기능이 너무 많아 우선순위 선정이 어려웠습니다.',
 '- MVP(Minimum Viable Product)에 집중하는 것이 중요합니다.',
 'GREAT'),

(1, '2026-01-01', '17:30', '19:00', '문서화 작업',
 'README.md 및 API 문서를 업데이트했습니다.',
 '- README 개선
- API 명세서 작성
- 설치 가이드 작성',
 '- 기술 문서 작성이 개발만큼 중요하다는 것을 느꼈습니다.',
 '- Markdown 문법을 잘 활용하면 가독성 높은 문서를 만들 수 있습니다.',
 'GOOD');

-- 01/02 (목요일) - 프로젝트 2, 1개 로그
INSERT INTO dev_logs (project_id, log_date, start_time, end_time, title, description, achievements, challenges, learnings, mood) VALUES
(2, '2026-01-02', '10:00', '14:00', 'SEO 최적화 작업',
 'Next.js의 Metadata API를 활용한 SEO 최적화 작업을 진행했습니다.',
 '- 메타 태그 설정
- Open Graph 태그 추가
- sitemap.xml 생성
- robots.txt 작성',
 '- 동적 메타데이터 생성이 복잡했습니다.',
 '- generateMetadata 함수를 사용하면 동적으로 메타데이터를 생성할 수 있습니다.',
 'NEUTRAL');

-- 01/03 (금요일) - 프로젝트 1, 3개 로그
INSERT INTO dev_logs (project_id, log_date, start_time, end_time, title, description, achievements, challenges, learnings, mood) VALUES
(1, '2026-01-03', '09:00', '11:00', 'Toast 알림 컴포넌트 구현',
 '사용자 액션에 대한 피드백을 제공하는 Toast 컴포넌트를 구현했습니다.',
 '- Toast 컴포넌트 작성
- 자동 사라짐 기능
- 다양한 타입 지원 (success, error, info)',
 '- 애니메이션 구현이 생각보다 복잡했습니다.',
 '- CSS transitions를 활용하면 부드러운 애니메이션을 만들 수 있습니다.',
 'GOOD'),

(1, '2026-01-03', '13:00', '16:00', 'GlobalSearch 컴포넌트 구현',
 '전역 검색 기능을 구현하여 로그와 프로젝트를 빠르게 찾을 수 있게 했습니다.',
 '- 검색 UI 구현
- 디바운싱 적용
- 키보드 단축키 지원',
 '- 검색 성능 최적화가 필요했습니다.',
 '- useMemo와 useCallback을 적절히 사용하면 불필요한 리렌더링을 방지할 수 있습니다.',
 'GREAT'),

(1, '2026-01-03', '16:30', '18:00', 'Error Boundary 구현',
 'React Error Boundary를 구현하여 예상치 못한 에러를 처리했습니다.',
 '- ErrorBoundary 컴포넌트 작성
- 에러 로깅 추가
- Fallback UI 구현',
 '- 비동기 에러는 Error Boundary로 잡을 수 없다는 것을 알았습니다.',
 '- try-catch와 Error Boundary를 함께 사용해야 완전한 에러 처리가 가능합니다.',
 'GOOD');

-- 01/04 (토요일) - 프로젝트 1, 2, 총 3개 로그
INSERT INTO dev_logs (project_id, log_date, start_time, end_time, title, description, achievements, challenges, learnings, mood) VALUES
(1, '2026-01-04', '10:00', '13:00', 'Skeleton 로딩 컴포넌트 구현',
 '데이터 로딩 중 사용자 경험을 개선하기 위한 Skeleton 컴포넌트를 구현했습니다.',
 '- Skeleton 컴포넌트 작성
- 다양한 타입 지원
- 애니메이션 효과 추가',
 '- 실제 콘텐츠와 비슷한 레이아웃을 만드는 것이 중요했습니다.',
 '- Skeleton UI는 체감 성능을 크게 향상시킵니다.',
 'GOOD'),

(2, '2026-01-04', '14:00', '17:00', '포트폴리오 배포 완료',
 'Vercel을 통해 포트폴리오 웹사이트를 배포했습니다.',
 '- Vercel 배포 설정
- 커스텀 도메인 연결
- 환경 변수 설정
- 배포 자동화',
 '- 도메인 DNS 설정이 처음이라 시간이 걸렸습니다.',
 '- Vercel은 Next.js와 완벽하게 통합되어 배포가 매우 간단합니다.',
 'GREAT'),

(1, '2026-01-04', '17:30', '19:00', '주간 회고 작성',
 '한 주간의 개발 내용을 정리하고 다음 주 계획을 수립했습니다.',
 '- 완료한 기능 정리
- 배운 점 기록
- 개선 사항 도출
- 다음 주 목표 설정',
 '- 회고 작성이 처음에는 어색했지만 점점 익숙해졌습니다.',
 '- 정기적인 회고는 성장에 큰 도움이 됩니다.',
 'GOOD');

-- ============================================================
-- LOG_TECH_TAGS 연결 데이터 (주간 테스트 데이터)
-- ============================================================
INSERT INTO log_tech_tags (log_id, tech_tag_id)
SELECT dl.id, tt.id
FROM dev_logs dl
CROSS JOIN tech_tags tt
WHERE dl.log_date BETWEEN '2025-12-29' AND '2026-01-04'
AND (
    -- 12/29 로그들
    (dl.log_date = '2025-12-29' AND dl.project_id = 1 AND tt.name IN ('Spring Boot', 'Java', 'MyBatis', 'PostgreSQL')) OR
    (dl.log_date = '2025-12-29' AND dl.project_id = 2 AND tt.name IN ('Next.js', 'TypeScript', 'Tailwind CSS')) OR

    -- 12/30 로그들
    (dl.log_date = '2025-12-30' AND dl.title LIKE '%통계 API%' AND tt.name IN ('PostgreSQL', 'MyBatis', 'Java')) OR
    (dl.log_date = '2025-12-30' AND dl.title LIKE '%MyBatis%' AND tt.name IN ('MyBatis', 'Java', 'SQL')) OR
    (dl.log_date = '2025-12-30' AND dl.title LIKE '%Postman%' AND tt.name IN ('Postman')) OR

    -- 12/31 로그들
    (dl.log_date = '2025-12-31' AND dl.title LIKE '%Dashboard%' AND tt.name IN ('React', 'JavaScript', 'Recharts')) OR
    (dl.log_date = '2025-12-31' AND dl.title LIKE '%이미지%' AND tt.name IN ('Next.js', 'TypeScript')) OR
    (dl.log_date = '2025-12-31' AND dl.title LIKE '%GitHub Actions%' AND tt.name IN ('Docker', 'Git', 'GitHub')) OR
    (dl.log_date = '2025-12-31' AND dl.title LIKE '%코드 리뷰%' AND tt.name IN ('Java', 'Spring Boot')) OR

    -- 01/01 로그들
    (dl.log_date = '2026-01-01' AND dl.title LIKE '%계획%' AND tt.name IN ('Java', 'React')) OR
    (dl.log_date = '2026-01-01' AND dl.title LIKE '%문서화%' AND tt.name IN ('Git', 'GitHub')) OR

    -- 01/02 로그
    (dl.log_date = '2026-01-02' AND tt.name IN ('Next.js', 'TypeScript', 'HTML')) OR

    -- 01/03 로그들
    (dl.log_date = '2026-01-03' AND dl.title LIKE '%Toast%' AND tt.name IN ('React', 'JavaScript', 'CSS')) OR
    (dl.log_date = '2026-01-03' AND dl.title LIKE '%GlobalSearch%' AND tt.name IN ('React', 'JavaScript', 'Axios')) OR
    (dl.log_date = '2026-01-03' AND dl.title LIKE '%Error Boundary%' AND tt.name IN ('React', 'JavaScript')) OR

    -- 01/04 로그들
    (dl.log_date = '2026-01-04' AND dl.title LIKE '%Skeleton%' AND tt.name IN ('React', 'JavaScript', 'CSS')) OR
    (dl.log_date = '2026-01-04' AND dl.title LIKE '%배포%' AND tt.name IN ('Next.js', 'Vercel', 'GitHub')) OR
    (dl.log_date = '2026-01-04' AND dl.title LIKE '%회고%' AND tt.name IN ('Git'))
);

-- ============================================================
-- PROJECT_STATS 주간 통계 데이터
-- ============================================================
INSERT INTO project_stats (project_id, stat_date, total_minutes, log_count) VALUES
-- 12/29
(1, '2025-12-29', 180, 1),  -- 3시간
(2, '2025-12-29', 180, 1),  -- 3시간

-- 12/30
(1, '2025-12-30', 360, 3),  -- 6시간 (2.5 + 3 + 1.5)

-- 12/31
(1, '2025-12-31', 330, 3),  -- 5.5시간 (3 + 2.5 + 1)
(2, '2025-12-31', 150, 1),  -- 2.5시간

-- 01/01
(1, '2026-01-01', 270, 2),  -- 4.5시간 (3 + 1.5)

-- 01/02
(2, '2026-01-02', 240, 1),  -- 4시간

-- 01/03
(1, '2026-01-03', 390, 3),  -- 6.5시간 (2 + 3 + 1.5)

-- 01/04
(1, '2026-01-04', 270, 2),  -- 4.5시간 (3 + 1.5)
(2, '2026-01-04', 180, 1);  -- 3시간

-- ============================================================
-- 데이터 검증 쿼리
-- ============================================================
SELECT
    '주간 테스트 데이터' as info,
    COUNT(*) as total_logs,
    COUNT(DISTINCT project_id) as projects,
    SUM(EXTRACT(EPOCH FROM (end_time - start_time)) / 60) as total_minutes
FROM dev_logs
WHERE log_date BETWEEN '2025-12-29' AND '2026-01-04';

-- 일별 통계
SELECT
    log_date,
    COUNT(*) as log_count,
    SUM(EXTRACT(EPOCH FROM (end_time - start_time)) / 60) as total_minutes
FROM dev_logs
WHERE log_date BETWEEN '2025-12-29' AND '2026-01-04'
GROUP BY log_date
ORDER BY log_date;

-- 프로젝트별 통계
SELECT
    p.name,
    COUNT(dl.id) as log_count,
    SUM(EXTRACT(EPOCH FROM (dl.end_time - dl.start_time)) / 60) as total_minutes
FROM projects p
LEFT JOIN dev_logs dl ON p.id = dl.project_id
WHERE dl.log_date BETWEEN '2025-12-29' AND '2026-01-04'
GROUP BY p.id, p.name
ORDER BY log_count DESC;

-- ============================================================
-- TEST DATA INSERTION COMPLETED
-- ============================================================
