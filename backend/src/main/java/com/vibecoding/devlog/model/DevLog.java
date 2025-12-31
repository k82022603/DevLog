package com.vibecoding.devlog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 개발 로그 도메인 모델
 *
 * 일일 개발 활동 로그를 담는 엔티티입니다.
 * dev_logs 테이블과 매핑됩니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevLog {

    /**
     * 로그 ID (Primary Key)
     */
    private Long id;

    /**
     * 프로젝트 ID (Foreign Key)
     */
    private Long projectId;

    /**
     * 로그 제목
     */
    private String title;

    /**
     * 로그 설명
     */
    private String description;

    /**
     * 작업 시작 시간
     */
    private LocalTime startTime;

    /**
     * 작업 종료 시간
     */
    private LocalTime endTime;

    /**
     * 작업 시간 (분)
     */
    private Integer workMinutes;

    /**
     * 성과 (Achievements)
     */
    private String achievements;

    /**
     * 도전 과제 (Challenges)
     */
    private String challenges;

    /**
     * 학습 내용 (Learnings)
     */
    private String learnings;

    /**
     * 코드 스니펫 (JSONB)
     */
    private String codeSnippets;

    /**
     * 로그 날짜
     */
    private LocalDate logDate;

    /**
     * 기분 상태 (GREAT, GOOD, NEUTRAL, BAD, TERRIBLE)
     */
    private String mood;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;

    /**
     * 연관된 프로젝트 (Join 시 사용)
     */
    private Project project;

    /**
     * 연관된 기술 태그 목록 (Join 시 사용)
     */
    private List<TechTag> techTags;

    /**
     * 작업 시간을 시작/종료 시간으로부터 계산
     *
     * @return 작업 시간 (분), 계산 불가능하면 0
     */
    public Integer calculateWorkMinutes() {
        if (startTime != null && endTime != null) {
            long minutes = java.time.Duration.between(startTime, endTime).toMinutes();
            return (int) Math.max(0, minutes);
        }
        return 0;
    }

    /**
     * 작업 시간이 설정되어 있는지 확인
     *
     * @return 시작/종료 시간이 모두 있으면 true
     */
    public boolean hasWorkTime() {
        return startTime != null && endTime != null;
    }

    /**
     * 코드 스니펫이 있는지 확인
     *
     * @return 코드 스니펫이 있으면 true
     */
    public boolean hasCodeSnippets() {
        return codeSnippets != null && !codeSnippets.trim().isEmpty();
    }

    /**
     * 프로젝트가 연결되어 있는지 확인
     *
     * @return 프로젝트 ID가 있으면 true
     */
    public boolean hasProject() {
        return projectId != null;
    }
}
