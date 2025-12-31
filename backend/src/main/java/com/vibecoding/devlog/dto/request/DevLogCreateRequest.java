package com.vibecoding.devlog.dto.request;

import com.vibecoding.devlog.model.DevLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 개발 로그 생성 요청 DTO
 *
 * 클라이언트로부터 받는 개발 로그 생성 데이터입니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevLogCreateRequest {

    /**
     * 프로젝트 ID
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
     * 코드 스니펫 (JSON 문자열)
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
     * 기술 태그 ID 목록
     */
    private List<Long> techTagIds;

    /**
     * DTO를 DevLog 엔티티로 변환
     *
     * @return DevLog 엔티티
     */
    public DevLog toEntity() {
        DevLog devLog = DevLog.builder()
                .projectId(this.projectId)
                .title(this.title)
                .description(this.description)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .achievements(this.achievements)
                .challenges(this.challenges)
                .learnings(this.learnings)
                .codeSnippets(this.codeSnippets)
                .logDate(this.logDate)
                .mood(this.mood)
                .build();

        // 작업 시간 자동 계산
        if (devLog.hasWorkTime()) {
            devLog.setWorkMinutes(devLog.calculateWorkMinutes());
        }

        return devLog;
    }
}
