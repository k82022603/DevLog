-- ============================================================
-- DevLog Sample Data for PostgreSQL 15
-- ============================================================
-- 샘플 데이터: 프로젝트, 개발 로그, 기술 태그
-- ============================================================

-- ============================================================
-- 1. PROJECTS 샘플 데이터
-- ============================================================
INSERT INTO projects (name, description, status, start_date, end_date, repository_url, progress, color) VALUES
('DevLog 프로젝트', '개발자를 위한 일일 개발 로그 및 프로젝트 관리 시스템. Spring Boot와 React를 사용한 풀스택 웹 애플리케이션.', 'ACTIVE', '2025-01-01', NULL, 'https://github.com/vibecoding/devlog', 45, '#a855f7'),
('포트폴리오 웹사이트', '개인 포트폴리오 웹사이트 제작. Next.js와 Tailwind CSS를 활용한 반응형 디자인.', 'ACTIVE', '2025-01-15', NULL, 'https://github.com/vibecoding/portfolio', 70, '#3b82f6'),
('E-commerce 플랫폼', '온라인 쇼핑몰 구축 프로젝트. 마이크로서비스 아키텍처 적용.', 'ON_HOLD', '2024-12-01', NULL, 'https://github.com/vibecoding/ecommerce', 30, '#10b981'),
('AI 챗봇 서비스', 'OpenAI API를 활용한 고객 지원 챗봇. Python FastAPI 백엔드.', 'COMPLETED', '2024-10-01', '2024-12-15', 'https://github.com/vibecoding/ai-chatbot', 100, '#f59e0b'),
('모바일 앱 프로토타입', 'React Native를 사용한 크로스 플랫폼 모바일 앱.', 'ARCHIVED', '2024-08-01', '2024-09-30', NULL, 85, '#8b5cf6');

-- ============================================================
-- 2. TECH_TAGS 샘플 데이터
-- ============================================================
INSERT INTO tech_tags (name, category, color) VALUES
-- Languages
('Java', 'LANGUAGE', '#007396'),
('JavaScript', 'LANGUAGE', '#f7df1e'),
('TypeScript', 'LANGUAGE', '#3178c6'),
('Python', 'LANGUAGE', '#3776ab'),
('SQL', 'LANGUAGE', '#4479a1'),
('HTML', 'LANGUAGE', '#e34f26'),
('CSS', 'LANGUAGE', '#1572b6'),

-- Frameworks
('Spring Boot', 'FRAMEWORK', '#6db33f'),
('React', 'FRAMEWORK', '#61dafb'),
('Next.js', 'FRAMEWORK', '#000000'),
('FastAPI', 'FRAMEWORK', '#009688'),
('Express', 'FRAMEWORK', '#000000'),
('React Native', 'FRAMEWORK', '#61dafb'),
('Tailwind CSS', 'FRAMEWORK', '#06b6d4'),

-- Databases
('PostgreSQL', 'DATABASE', '#4169e1'),
('MongoDB', 'DATABASE', '#47a248'),
('Redis', 'DATABASE', '#dc382d'),
('H2', 'DATABASE', '#0000ff'),

-- Tools
('Git', 'TOOL', '#f05032'),
('Docker', 'TOOL', '#2496ed'),
('Postman', 'TOOL', '#ff6c37'),
('VS Code', 'TOOL', '#007acc'),
('IntelliJ IDEA', 'TOOL', '#000000'),

-- Libraries
('MyBatis', 'LIBRARY', '#ba3329'),
('Axios', 'LIBRARY', '#5a29e4'),
('Recharts', 'LIBRARY', '#8884d8'),
('Lodash', 'LIBRARY', '#3492ff'),

-- Platforms
('AWS', 'PLATFORM', '#ff9900'),
('Vercel', 'PLATFORM', '#000000'),
('Heroku', 'PLATFORM', '#430098'),
('GitHub', 'PLATFORM', '#181717');

