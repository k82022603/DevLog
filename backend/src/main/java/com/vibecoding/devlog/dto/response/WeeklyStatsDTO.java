package com.vibecoding.devlog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 주간 통계 응답 DTO
 *
 * 주간 개발 활동 통계를 담는 DTO입니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyStatsDTO {

    /**
     * 통계 시작 날짜
     */
    private LocalDate startDate;

    /**
     * 통계 종료 날짜
     */
    private LocalDate endDate;

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
}
