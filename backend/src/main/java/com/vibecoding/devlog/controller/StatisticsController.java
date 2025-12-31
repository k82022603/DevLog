package com.vibecoding.devlog.controller;

import com.vibecoding.devlog.dto.response.MonthlyStatsDTO;
import com.vibecoding.devlog.dto.response.ProjectStatsDTO;
import com.vibecoding.devlog.dto.response.TechStackStatsDTO;
import com.vibecoding.devlog.dto.response.WeeklyStatsDTO;
import com.vibecoding.devlog.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 통계 REST 컨트롤러
 *
 * 개발 로그 통계 관련 API 엔드포인트를 제공합니다.
 * 주간, 월간, 프로젝트별, 기술 스택 통계 API를 제공합니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 주간 통계 조회
     *
     * @param startDate 시작 날짜 (선택, 형식: yyyy-MM-dd, 기본값: 이번 주 월요일)
     * @return 주간 통계 DTO
     */
    @GetMapping("/weekly")
    public ResponseEntity<WeeklyStatsDTO> getWeeklyStats(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        log.debug("GET /statistics/weekly - startDate: {}", startDate);

        WeeklyStatsDTO stats = statisticsService.getWeeklyStats(startDate);

        return ResponseEntity.ok(stats);
    }

    /**
     * 현재 주 통계 조회 (편의 API)
     *
     * @return 이번 주 통계 DTO
     */
    @GetMapping("/weekly/current")
    public ResponseEntity<WeeklyStatsDTO> getCurrentWeekStats() {
        log.debug("GET /statistics/weekly/current");

        WeeklyStatsDTO stats = statisticsService.getCurrentWeekStats();

        return ResponseEntity.ok(stats);
    }

    /**
     * 지난 주 통계 조회 (편의 API)
     *
     * @return 지난 주 통계 DTO
     */
    @GetMapping("/weekly/last")
    public ResponseEntity<WeeklyStatsDTO> getLastWeekStats() {
        log.debug("GET /statistics/weekly/last");

        WeeklyStatsDTO stats = statisticsService.getLastWeekStats();

        return ResponseEntity.ok(stats);
    }

    /**
     * 월간 통계 조회
     *
     * @param year 연도 (선택, 기본값: 현재 연도)
     * @param month 월 (선택, 1-12, 기본값: 현재 월)
     * @return 월간 통계 DTO
     */
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyStatsDTO> getMonthlyStats(
            @RequestParam(required = false, defaultValue = "0") int year,
            @RequestParam(required = false, defaultValue = "0") int month) {

        log.debug("GET /statistics/monthly - year: {}, month: {}", year, month);

        try {
            MonthlyStatsDTO stats = statisticsService.getMonthlyStats(year, month);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            log.error("Invalid parameters for monthly stats: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 현재 월 통계 조회 (편의 API)
     *
     * @return 이번 달 통계 DTO
     */
    @GetMapping("/monthly/current")
    public ResponseEntity<MonthlyStatsDTO> getCurrentMonthStats() {
        log.debug("GET /statistics/monthly/current");

        MonthlyStatsDTO stats = statisticsService.getCurrentMonthStats();

        return ResponseEntity.ok(stats);
    }

    /**
     * 지난 달 통계 조회 (편의 API)
     *
     * @return 지난 달 통계 DTO
     */
    @GetMapping("/monthly/last")
    public ResponseEntity<MonthlyStatsDTO> getLastMonthStats() {
        log.debug("GET /statistics/monthly/last");

        MonthlyStatsDTO stats = statisticsService.getLastMonthStats();

        return ResponseEntity.ok(stats);
    }

    /**
     * 프로젝트별 통계 조회
     *
     * @param projectId 프로젝트 ID (필수)
     * @return 프로젝트 통계 DTO
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectStatsDTO> getProjectStats(@PathVariable Long projectId) {
        log.debug("GET /statistics/project/{} - projectId: {}", projectId, projectId);

        try {
            ProjectStatsDTO stats = statisticsService.getProjectStats(projectId);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            log.error("Invalid project ID: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 기술 스택 통계 조회
     *
     * @return 기술 스택 통계 DTO
     */
    @GetMapping("/tech-stack")
    public ResponseEntity<TechStackStatsDTO> getTechStackStats() {
        log.debug("GET /statistics/tech-stack");

        TechStackStatsDTO stats = statisticsService.getTechStackStats();

        return ResponseEntity.ok(stats);
    }

    /**
     * 대시보드용 종합 통계 조회
     * 주간, 월간, 기술 스택 통계를 한 번에 제공합니다.
     *
     * @return 종합 통계 맵
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        log.debug("GET /statistics/dashboard");

        try {
            WeeklyStatsDTO weeklyStats = statisticsService.getCurrentWeekStats();
            MonthlyStatsDTO monthlyStats = statisticsService.getCurrentMonthStats();
            TechStackStatsDTO techStackStats = statisticsService.getTechStackStats();

            return ResponseEntity.ok(new Object() {
                public final WeeklyStatsDTO weekly = weeklyStats;
                public final MonthlyStatsDTO monthly = monthlyStats;
                public final TechStackStatsDTO techStack = techStackStats;
            });
        } catch (Exception e) {
            log.error("Error getting dashboard stats", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
