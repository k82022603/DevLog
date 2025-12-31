-- ============================================================
-- DevLog Database Schema for PostgreSQL 15
-- ============================================================
-- 개발자를 위한 일일 개발 로그 및 프로젝트 관리 시스템
-- Version: 2.0
-- Last Updated: 2025-01-20
-- ============================================================

-- Drop tables if exists (역순으로 삭제)
DROP TABLE IF EXISTS project_stats CASCADE;
DROP TABLE IF EXISTS log_tech_tags CASCADE;
DROP TABLE IF EXISTS tech_tags CASCADE;
DROP TABLE IF EXISTS dev_logs CASCADE;
DROP TABLE IF EXISTS projects CASCADE;

-- ============================================================
-- 1. PROJECTS TABLE (프로젝트 정보)
-- ============================================================
CREATE TABLE projects (
    -- 기본 정보
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,

    -- 프로젝트 상태
    status VARCHAR(50) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'COMPLETED', 'ON_HOLD', 'ARCHIVED')),

    -- 날짜 정보
    start_date DATE,
    end_date DATE,

    -- 추가 정보
    repository_url VARCHAR(500),
    progress INTEGER DEFAULT 0 CHECK (progress >= 0 AND progress <= 100),
    color VARCHAR(7) DEFAULT '#a855f7',  -- 헥스 컬러 코드

    -- 메타데이터
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. DEV_LOGS TABLE (개발 로그)
-- ============================================================
CREATE TABLE dev_logs (
    -- 기본 정보
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,

    -- 날짜 및 시간
    log_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,

    -- 로그 내용
    title VARCHAR(500) NOT NULL,
    description TEXT NOT NULL,
    achievements TEXT,      -- 성과/완료한 작업
    challenges TEXT,        -- 어려웠던 점
    learnings TEXT,         -- 배운 점/깨달음

    -- 코드 스니펫 (JSON 형식)
    code_snippets JSONB,

    -- 기분/컨디션
    mood VARCHAR(20) DEFAULT 'NEUTRAL' CHECK (mood IN ('GREAT', 'GOOD', 'NEUTRAL', 'BAD', 'TERRIBLE')),

    -- 메타데이터
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key
    CONSTRAINT fk_dev_logs_projects
        FOREIGN KEY (project_id)
        REFERENCES projects(id)
        ON DELETE CASCADE
);