-- ============================================================
-- 3. DEV_LOGS 샘플 데이터
-- ============================================================

-- DevLog 프로젝트 로그
INSERT INTO dev_logs (project_id, log_date, start_time, end_time, title, description, achievements, challenges, learnings, code_snippets, mood) VALUES
(1, '2025-01-20', '09:00', '12:00', 'Spring Boot 프로젝트 초기 설정',
 'Spring Boot 3.2.1과 MyBatis를 이용한 백엔드 프로젝트 초기 설정을 완료했습니다. PostgreSQL 데이터베이스 연동 및 기본 구조를 생성했습니다.',
 '- Spring Boot 프로젝트 생성
- MyBatis 설정 완료
- PostgreSQL 연동 성공
- 기본 엔티티 및 매퍼 작성',
 '- MyBatis XML 매퍼 설정이 처음이라 레퍼런스를 많이 참고했습니다.
- PostgreSQL 연결 설정에서 포트 충돌 문제가 있었습니다.',
 '- MyBatis의 resultMap을 사용하면 카멜케이스 변환이 자동으로 됩니다.
- Spring Boot 3.x부터는 javax가 jakarta로 변경되었습니다.',
 '[
   {
     "language": "java",
     "code": "@Mapper\npublic interface DevLogMapper {\n    List<DevLog> findAll();\n    Optional<DevLog> findById(Long id);\n}",
     "description": "MyBatis 매퍼 인터페이스"
   }
 ]'::jsonb,
 'GOOD'),

(1, '2025-01-21', '14:00', '18:00', 'React 프론트엔드 구조 설계',
 'React 18.2 기반 프론트엔드 프로젝트 생성. Tailwind CSS를 이용한 UI 구성 및 라우팅 설정을 완료했습니다.',
 '- React 프로젝트 초기 설정
- Tailwind CSS 설정
- React Router 설정
- 기본 레이아웃 컴포넌트 작성',
 '- Tailwind CSS 설정 파일 구조를 이해하는데 시간이 걸렸습니다.
- Glassmorphism 효과 구현이 생각보다 복잡했습니다.',
 '- Tailwind의 @layer 지시어를 사용하면 커스텀 유틸리티 클래스를 만들 수 있습니다.
- backdrop-filter를 사용하면 유리 효과를 구현할 수 있습니다.',
 '[
   {
     "language": "css",
     "code": ".glass {\n  background: rgba(255, 255, 255, 0.1);\n  backdrop-filter: blur(10px);\n  border: 1px solid rgba(255, 255, 255, 0.2);\n}",
     "description": "Glassmorphism 스타일"
   }
 ]'::jsonb,
 'GREAT'),

(1, '2025-01-22', '10:00', '13:00', '데이터베이스 스키마 설계 및 구현',
 '프로젝트의 전체 데이터베이스 스키마를 설계하고 PostgreSQL에 구현했습니다. 5개의 테이블과 관계를 정의했습니다.',
 '- ERD 설계 완료
- 테이블 생성 SQL 작성
- 인덱스 및 제약조건 추가
- 트리거 함수 작성',
 '- 다대다 관계를 중간 테이블로 구현하는 것이 처음에 헷갈렸습니다.
- JSONB 타입 사용 여부를 결정하는데 고민이 많았습니다.',
 '- PostgreSQL의 JSONB 타입은 인덱싱이 가능해서 성능이 좋습니다.
- 트리거를 사용하면 updated_at을 자동으로 관리할 수 있습니다.',
 NULL,
 'NEUTRAL'),

