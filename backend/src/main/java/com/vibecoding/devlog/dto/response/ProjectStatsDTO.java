package com.vibecoding.devlog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 프로젝트별 통계 응답 DTO
 *
 * 특정 프로젝트의 개발 활동 통계를 담는 DTO입니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatsDTO {

    /**
     * 프로젝트 ID
     */
    private Long projectId;

    /**
     * 프로젝트 이름
     */
    private String projectName;

    /**
     * 프로젝트 설명
     */
    private String projectDescription;

    /**
     * 프로젝트 상태
     */
    private String projectStatus;

    /**
     * 프로젝트 진행률
     */
    private Integer projectProgress;

    /**
     * 프로젝트 시작일
     */
    private LocalDateTime projectStartDate;

    /**
     * 프로젝트 종료일
     */
    private LocalDateTime projectEndDate;

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
     * 최근 로그 날짜
     */
    private LocalDateTime lastLogDate;

    /**
     * 첫 로그 날짜
     */
    private LocalDateTime firstLogDate;

    /**
     * 사용된 기술 태그 개수
     */
    private Integer techTagCount;

    /**
     * 일별 로그 개수
     */
    private List<DailyCount> dailyCounts;

    /**
     * 기술 태그별 사용 빈도
     */
    private List<TechTagCount> techTagCounts;

    /**
     * 주간별 활동
     */
    private List<WeeklyActivity> weeklyActivities;

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
     * 기술 태그별 카운트 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechTagCount {
        private Long tagId;
        private String tagName;
        private String category;
        private String color;
        private Integer count;
    }

    /**
     * 주간 활동 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyActivity {
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer logCount;
        private Integer workMinutes;
    }
}
