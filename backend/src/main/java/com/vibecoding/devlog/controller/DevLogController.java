package com.vibecoding.devlog.controller;

import com.vibecoding.devlog.dto.request.DevLogCreateRequest;
import com.vibecoding.devlog.dto.response.DevLogResponse;
import com.vibecoding.devlog.model.DevLog;
import com.vibecoding.devlog.service.DevLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 개발 로그 REST API 컨트롤러
 *
 * 개발 로그 관련 HTTP 요청을 처리합니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class DevLogController {

    private final DevLogService devLogService;

    /**
     * 모든 개발 로그 조회
     *
     * @param projectId 필터링할 프로젝트 ID (선택사항)
     * @param startDate 시작 날짜 (선택사항)
     * @param endDate 종료 날짜 (선택사항)
     * @param keyword 검색 키워드 (선택사항)
     * @return 개발 로그 목록
     */
    @GetMapping
    public ResponseEntity<List<DevLogResponse>> getAllLogs(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String keyword) {

        // Parse dates - accept both "yyyy-MM-dd" and "yyyy-MM-dd'T'HH:mm:ss" formats
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null && !startDate.isEmpty()) {
            startDateTime = parseDateParameter(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            endDateTime = parseDateParameter(endDate);
        }

        log.info("GET /logs - projectId: {}, startDate: {}, endDate: {}, keyword: {}",
                projectId, startDateTime, endDateTime, keyword);

        List<DevLog> logs;

        if (keyword != null && !keyword.trim().isEmpty()) {
            logs = devLogService.search(keyword);
        } else {
            logs = devLogService.findAll(projectId, startDateTime, endDateTime);
        }

        List<DevLogResponse> responses = logs.stream()
                .map(DevLogResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * 특정 개발 로그 조회
     *
     * @param id 로그 ID
     * @return 개발 로그 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<DevLogResponse> getLogById(@PathVariable Long id) {
        log.info("GET /logs/{}", id);

        return devLogService.findById(id)
                .map(DevLogResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 새 개발 로그 생성
     *
     * @param request 생성 요청 DTO
     * @return 생성된 개발 로그
     */
    @PostMapping
    public ResponseEntity<DevLogResponse> createLog(@RequestBody DevLogCreateRequest request) {
        log.info("POST /logs - title: {}", request.getTitle());

        try {
            DevLog created = devLogService.create(request);
            DevLogResponse response = DevLogResponse.from(created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Failed to create log: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 개발 로그 수정
     *
     * @param id 로그 ID
     * @param request 수정 요청 DTO
     * @return 수정된 개발 로그
     */
    @PutMapping("/{id}")
    public ResponseEntity<DevLogResponse> updateLog(
            @PathVariable Long id,
            @RequestBody DevLogCreateRequest request) {

        log.info("PUT /logs/{} - title: {}", id, request.getTitle());

        try {
            DevLog updated = devLogService.update(id, request);
            DevLogResponse response = DevLogResponse.from(updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Failed to update log: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 개발 로그 삭제
     *
     * @param id 로그 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        log.info("DELETE /logs/{}", id);

        try {
            devLogService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to delete log: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 개발 로그 검색
     *
     * @param q 검색 키워드
     * @return 검색 결과 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<DevLogResponse>> searchLogs(@RequestParam String q) {
        log.info("GET /logs/search?q={}", q);

        List<DevLog> logs = devLogService.search(q);
        List<DevLogResponse> responses = logs.stream()
                .map(DevLogResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * 캘린더 데이터 조회
     *
     * @param year 년도
     * @param month 월
     * @return 날짜별 로그 개수
     */
    @GetMapping("/calendar")
    public ResponseEntity<List<Map<String, Object>>> getCalendarData(
            @RequestParam int year,
            @RequestParam int month) {

        log.info("GET /logs/calendar?year={}&month={}", year, month);

        List<Map<String, Object>> calendarData = devLogService.findCalendarData(year, month);
        return ResponseEntity.ok(calendarData);
    }

    /**
     * 최근 개발 로그 조회
     *
     * @param limit 조회할 개수 (기본값: 10)
     * @return 최근 로그 목록
     */
    @GetMapping("/recent")
    public ResponseEntity<List<DevLogResponse>> getRecentLogs(
            @RequestParam(defaultValue = "10") int limit) {

        log.info("GET /logs/recent?limit={}", limit);

        List<DevLog> logs = devLogService.findRecent(limit);
        List<DevLogResponse> responses = logs.stream()
                .map(DevLogResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * 개발 로그 통계 조회
     *
     * @return 로그 통계 정보
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        log.info("GET /logs/statistics");

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", devLogService.getTotalCount());

        // 이번 주 로그 수
        LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1);
        LocalDateTime now = LocalDateTime.now();
        statistics.put("thisWeek", devLogService.getCountByDateRange(weekStart, now));

        // 이번 달 로그 수
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1);
        statistics.put("thisMonth", devLogService.getCountByDateRange(monthStart, now));

        return ResponseEntity.ok(statistics);
    }

    /**
     * 날짜 파라미터 파싱 헬퍼 메서드
     * "yyyy-MM-dd" 또는 "yyyy-MM-dd'T'HH:mm:ss" 형식을 모두 지원
     *
     * @param dateString 날짜 문자열
     * @return LocalDateTime 객체
     */
    private LocalDateTime parseDateParameter(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            // ISO datetime 형식인 경우
            if (dateString.contains("T")) {
                return LocalDateTime.parse(dateString);
            }
            // 날짜만 있는 경우 - 시작 시간(00:00:00)으로 변환
            else {
                LocalDate date = LocalDate.parse(dateString);
                return date.atStartOfDay();
            }
        } catch (Exception e) {
            log.error("Failed to parse date parameter: {}", dateString, e);
            return null;
        }
    }

    /**
     * 전역 예외 처리
     *
     * @param e 발생한 예외
     * @return 에러 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        log.error("Unexpected error occurred", e);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        errorResponse.put("status", "500");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