-- 포트폴리오 프로젝트 로그
(2, '2025-01-18', '15:00', '19:00', '포트폴리오 디자인 시안 작업',
 'Figma를 이용한 포트폴리오 웹사이트 디자인 시안 작업. 메인 페이지, About, Projects, Contact 페이지 레이아웃을 완성했습니다.',
 '- 와이어프레임 작성
- 컬러 팔레트 선정
- 타이포그래피 정의
- 주요 페이지 디자인 완료',
 '- 다크모드와 라이트모드 둘 다 디자인하는 것이 시간이 오래 걸렸습니다.
- 반응형 디자인을 고려하며 작업하는 것이 복잡했습니다.',
 '- Figma의 Auto Layout 기능을 사용하면 반응형 디자인이 쉬워집니다.
- 8px 그리드 시스템을 사용하면 일관성 있는 디자인이 가능합니다.',
 NULL,
 'GOOD'),

(2, '2025-01-19', '10:00', '14:00', 'Next.js 프로젝트 초기 설정',
 'Next.js 14를 사용한 프로젝트 초기 설정. App Router를 사용하여 페이지 구조를 설계했습니다.',
 '- Next.js 14 프로젝트 생성
- TypeScript 설정
- Tailwind CSS 통합
- 기본 페이지 라우팅 구현',
 '- App Router와 Pages Router의 차이를 이해하는데 시간이 걸렸습니다.
- Server Component와 Client Component의 구분이 헷갈렸습니다.',
 '- use client 지시어는 클라이언트 컴포넌트를 명시할 때 사용합니다.
- Next.js 14부터는 기본적으로 모든 컴포넌트가 서버 컴포넌트입니다.',
 '[
   {
     "language": "typescript",
     "code": "// app/layout.tsx\nexport default function RootLayout({\n  children,\n}: {\n  children: React.ReactNode\n}) {\n  return (\n    <html lang=\"ko\">\n      <body>{children}</body>\n    </html>\n  )\n}",
     "description": "Next.js 루트 레이아웃"
   }
 ]'::jsonb,
 'GREAT'),

-- E-commerce 프로젝트 로그
(3, '2024-12-15', '09:00', '17:00', '마이크로서비스 아키텍처 설계',
 '온라인 쇼핑몰의 마이크로서비스 아키텍처를 설계했습니다. 사용자, 상품, 주문, 결제 서비스로 분리했습니다.',
 '- 도메인별 서비스 분리
- API Gateway 설계
- 서비스 간 통신 방식 결정
- 데이터베이스 분리 전략 수립',
 '- 서비스 간 트랜잭션 관리가 가장 어려웠습니다.
- 분산 환경에서의 데이터 일관성 문제를 고민했습니다.',
 '- Saga 패턴을 사용하면 분산 트랜잭션을 관리할 수 있습니다.
- 이벤트 소싱을 활용하면 데이터 변경 이력을 추적할 수 있습니다.',
 NULL,
 'NEUTRAL'),

-- AI 챗봇 프로젝트 로그
(4, '2024-12-10', '13:00', '18:00', 'OpenAI API 통합',
 'FastAPI 백엔드에 OpenAI API를 통합하여 챗봇 기능을 구현했습니다. GPT-4를 사용하여 고객 문의에 답변합니다.',
 '- OpenAI API 연동
- 프롬프트 엔지니어링
- 스트리밍 응답 구현
- 토큰 사용량 최적화',
 '- API 비용 관리가 중요했습니다.
- 프롬프트 작성이 생각보다 어려웠습니다.
- 스트리밍 응답 구현 시 에러 처리가 복잡했습니다.',
 '- 시스템 프롬프트를 잘 작성하면 응답 품질이 크게 향상됩니다.
- 토큰 제한을 두면 비용을 효과적으로 관리할 수 있습니다.',
 '[
   {
     "language": "python",
     "code": "async def stream_chat_response(message: str):\n    response = await openai.ChatCompletion.acreate(\n        model=\"gpt-4\",\n        messages=[{\"role\": \"user\", \"content\": message}],\n        stream=True\n    )\n    async for chunk in response:\n        yield chunk",
     "description": "OpenAI 스트리밍 응답"
   }
 ]'::jsonb,
 'GOOD'),

