package com.vibecoding.devlog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 프로젝트 도메인 모델
 * 
 * 개발 프로젝트의 기본 정보를 담는 엔티티입니다.
 * projects 테이블과 매핑됩니다.
 * 
 * @author DevLog Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    
    /**
     * 프로젝트 ID (Primary Key)
     */
    private Long id;
    
    /**
     * 프로젝트 이름
     */
    private String name;
    
    /**
     * 프로젝트 설명
     */
    private String description;
    
    /**
     * 프로젝트 상태
     * ACTIVE, COMPLETED, ON_HOLD, ARCHIVED
     */
    private String status;
    
    /**
     * 프로젝트 시작일
     */
    private LocalDate startDate;

    /**
     * 프로젝트 종료일 (선택사항)
     */
    private LocalDate endDate;
    
    /**
     * 진행률 (0-100)
     */
    private Integer progress;
    
    /**
     * 생성일시
     */
    private LocalDateTime createdAt;
    
    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;
    
    /**
     * 프로젝트가 활성 상태인지 확인
     * 
     * @return 활성 상태이면 true, 아니면 false
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }
    
    /**
     * 프로젝트가 완료되었는지 확인
     * 
     * @return 완료 상태이면 true, 아니면 false
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(this.status);
    }
    
    /**
     * 프로젝트가 보류 상태인지 확인
     * 
     * @return 보류 상태이면 true, 아니면 false
     */
    public boolean isOnHold() {
        return "ON_HOLD".equals(this.status);
    }
    
    /**
     * 프로젝트 진행률을 설정 (0-100 범위 검증)
     * 
     * @param progress 진행률 (0-100)
     */
    public void setProgress(Integer progress) {
        if (progress != null) {
            if (progress < 0) {
                this.progress = 0;
            } else if (progress > 100) {
                this.progress = 100;
            } else {
                this.progress = progress;
            }
        } else {
            this.progress = 0;
        }
    }
}
