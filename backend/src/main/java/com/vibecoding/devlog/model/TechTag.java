package com.vibecoding.devlog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 기술 태그 도메인 모델
 *
 * 개발 로그에 사용되는 기술 스택 태그입니다.
 * tech_tags 테이블과 매핑됩니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechTag {

    /**
     * 태그 ID (Primary Key)
     */
    private Long id;

    /**
     * 태그 이름
     */
    private String name;

    /**
     * 태그 카테고리
     * LANGUAGE, FRAMEWORK, DATABASE, TOOL, LIBRARY, PLATFORM
     */
    private String category;

    /**
     * 태그 색상 (Hex Code)
     */
    private String color;

    /**
     * 사용 횟수
     */
    private Integer usageCount;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;

    /**
     * 태그가 언어 카테고리인지 확인
     *
     * @return 언어 카테고리면 true
     */
    public boolean isLanguage() {
        return "LANGUAGE".equals(this.category);
    }

    /**
     * 태그가 프레임워크 카테고리인지 확인
     *
     * @return 프레임워크 카테고리면 true
     */
    public boolean isFramework() {
        return "FRAMEWORK".equals(this.category);
    }

    /**
     * 사용 횟수 증가
     */
    public void incrementUsage() {
        if (this.usageCount == null) {
            this.usageCount = 1;
        } else {
            this.usageCount++;
        }
    }

    /**
     * 사용 횟수 감소
     */
    public void decrementUsage() {
        if (this.usageCount != null && this.usageCount > 0) {
            this.usageCount--;
        }
    }
}