(4, '2024-12-14', '10:00', '15:00', '챗봇 UI 개선 및 배포',
 '사용자 친화적인 챗봇 인터페이스를 구현하고 AWS에 배포했습니다.',
 '- 채팅 UI 개선
- 타이핑 애니메이션 추가
- 코드 하이라이팅 기능
- AWS EC2에 배포',
 '- WebSocket 연결이 자주 끊기는 문제가 있었습니다.
- 배포 환경 설정이 복잡했습니다.',
 '- Nginx를 리버스 프록시로 사용하면 WebSocket 안정성이 향상됩니다.
- Docker를 사용하면 배포가 훨씬 간단해집니다.',
 NULL,
 'GREAT'),

-- 모바일 앱 프로젝트 로그
(5, '2024-09-20', '09:00', '16:00', 'React Native 앱 초기 개발',
 'React Native로 크로스 플랫폼 모바일 앱을 개발하기 시작했습니다. iOS와 Android 동시 개발.',
 '- React Native 프로젝트 설정
- 네비게이션 구현
- 기본 화면 작성
- API 연동',
 '- iOS 시뮬레이터 설정이 어려웠습니다.
- 플랫폼별로 다른 동작을 처리하는 것이 복잡했습니다.',
 '- Platform.select를 사용하면 플랫폼별 코드를 쉽게 작성할 수 있습니다.
- Expo를 사용하면 초기 설정이 간단해집니다.',
 NULL,
 'NEUTRAL'),

(5, '2024-09-28', '14:00', '18:00', '앱 스토어 배포 준비',
 'iOS App Store와 Google Play Store 배포를 위한 준비 작업을 진행했습니다.',
 '- 앱 아이콘 및 스플래시 스크린 제작
- 스토어 스크린샷 준비
- 개인정보 처리방침 작성
- 배포 설정',
 '- App Store 심사 가이드라인이 매우 까다로웠습니다.
- 앱 서명 및 인증서 관리가 복잡했습니다.',
 '- Fastlane을 사용하면 배포 자동화가 가능합니다.
- TestFlight를 활용하면 베타 테스트가 쉬워집니다.',
 NULL,
 'BAD');

-- ============================================================
-- 4. LOG_TECH_TAGS 연결 데이터
-- ============================================================
INSERT INTO log_tech_tags (log_id, tech_tag_id) VALUES
-- 로그 1: Spring Boot 프로젝트 초기 설정
(1, (SELECT id FROM tech_tags WHERE name = 'Java')),
(1, (SELECT id FROM tech_tags WHERE name = 'Spring Boot')),
(1, (SELECT id FROM tech_tags WHERE name = 'MyBatis')),
(1, (SELECT id FROM tech_tags WHERE name = 'PostgreSQL')),

-- 로그 2: React 프론트엔드
(2, (SELECT id FROM tech_tags WHERE name = 'React')),
(2, (SELECT id FROM tech_tags WHERE name = 'JavaScript')),
(2, (SELECT id FROM tech_tags WHERE name = 'Tailwind CSS')),
(2, (SELECT id FROM tech_tags WHERE name = 'HTML')),
(2, (SELECT id FROM tech_tags WHERE name = 'CSS')),

-- 로그 3: 데이터베이스 스키마
(3, (SELECT id FROM tech_tags WHERE name = 'PostgreSQL')),
(3, (SELECT id FROM tech_tags WHERE name = 'SQL')),

-- 로그 4: 포트폴리오 디자인
(4, (SELECT id FROM tech_tags WHERE name = 'HTML')),
(4, (SELECT id FROM tech_tags WHERE name = 'CSS')),

-- 로그 5: Next.js
(5, (SELECT id FROM tech_tags WHERE name = 'Next.js')),
(5, (SELECT id FROM tech_tags WHERE name = 'TypeScript')),
(5, (SELECT id FROM tech_tags WHERE name = 'React')),
(5, (SELECT id FROM tech_tags WHERE name = 'Tailwind CSS')),

