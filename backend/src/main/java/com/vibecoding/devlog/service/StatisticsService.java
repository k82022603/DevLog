package com.vibecoding.devlog.service;

import com.vibecoding.devlog.dto.response.MonthlyStatsDTO;
import com.vibecoding.devlog.dto.response.ProjectStatsDTO;
import com.vibecoding.devlog.dto.response.TechStackStatsDTO;
import com.vibecoding.devlog.dto.response.WeeklyStatsDTO;
import com.vibecoding.devlog.mapper.ProjectMapper;
import com.vibecoding.devlog.mapper.StatisticsMapper;
import com.vibecoding.devlog.model.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

/**
 * 통계 서비스
 *
 * 개발 로그 통계 관련 비즈니스 로직을 처리합니다.
 * 주간, 월간, 프로젝트별, 기술 스택 통계를 제공합니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final StatisticsMapper statisticsMapper;
    private final ProjectMapper projectMapper;

    /**
     * 주간 통계 조회
     *
     * @param startDate 시작 날짜 (null일 경우 이번 주 월요일)
     * @return 주간 통계 DTO
     */
    public WeeklyStatsDTO getWeeklyStats(LocalDate startDate) {
        log.debug("Getting weekly statistics for startDate: {}", startDate);

        // startDate가 null이면 이번 주 월요일로 설정
        LocalDate weekStart = (startDate != null)
                ? startDate
                : LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // 주의 마지막 날 (일요일) 계산
        LocalDate weekEnd = weekStart.plusDays(6);

        log.debug("Calculated week range: {} to {}", weekStart, weekEnd);

        // 기본 통계 조회
        Map<String, Object> basicStats = statisticsMapper.getWeeklyBasicStats(weekStart, weekEnd);

        // 일별 카운트 조회
        List<WeeklyStatsDTO.DailyCount> dailyCounts = statisticsMapper.getWeeklyDailyCounts(weekStart, weekEnd);

        // 프로젝트별 카운트 조회
        List<WeeklyStatsDTO.ProjectCount> projectCounts = statisticsMapper.getWeeklyProjectCounts(weekStart, weekEnd);

        // DTO 생성
        WeeklyStatsDTO stats = WeeklyStatsDTO.builder()
                .startDate(weekStart)
                .endDate(weekEnd)
                .totalLogs(getIntegerValue(basicStats, "totallogs"))
                .totalWorkMinutes(getIntegerValue(basicStats, "totalworkminutes"))
                .avgWorkMinutes(getIntegerValue(basicStats, "avgworkminutes"))
                .activeProjects(getIntegerValue(basicStats, "activeprojects"))
                .dailyCounts(dailyCounts)
                .projectCounts(projectCounts)
                .build();

        log.info("Weekly stats retrieved: {} logs, {} minutes", stats.getTotalLogs(), stats.getTotalWorkMinutes());

        return stats;
    }

    /**
     * 월간 통계 조회
     *
     * @param year 연도 (0 이하일 경우 현재 연도)
     * @param month 월 (0 이하일 경우 현재 월)
     * @return 월간 통계 DTO
     */
    public MonthlyStatsDTO getMonthlyStats(int year, int month) {
        log.debug("Getting monthly statistics for year: {}, month: {}", year, month);

        // 연도/월 기본값 설정
        LocalDate now = LocalDate.now();
        int targetYear = (year > 0) ? year : now.getYear();
        int targetMonth = (month > 0) ? month : now.getMonthValue();

        // 월의 유효성 검증
        if (targetMonth < 1 || targetMonth > 12) {
            throw new IllegalArgumentException("Invalid month: " + targetMonth + ". Must be between 1 and 12.");
        }

        log.debug("Target date: {}-{}", targetYear, targetMonth);

        // 기본 통계 조회
        Map<String, Object> basicStats = statisticsMapper.getMonthlyBasicStats(targetYear, targetMonth);

        // 일별 카운트 조회
        List<MonthlyStatsDTO.DailyCount> dailyCounts = statisticsMapper.getMonthlyDailyCounts(targetYear, targetMonth);

        // 프로젝트별 카운트 조회
        List<MonthlyStatsDTO.ProjectCount> projectCounts = statisticsMapper.getMonthlyProjectCounts(targetYear, targetMonth);

        // 주별 카운트 조회
        List<MonthlyStatsDTO.WeeklyCount> weeklyCounts = statisticsMapper.getMonthlyWeeklyCounts(targetYear, targetMonth);

        // DTO 생성
        MonthlyStatsDTO stats = MonthlyStatsDTO.builder()
                .year(targetYear)
                .month(targetMonth)
                .totalLogs(getIntegerValue(basicStats, "totallogs"))
                .totalWorkMinutes(getIntegerValue(basicStats, "totalworkminutes"))
                .avgWorkMinutes(getIntegerValue(basicStats, "avgworkminutes"))
                .activeProjects(getIntegerValue(basicStats, "activeprojects"))
                .workDays(getIntegerValue(basicStats, "workdays"))
                .dailyCounts(dailyCounts)
                .projectCounts(projectCounts)
                .weeklyCounts(weeklyCounts)
                .build();

        log.info("Monthly stats retrieved: {} logs, {} minutes, {} work days",
                stats.getTotalLogs(), stats.getTotalWorkMinutes(), stats.getWorkDays());

        return stats;
    }

    /**
     * 프로젝트별 통계 조회
     *
     * @param projectId 프로젝트 ID
     * @return 프로젝트 통계 DTO
     * @throws IllegalArgumentException 프로젝트가 존재하지 않는 경우
     */
    public ProjectStatsDTO getProjectStats(Long projectId) {
        log.debug("Getting project statistics for projectId: {}", projectId);

        if (projectId == null) {
            throw new IllegalArgumentException("Project ID is required");
        }

        // 프로젝트 존재 확인
        Project project = projectMapper.findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project not found with id: " + projectId);
        }

        // 기본 통계 조회
        Map<String, Object> basicStats = statisticsMapper.getProjectBasicStats(projectId);

        // 일별 카운트 조회
        List<ProjectStatsDTO.DailyCount> dailyCounts = statisticsMapper.getProjectDailyCounts(projectId);

        // 기술 태그별 카운트 조회
        List<ProjectStatsDTO.TechTagCount> techTagCounts = statisticsMapper.getProjectTechTagCounts(projectId);

        // 주간 활동 조회
        List<ProjectStatsDTO.WeeklyActivity> weeklyActivities = statisticsMapper.getProjectWeeklyActivities(projectId);

        // DTO 생성
        ProjectStatsDTO stats = ProjectStatsDTO.builder()
                .projectId(projectId)
                .projectName(getStringValue(basicStats, "projectname"))
                .projectDescription(getStringValue(basicStats, "projectdescription"))
                .projectStatus(getStringValue(basicStats, "projectstatus"))
                .projectProgress(getIntegerValue(basicStats, "projectprogress"))
                .projectStartDate(getLocalDateTimeValue(basicStats, "projectstartdate"))
                .projectEndDate(getLocalDateTimeValue(basicStats, "projectenddate"))
                .totalLogs(getIntegerValue(basicStats, "totallogs"))
                .totalWorkMinutes(getIntegerValue(basicStats, "totalworkminutes"))
                .avgWorkMinutes(getIntegerValue(basicStats, "avgworkminutes"))
                .lastLogDate(getLocalDateTimeValue(basicStats, "lastlogdate"))
                .firstLogDate(getLocalDateTimeValue(basicStats, "firstlogdate"))
                .techTagCount(getIntegerValue(basicStats, "techtagcount"))
                .dailyCounts(dailyCounts)
                .techTagCounts(techTagCounts)
                .weeklyActivities(weeklyActivities)
                .build();

        log.info("Project stats retrieved for {}: {} logs, {} minutes",
                stats.getProjectName(), stats.getTotalLogs(), stats.getTotalWorkMinutes());

        return stats;
    }

    /**
     * 기술 스택 통계 조회
     *
     * @return 기술 스택 통계 DTO
     */
    public TechStackStatsDTO getTechStackStats() {
        log.debug("Getting tech stack statistics");

        // 기본 통계 조회
        Map<String, Object> basicStats = statisticsMapper.getTechStackBasicStats();

        // 카테고리별 카운트 조회
        List<TechStackStatsDTO.CategoryCount> categoryCounts = statisticsMapper.getTechStackCategoryCounts();

        // 모든 태그 사용 통계 조회
        List<TechStackStatsDTO.TagUsage> tagUsages = statisticsMapper.getTechStackAllTagUsages();

        // 인기 태그 조회 (TOP 10)
        List<TechStackStatsDTO.TagUsage> popularTags = statisticsMapper.getTechStackPopularTags(10);

        // 최근 사용 태그 조회 (TOP 10)
        List<TechStackStatsDTO.TagUsage> recentTags = statisticsMapper.getTechStackRecentTags(10);

        // DTO 생성
        TechStackStatsDTO stats = TechStackStatsDTO.builder()
                .totalTags(getIntegerValue(basicStats, "totaltags"))
                .totalUsageCount(getIntegerValue(basicStats, "totalusagecount"))
                .categoryCounts(categoryCounts)
                .tagUsages(tagUsages)
                .popularTags(popularTags)
                .recentTags(recentTags)
                .build();

        log.info("Tech stack stats retrieved: {} tags, {} total uses",
                stats.getTotalTags(), stats.getTotalUsageCount());

        return stats;
    }

    /**
     * 현재 주의 통계 조회 (편의 메서드)
     *
     * @return 이번 주 통계
     */
    public WeeklyStatsDTO getCurrentWeekStats() {
        return getWeeklyStats(null);
    }

    /**
     * 현재 월의 통계 조회 (편의 메서드)
     *
     * @return 이번 달 통계
     */
    public MonthlyStatsDTO getCurrentMonthStats() {
        return getMonthlyStats(0, 0);
    }

    /**
     * 지난 주의 통계 조회 (편의 메서드)
     *
     * @return 지난 주 통계
     */
    public WeeklyStatsDTO getLastWeekStats() {
        LocalDate lastWeekMonday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusWeeks(1);
        return getWeeklyStats(lastWeekMonday);
    }

    /**
     * 지난 달의 통계 조회 (편의 메서드)
     *
     * @return 지난 달 통계
     */
    public MonthlyStatsDTO getLastMonthStats() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        return getMonthlyStats(lastMonth.getYear(), lastMonth.getMonthValue());
    }

    // ========== 헬퍼 메서드 ==========

    /**
     * Map에서 Integer 값 안전하게 추출
     */
    private Integer getIntegerValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) {
            return 0;
        }
        Object value = map.get(key);
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    /**
     * Map에서 String 값 안전하게 추출
     */
    private String getStringValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) {
            return null;
        }
        Object value = map.get(key);
        return (value != null) ? value.toString() : null;
    }

    /**
     * Map에서 LocalDateTime 값 안전하게 추출
     */
    private LocalDateTime getLocalDateTimeValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) {
            return null;
        }
        Object value = map.get(key);
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        return null;
    }
}