-- ============================================================
-- 3. TECH_TAGS TABLE (기술 태그)
-- ============================================================
CREATE TABLE tech_tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) DEFAULT 'OTHER' CHECK (category IN ('LANGUAGE', 'FRAMEWORK', 'DATABASE', 'TOOL', 'LIBRARY', 'PLATFORM', 'OTHER')),
    color VARCHAR(7) DEFAULT '#3b82f6',  -- 헥스 컬러 코드
    usage_count INTEGER DEFAULT 0,       -- 사용 횟수 (캐시)

    -- 메타데이터
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 4. LOG_TECH_TAGS TABLE (로그-태그 연결)
-- ============================================================
CREATE TABLE log_tech_tags (
    log_id BIGINT NOT NULL,
    tech_tag_id BIGINT NOT NULL,

    -- 복합 Primary Key
    PRIMARY KEY (log_id, tech_tag_id),

    -- Foreign Keys
    CONSTRAINT fk_log_tech_tags_logs
        FOREIGN KEY (log_id)
        REFERENCES dev_logs(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_log_tech_tags_tags
        FOREIGN KEY (tech_tag_id)
        REFERENCES tech_tags(id)
        ON DELETE CASCADE
);

-- ============================================================
-- 5. PROJECT_STATS TABLE (프로젝트 통계 - 집계용)
-- ============================================================
CREATE TABLE project_stats (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    stat_date DATE NOT NULL,

    -- 통계 정보
    total_minutes INTEGER DEFAULT 0,  -- 총 작업 시간 (분)
    log_count INTEGER DEFAULT 0,      -- 로그 개수

    -- 메타데이터
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key
    CONSTRAINT fk_project_stats_projects
        FOREIGN KEY (project_id)
        REFERENCES projects(id)
        ON DELETE CASCADE,

    -- 유니크 제약조건 (프로젝트별 날짜별 하나의 통계만)
    UNIQUE (project_id, stat_date)
);

-- ============================================================
-- INDEXES (성능 최적화)
-- ============================================================

-- Projects 테이블 인덱스
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_start_date ON projects(start_date);
CREATE INDEX idx_projects_created_at ON projects(created_at);

-- Dev_logs 테이블 인덱스
CREATE INDEX idx_dev_logs_project_id ON dev_logs(project_id);
CREATE INDEX idx_dev_logs_log_date ON dev_logs(log_date);
CREATE INDEX idx_dev_logs_created_at ON dev_logs(created_at);
CREATE INDEX idx_dev_logs_mood ON dev_logs(mood);
CREATE INDEX idx_dev_logs_project_date ON dev_logs(project_id, log_date);  -- 복합 인덱스

-- Tech_tags 테이블 인덱스
CREATE INDEX idx_tech_tags_category ON tech_tags(category);
CREATE INDEX idx_tech_tags_usage_count ON tech_tags(usage_count DESC);
CREATE INDEX idx_tech_tags_name ON tech_tags(name);  -- 검색용

-- Log_tech_tags 테이블 인덱스
CREATE INDEX idx_log_tech_tags_tag_id ON log_tech_tags(tech_tag_id);  -- 역방향 조회용

-- Project_stats 테이블 인덱스
CREATE INDEX idx_project_stats_project_id ON project_stats(project_id);
CREATE INDEX idx_project_stats_stat_date ON project_stats(stat_date);
CREATE INDEX idx_project_stats_project_date ON project_stats(project_id, stat_date);  -- 복합 인덱스

-- ============================================================
-- COMMENTS (테이블 및 컬럼 설명)
-- ============================================================

-- Projects 테이블
COMMENT ON TABLE projects IS '프로젝트 정보를 관리하는 테이블';
COMMENT ON COLUMN projects.status IS '프로젝트 상태: ACTIVE(진행중), COMPLETED(완료), ON_HOLD(보류), ARCHIVED(아카이브)';
COMMENT ON COLUMN projects.progress IS '프로젝트 진행률 (0-100%)';
COMMENT ON COLUMN projects.color IS '프로젝트 대표 색상 (헥스 코드)';

-- Dev_logs 테이블
COMMENT ON TABLE dev_logs IS '일일 개발 로그를 저장하는 테이블';
COMMENT ON COLUMN dev_logs.log_date IS '로그 작성 날짜';
COMMENT ON COLUMN dev_logs.start_time IS '작업 시작 시간';
COMMENT ON COLUMN dev_logs.end_time IS '작업 종료 시간';
COMMENT ON COLUMN dev_logs.achievements IS '완료한 작업 및 성과';
COMMENT ON COLUMN dev_logs.challenges IS '어려웠던 점 및 문제';
COMMENT ON COLUMN dev_logs.learnings IS '배운 점 및 깨달음';
COMMENT ON COLUMN dev_logs.code_snippets IS '코드 스니펫 (JSON 형식: [{language, code, description}])';
COMMENT ON COLUMN dev_logs.mood IS '작업 시 기분/컨디션: GREAT, GOOD, NEUTRAL, BAD, TERRIBLE';

-- Tech_tags 테이블
COMMENT ON TABLE tech_tags IS '기술 태그 마스터 테이블';
COMMENT ON COLUMN tech_tags.category IS '태그 카테고리: LANGUAGE, FRAMEWORK, DATABASE, TOOL, LIBRARY, PLATFORM, OTHER';
COMMENT ON COLUMN tech_tags.usage_count IS '태그 사용 횟수 (캐시된 값)';

-- Log_tech_tags 테이블
COMMENT ON TABLE log_tech_tags IS '개발 로그와 기술 태그의 다대다 관계 테이블';

-- Project_stats 테이블
COMMENT ON TABLE project_stats IS '프로젝트별 일일 통계 집계 테이블';
COMMENT ON COLUMN project_stats.total_minutes IS '해당 날짜의 총 작업 시간 (분 단위)';
COMMENT ON COLUMN project_stats.log_count IS '해당 날짜의 로그 개수';

-- ============================================================
-- FUNCTIONS & TRIGGERS (자동화)
-- ============================================================

-- 1. updated_at 자동 업데이트 함수
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 2. updated_at 트리거 적용
CREATE TRIGGER update_projects_updated_at
    BEFORE UPDATE ON projects
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_dev_logs_updated_at
    BEFORE UPDATE ON dev_logs
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_tech_tags_updated_at
    BEFORE UPDATE ON tech_tags
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_project_stats_updated_at
    BEFORE UPDATE ON project_stats
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 3. tech_tags usage_count 자동 증가 함수
CREATE OR REPLACE FUNCTION increment_tag_usage_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE tech_tags
    SET usage_count = usage_count + 1
    WHERE id = NEW.tech_tag_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER increment_tag_usage
    AFTER INSERT ON log_tech_tags
    FOR EACH ROW
    EXECUTE FUNCTION increment_tag_usage_count();

-- 4. tech_tags usage_count 자동 감소 함수
CREATE OR REPLACE FUNCTION decrement_tag_usage_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE tech_tags
    SET usage_count = GREATEST(0, usage_count - 1)
    WHERE id = OLD.tech_tag_id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER decrement_tag_usage
    AFTER DELETE ON log_tech_tags
    FOR EACH ROW
    EXECUTE FUNCTION decrement_tag_usage_count();

-- ============================================================
-- VIEWS (자주 사용하는 조회 쿼리)
-- ============================================================

-- 1. 프로젝트별 전체 통계 뷰
CREATE OR REPLACE VIEW v_project_summary AS
SELECT
    p.id,
    p.name,
    p.status,
    p.progress,
    p.color,
    COUNT(DISTINCT dl.id) as total_logs,
    COUNT(DISTINCT DATE(dl.log_date)) as active_days,
    COALESCE(SUM(
        CASE
            WHEN dl.start_time IS NOT NULL AND dl.end_time IS NOT NULL
            THEN EXTRACT(EPOCH FROM (dl.end_time - dl.start_time)) / 60
            ELSE 0
        END
    ), 0) as total_minutes,
    MAX(dl.log_date) as last_log_date,
    p.created_at,
    p.updated_at
FROM projects p
LEFT JOIN dev_logs dl ON p.id = dl.project_id
GROUP BY p.id, p.name, p.status, p.progress, p.color, p.created_at, p.updated_at;

COMMENT ON VIEW v_project_summary IS '프로젝트별 요약 통계 뷰';

-- 2. 로그 상세 정보 (태그 포함) 뷰
CREATE OR REPLACE VIEW v_log_details AS
SELECT
    dl.id,
    dl.project_id,
    p.name as project_name,
    p.color as project_color,
    dl.log_date,
    dl.start_time,
    dl.end_time,
    dl.title,
    dl.description,
    dl.achievements,
    dl.challenges,
    dl.learnings,
    dl.mood,
    dl.code_snippets,
    ARRAY_AGG(DISTINCT tt.name ORDER BY tt.name) FILTER (WHERE tt.name IS NOT NULL) as tags,
    dl.created_at,
    dl.updated_at
FROM dev_logs dl
LEFT JOIN projects p ON dl.project_id = p.id
LEFT JOIN log_tech_tags ltt ON dl.id = ltt.log_id
LEFT JOIN tech_tags tt ON ltt.tech_tag_id = tt.id
GROUP BY
    dl.id, dl.project_id, p.name, p.color, dl.log_date,
    dl.start_time, dl.end_time, dl.title, dl.description,
    dl.achievements, dl.challenges, dl.learnings, dl.mood,
    dl.code_snippets, dl.created_at, dl.updated_at;

COMMENT ON VIEW v_log_details IS '개발 로그 상세 정보 (프로젝트 및 태그 포함) 뷰';

-- 3. 기술 태그별 통계 뷰
CREATE OR REPLACE VIEW v_tag_statistics AS
SELECT
    tt.id,
    tt.name,
    tt.category,
    tt.color,
    tt.usage_count,
    COUNT(DISTINCT ltt.log_id) as actual_usage_count,
    COUNT(DISTINCT dl.project_id) as projects_count,
    MAX(dl.log_date) as last_used_date,
    tt.created_at
FROM tech_tags tt
LEFT JOIN log_tech_tags ltt ON tt.id = ltt.tech_tag_id
LEFT JOIN dev_logs dl ON ltt.log_id = dl.id
GROUP BY tt.id, tt.name, tt.category, tt.color, tt.usage_count, tt.created_at;

COMMENT ON VIEW v_tag_statistics IS '기술 태그별 사용 통계 뷰';

-- ============================================================
-- SCHEMA CREATION COMPLETED
-- ============================================================
