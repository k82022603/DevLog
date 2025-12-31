package com.vibecoding.devlog.controller;

import com.vibecoding.devlog.model.TechTag;
import com.vibecoding.devlog.service.TechTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 기술 태그 REST API 컨트롤러
 *
 * 기술 태그 관련 HTTP 요청을 처리합니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/tech-tags")
@RequiredArgsConstructor
public class TechTagController {

    private final TechTagService techTagService;

    /**
     * 모든 기술 태그 조회
     *
     * @param category 필터링할 카테고리 (선택사항)
     * @param keyword 검색 키워드 (선택사항)
     * @return 기술 태그 목록
     */
    @GetMapping
    public ResponseEntity<List<TechTag>> getAllTags(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {

        log.info("GET /tech-tags - category: {}, keyword: {}", category, keyword);

        List<TechTag> tags;

        if (keyword != null && !keyword.trim().isEmpty()) {
            tags = techTagService.search(keyword);
        } else if (category != null && !category.trim().isEmpty()) {
            try {
                tags = techTagService.findByCategory(category);
            } catch (IllegalArgumentException e) {
                log.error("Invalid category: {}", category);
                return ResponseEntity.badRequest().build();
            }
        } else {
            tags = techTagService.findAll();
        }

        return ResponseEntity.ok(tags);
    }

    /**
     * 특정 기술 태그 조회
     *
     * @param id 태그 ID
     * @return 태그 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechTag> getTagById(@PathVariable Long id) {
        log.info("GET /tech-tags/{}", id);

        return techTagService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 인기 태그 조회
     *
     * @param limit 조회할 개수 (기본값: 10)
     * @return 인기 태그 목록
     */
    @GetMapping("/popular")
    public ResponseEntity<List<TechTag>> getPopularTags(
            @RequestParam(defaultValue = "10") int limit) {

        log.info("GET /tech-tags/popular?limit={}", limit);

        List<TechTag> tags = techTagService.findPopular(limit);
        return ResponseEntity.ok(tags);
    }

    /**
     * 새 기술 태그 생성
     *
     * @param techTag 생성할 태그 정보
     * @return 생성된 태그
     */
    @PostMapping
    public ResponseEntity<TechTag> createTag(@RequestBody TechTag techTag) {
        log.info("POST /tech-tags - name: {}", techTag.getName());

        try {
            TechTag created = techTagService.create(techTag);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.error("Failed to create tech tag: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 기술 태그 수정
     *
     * @param id 태그 ID
     * @param techTag 수정할 태그 정보
     * @return 수정된 태그
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechTag> updateTag(
            @PathVariable Long id,
            @RequestBody TechTag techTag) {

        log.info("PUT /tech-tags/{} - name: {}", id, techTag.getName());

        try {
            TechTag updated = techTagService.update(id, techTag);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            log.error("Failed to update tech tag: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 기술 태그 삭제
     *
     * @param id 태그 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        log.info("DELETE /tech-tags/{}", id);

        try {
            techTagService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to delete tech tag: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 태그 검색
     *
     * @param q 검색 키워드
     * @return 검색 결과 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<TechTag>> searchTags(@RequestParam String q) {
        log.info("GET /tech-tags/search?q={}", q);

        List<TechTag> tags = techTagService.search(q);
        return ResponseEntity.ok(tags);
    }

    /**
     * 기술 태그 통계 조회
     *
     * @return 태그 통계 정보
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        log.info("GET /tech-tags/statistics");

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", techTagService.getTotalCount());
        statistics.put("byCategory", Map.of(
            "language", techTagService.getCountByCategory("LANGUAGE"),
            "framework", techTagService.getCountByCategory("FRAMEWORK"),
            "database", techTagService.getCountByCategory("DATABASE"),
            "tool", techTagService.getCountByCategory("TOOL"),
            "library", techTagService.getCountByCategory("LIBRARY"),
            "platform", techTagService.getCountByCategory("PLATFORM")
        ));

        return ResponseEntity.ok(statistics);
    }

    /**
     * 사용 횟수 증가
     *
     * @param id 태그 ID
     * @return 200 OK
     */
    @PostMapping("/{id}/increment")
    public ResponseEntity<Void> incrementUsage(@PathVariable Long id) {
        log.info("POST /tech-tags/{}/increment", id);

        try {
            techTagService.incrementUsage(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to increment usage: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사용 횟수 감소
     *
     * @param id 태그 ID
     * @return 200 OK
     */
    @PostMapping("/{id}/decrement")
    public ResponseEntity<Void> decrementUsage(@PathVariable Long id) {
        log.info("POST /tech-tags/{}/decrement", id);

        try {
            techTagService.decrementUsage(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to decrement usage: {}", e.getMessage());
            return ResponseEntity.notFound().build();
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
