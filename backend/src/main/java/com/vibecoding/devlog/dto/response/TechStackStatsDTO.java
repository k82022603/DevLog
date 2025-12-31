package com.vibecoding.devlog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 기술 스택 통계 응답 DTO
 *
 * 기술 스택 사용 통계를 담는 DTO입니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechStackStatsDTO {

    /**
     * 총 기술 태그 수
     */
    private Integer totalTags;

    /**
     * 총 사용 횟수
     */
    private Integer totalUsageCount;

    /**
     * 카테고리별 태그 수
     */
    private List<CategoryCount> categoryCounts;

    /**
     * 기술 태그별 사용 통계
     */
    private List<TagUsage> tagUsages;

    /**
     * 인기 태그 (TOP 10)
     */
    private List<TagUsage> popularTags;

    /**
     * 최근 사용된 태그 (TOP 10)
     */
    private List<TagUsage> recentTags;

    /**
     * 카테고리별 카운트 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCount {
        /**
         * 카테고리명
         */
        private String category;

        /**
         * 태그 개수
         */
        private Integer count;

        /**
         * 총 사용 횟수
         */
        private Integer usageCount;

        /**
         * 사용률 (%)
         */
        private Double percentage;
    }

    /**
     * 태그 사용 통계 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagUsage {
        /**
         * 태그 ID
         */
        private Long tagId;

        /**
         * 태그 이름
         */
        private String tagName;

        /**
         * 카테고리
         */
        private String category;

        /**
         * 태그 색상
         */
        private String color;

        /**
         * 사용 횟수
         */
        private Integer usageCount;

        /**
         * 사용률 (%)
         */
        private Double percentage;

        /**
         * 최근 사용 날짜
         */
        private String lastUsedDate;

        /**
         * 이 태그를 사용한 프로젝트 수
         */
        private Integer projectCount;
    }
}
