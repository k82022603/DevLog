package com.vibecoding.devlog.mapper;

import com.vibecoding.devlog.dto.response.MonthlyStatsDTO;
import com.vibecoding.devlog.dto.response.ProjectStatsDTO;
import com.vibecoding.devlog.dto.response.TechStackStatsDTO;
import com.vibecoding.devlog.dto.response.WeeklyStatsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 통계 MyBatis 매퍼 인터페이스
 *
 * 개발 로그 통계 관련 집계 쿼리를 처리합니다.
 * StatisticsMapper.xml과 매핑됩니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Mapper
public interface StatisticsMapper {

    // ========== 주간 통계 ==========

    /**
     * 주간 통계 기본 정보 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주간 통계 기본 정보 (총 로그 수, 총 작업 시간 등)
     */
    Map<String, Object> getWeeklyBasicStats(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 주간 일별 로그 카운트 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 일별 로그 카운트 목록
     */
    List<WeeklyStatsDTO.DailyCount> getWeeklyDailyCounts(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 주간 프로젝트별 로그 카운트 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 프로젝트별 로그 카운트 목록
     */
    List<WeeklyStatsDTO.ProjectCount> getWeeklyProjectCounts(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // ========== 월간 통계 ==========

    /**
     * 월간 통계 기본 정보 조회
     *
     * @param year 연도
     * @param month 월
     * @return 월간 통계 기본 정보
     */
    Map<String, Object> getMonthlyBasicStats(
            @Param("year") int year,
            @Param("month") int month
    );

    /**
     * 월간 일별 로그 카운트 조회
     *
     * @param year 연도
     * @param month 월
     * @return 일별 로그 카운트 목록
     */
    List<MonthlyStatsDTO.DailyCount> getMonthlyDailyCounts(
            @Param("year") int year,
            @Param("month") int month
    );

    /**
     * 월간 프로젝트별 로그 카운트 조회
     *
     * @param year 연도
     * @param month 월
     * @return 프로젝트별 로그 카운트 목록
     */
    List<MonthlyStatsDTO.ProjectCount> getMonthlyProjectCounts(
            @Param("year") int year,
            @Param("month") int month
    );

    /**
     * 월간 주별 카운트 조회
     *
     * @param year 연도
     * @param month 월
     * @return 주별 카운트 목록
     */
    List<MonthlyStatsDTO.WeeklyCount> getMonthlyWeeklyCounts(
            @Param("year") int year,
            @Param("month") int month
    );

    // ========== 프로젝트별 통계 ==========

    /**
     * 프로젝트 통계 기본 정보 조회
     *
     * @param projectId 프로젝트 ID
     * @return 프로젝트 통계 기본 정보
     */
    Map<String, Object> getProjectBasicStats(@Param("projectId") Long projectId);

    /**
     * 프로젝트 일별 로그 카운트 조회
     *
     * @param projectId 프로젝트 ID
     * @return 일별 로그 카운트 목록
     */
    List<ProjectStatsDTO.DailyCount> getProjectDailyCounts(@Param("projectId") Long projectId);

    /**
     * 프로젝트 기술 태그별 카운트 조회
     *
     * @param projectId 프로젝트 ID
     * @return 기술 태그별 카운트 목록
     */
    List<ProjectStatsDTO.TechTagCount> getProjectTechTagCounts(@Param("projectId") Long projectId);

    /**
     * 프로젝트 주간 활동 조회
     *
     * @param projectId 프로젝트 ID
     * @return 주간 활동 목록
     */
    List<ProjectStatsDTO.WeeklyActivity> getProjectWeeklyActivities(@Param("projectId") Long projectId);

    // ========== 기술 스택 통계 ==========

    /**
     * 기술 스택 기본 통계 조회
     *
     * @return 기본 통계 정보 (총 태그 수, 총 사용 횟수)
     */
    Map<String, Object> getTechStackBasicStats();

    /**
     * 카테고리별 태그 카운트 조회
     *
     * @return 카테고리별 카운트 목록
     */
    List<TechStackStatsDTO.CategoryCount> getTechStackCategoryCounts();

    /**
     * 모든 태그 사용 통계 조회
     *
     * @return 태그 사용 통계 목록
     */
    List<TechStackStatsDTO.TagUsage> getTechStackAllTagUsages();

    /**
     * 인기 태그 조회 (사용 횟수 기준 TOP 10)
     *
     * @param limit 조회할 개수
     * @return 인기 태그 목록
     */
    List<TechStackStatsDTO.TagUsage> getTechStackPopularTags(@Param("limit") int limit);

    /**
     * 최근 사용된 태그 조회 (TOP 10)
     *
     * @param limit 조회할 개수
     * @return 최근 사용 태그 목록
     */
    List<TechStackStatsDTO.TagUsage> getTechStackRecentTags(@Param("limit") int limit);

    /**
     * 태그별 프로젝트 수 조회
     *
     * @param tagId 태그 ID
     * @return 프로젝트 수
     */
    int getProjectCountByTag(@Param("tagId") Long tagId);

    // ========== 공통 헬퍼 쿼리 ==========

    /**
     * 기간별 활성 프로젝트 수 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 활성 프로젝트 수
     */
    int getActiveProjectCount(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 기간별 작업 일수 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 작업 일수
     */
    int getWorkDaysCount(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