-- 로그 6: 마이크로서비스
(6, (SELECT id FROM tech_tags WHERE name = 'Docker')),
(6, (SELECT id FROM tech_tags WHERE name = 'PostgreSQL')),
(6, (SELECT id FROM tech_tags WHERE name = 'MongoDB')),

-- 로그 7: OpenAI API
(7, (SELECT id FROM tech_tags WHERE name = 'Python')),
(7, (SELECT id FROM tech_tags WHERE name = 'FastAPI')),

-- 로그 8: 챗봇 배포
(8, (SELECT id FROM tech_tags WHERE name = 'AWS')),
(8, (SELECT id FROM tech_tags WHERE name = 'Docker')),

-- 로그 9: React Native
(9, (SELECT id FROM tech_tags WHERE name = 'React Native')),
(9, (SELECT id FROM tech_tags WHERE name = 'JavaScript')),

-- 로그 10: 앱 스토어 배포
(10, (SELECT id FROM tech_tags WHERE name = 'React Native'));

-- ============================================================
-- 5. PROJECT_STATS 샘플 데이터 (집계 데이터)
-- ============================================================
INSERT INTO project_stats (project_id, stat_date, total_minutes, log_count) VALUES
-- DevLog 프로젝트 통계
(1, '2025-01-20', 180, 1),  -- 3시간
(1, '2025-01-21', 240, 1),  -- 4시간
(1, '2025-01-22', 180, 1),  -- 3시간

-- 포트폴리오 프로젝트 통계
(2, '2025-01-18', 240, 1),  -- 4시간
(2, '2025-01-19', 240, 1),  -- 4시간

-- E-commerce 프로젝트 통계
(3, '2024-12-15', 480, 1),  -- 8시간

-- AI 챗봇 프로젝트 통계
(4, '2024-12-10', 300, 1),  -- 5시간
(4, '2024-12-14', 300, 1),  -- 5시간

-- 모바일 앱 프로젝트 통계
(5, '2024-09-20', 420, 1),  -- 7시간
(5, '2024-09-28', 240, 1);  -- 4시간

-- ============================================================
-- 데이터 검증 쿼리
-- ============================================================
SELECT 'Projects inserted:' as info, COUNT(*) as count FROM projects;
SELECT 'Tech tags inserted:' as info, COUNT(*) as count FROM tech_tags;
SELECT 'Dev logs inserted:' as info, COUNT(*) as count FROM dev_logs;
SELECT 'Log-tag connections:' as info, COUNT(*) as count FROM log_tech_tags;
SELECT 'Project stats records:' as info, COUNT(*) as count FROM project_stats;

-- ============================================================
-- 유용한 조회 쿼리 예제
-- ============================================================

-- 프로젝트별 통계 보기
SELECT * FROM v_project_summary;

-- 로그 상세 정보 (태그 포함)
SELECT * FROM v_log_details ORDER BY log_date DESC;

-- 기술 태그 사용 통계
SELECT * FROM v_tag_statistics ORDER BY usage_count DESC;

-- 최근 7일간의 로그
SELECT
    log_date,
    title,
    mood,
    EXTRACT(EPOCH FROM (end_time - start_time)) / 60 as minutes_worked
FROM dev_logs
WHERE log_date >= CURRENT_DATE - INTERVAL '7 days'
ORDER BY log_date DESC;

-- 기분별 로그 통계
SELECT
    mood,
    COUNT(*) as log_count,
    AVG(EXTRACT(EPOCH FROM (end_time - start_time)) / 60) as avg_minutes
FROM dev_logs
WHERE start_time IS NOT NULL AND end_time IS NOT NULL
GROUP BY mood
ORDER BY log_count DESC;

-- ============================================================
-- SAMPLE DATA INSERTION COMPLETED
-- ============================================================
