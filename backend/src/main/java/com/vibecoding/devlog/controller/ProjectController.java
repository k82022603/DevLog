package com.vibecoding.devlog.controller;

import com.vibecoding.devlog.model.Project;
import com.vibecoding.devlog.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 프로젝트 REST API 컨트롤러
 *
 * 프로젝트 관련 HTTP 요청을 처리합니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 모든 프로젝트 조회
     *
     * @param status 필터링할 상태 (선택사항)
     * @param keyword 검색 키워드 (선택사항)
     * @return 프로젝트 목록
     */
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {

        log.info("GET /projects - status: {}, keyword: {}", status, keyword);

        List<Project> projects;

        if (keyword != null && !keyword.trim().isEmpty()) {
            projects = projectService.searchByName(keyword);
        } else if (status != null && !status.trim().isEmpty()) {
            projects = projectService.findByStatus(status);
        } else {
            projects = projectService.findAll();
        }

        return ResponseEntity.ok(projects);
    }

    /**
     * 특정 프로젝트 조회
     *
     * @param id 프로젝트 ID
     * @return 프로젝트 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        log.info("GET /projects/{}", id);

        return projectService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 새 프로젝트 생성
     *
     * @param project 생성할 프로젝트 정보
     * @return 생성된 프로젝트
     */
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        log.info("POST /projects - name: {}", project.getName());

        try {
            Project created = projectService.create(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.error("Failed to create project: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 프로젝트 수정
     *
     * @param id 프로젝트 ID
     * @param project 수정할 프로젝트 정보
     * @return 수정된 프로젝트
     */
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @RequestBody Project project) {

        log.info("PUT /projects/{} - name: {}", id, project.getName());

        try {
            Project updated = projectService.update(id, project);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            log.error("Failed to update project: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 프로젝트 진행률 업데이트
     *
     * @param id 프로젝트 ID
     * @param requestBody 진행률 정보를 담은 Map (progress 키 필요)
     * @return 수정된 프로젝트
     */
    @PatchMapping("/{id}/progress")
    public ResponseEntity<Project> updateProjectProgress(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> requestBody) {

        Integer progress = requestBody.get("progress");
        log.info("PATCH /projects/{}/progress - progress: {}", id, progress);

        if (progress == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Project updated = projectService.updateProgress(id, progress);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            log.error("Failed to update project progress: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 프로젝트 상태 변경
     *
     * @param id 프로젝트 ID
     * @param requestBody 상태 정보를 담은 Map (status 키 필요)
     * @return 수정된 프로젝트
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Project> updateProjectStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {

        String status = requestBody.get("status");
        log.info("PATCH /projects/{}/status - status: {}", id, status);

        if (status == null || status.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Project updated = projectService.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            log.error("Failed to update project status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 프로젝트 삭제
     *
     * @param id 프로젝트 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.info("DELETE /projects/{}", id);

        try {
            projectService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to delete project: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 프로젝트 통계 조회
     *
     * @return 프로젝트 통계 정보
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        log.info("GET /projects/statistics");

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", projectService.count());
        statistics.put("active", projectService.countByStatus("ACTIVE"));
        statistics.put("completed", projectService.countByStatus("COMPLETED"));
        statistics.put("onHold", projectService.countByStatus("ON_HOLD"));
        statistics.put("archived", projectService.countByStatus("ARCHIVED"));

        return ResponseEntity.ok(statistics);
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
