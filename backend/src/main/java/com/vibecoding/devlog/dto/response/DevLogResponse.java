package com.vibecoding.devlog.dto.response;

import com.vibecoding.devlog.model.DevLog;
import com.vibecoding.devlog.model.TechTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 개발 로그 응답 DTO
 *
 * 클라이언트에게 반환하는 개발 로그 데이터입니다.
 * 프로젝트명, 기술 태그 등 추가 정보를 포함합니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevLogResponse {

    /**
     * 로그 ID
     */
    private Long id;

    /**
     * 프로젝트 ID
     */
    private Long projectId;

    /**
     * 프로젝트 이름
     */
    private String projectName;

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
     * 성과
     */
    private String achievements;

    /**
     * 도전 과제
     */
    private String challenges;

    /**
     * 학습 내용
     */
    private String learnings;

    /**
     * 코드 스니펫
     */
    private String codeSnippets;

    /**
     * 로그 날짜
     */
    private LocalDate logDate;

    /**
     * 기분 상태
     */
    private String mood;

    /**
     * 기술 태그 목록
     */
    private List<TechTagDto> techTags;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;

    /**
     * 기술 태그 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechTagDto {
        private Long id;
        private String name;
        private String category;
        private String color;
    }

    /**
     * DevLog 엔티티를 응답 DTO로 변환
     *
     * @param devLog DevLog 엔티티
     * @return DevLogResponse DTO
     */
    public static DevLogResponse from(DevLog devLog) {
        DevLogResponseBuilder builder = DevLogResponse.builder()
                .id(devLog.getId())
                .projectId(devLog.getProjectId())
                .title(devLog.getTitle())
                .description(devLog.getDescription())
                .startTime(devLog.getStartTime())
                .endTime(devLog.getEndTime())
                .workMinutes(devLog.getWorkMinutes())
                .achievements(devLog.getAchievements())
                .challenges(devLog.getChallenges())
                .learnings(devLog.getLearnings())
                .codeSnippets(devLog.getCodeSnippets())
                .logDate(devLog.getLogDate())
                .mood(devLog.getMood())
                .createdAt(devLog.getCreatedAt())
                .updatedAt(devLog.getUpdatedAt());

        // 프로젝트 정보 추가
        if (devLog.getProject() != null) {
            builder.projectName(devLog.getProject().getName());
        }

        // 기술 태그 목록 변환
        if (devLog.getTechTags() != null && !devLog.getTechTags().isEmpty()) {
            List<TechTagDto> tagDtos = devLog.getTechTags().stream()
                    .map(tag -> TechTagDto.builder()
                            .id(tag.getId())
                            .name(tag.getName())
                            .category(tag.getCategory())
                            .color(tag.getColor())
                            .build())
                    .collect(Collectors.toList());
            builder.techTags(tagDtos);
        }

        return builder.build();
    }
}
