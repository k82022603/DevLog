package com.vibecoding.devlog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 월간 통계 응답 DTO
 *
 * 월간 개발 활동 통계를 담는 DTO입니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatsDTO {

    /**
     * 통계 연도
     */
    private Integer year;

    /**
     * 통계 월
     */
    private Integer month;

    /**
     * 총 로그 개수
     */
    private Integer totalLogs;

    /**
     * 총 작업 시간 (분)
     */
    private Integer totalWorkMinutes;

    /**
     * 평균 작업 시간 (분)
     */
    private Integer avgWorkMinutes;

    /**
     * 활성 프로젝트 수
     */
    private Integer activeProjects;

    /**
     * 작업 일수 (로그가 있는 날)
     */
    private Integer workDays;

    /**
     * 일별 로그 개수
     * key: 날짜 (YYYY-MM-DD)
     * value: 로그 개수
     */
    private List<DailyCount> dailyCounts;

    /**
     * 프로젝트별 로그 개수
     * key: 프로젝트 이름
     * value: 로그 개수
     */
    private List<ProjectCount> projectCounts;

    /**
     * 주별 통계
     */
    private List<WeeklyCount> weeklyCounts;

    /**
     * 일별 카운트 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyCount {
        private String date;
        private Integer count;
        private Integer workMinutes;
    }

    /**
     * 프로젝트별 카운트 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectCount {
        private Long projectId;
        private String projectName;
        private Integer count;
        private Integer workMinutes;
    }

    /**
     * 주별 카운트 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyCount {
        private Integer weekNumber;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer count;
        private Integer workMinutes;
    }
}
