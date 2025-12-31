package com.vibecoding.devlog.service;

import com.vibecoding.devlog.mapper.StatisticsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * StatisticsService 단위 테스트
 *
 * 통계 조회 기능을 테스트합니다.
 * 주간, 월간, 프로젝트별 통계 등을 검증합니다.
 *
 * @author DevLog Test Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StatisticsService 테스트")
public class StatisticsServiceTest {

    @Mock
    private StatisticsMapper statisticsMapper;

    @InjectMocks
    private StatisticsService statisticsService;

    private Map<String, Object> mockWeeklyStats;
    private Map<String, Object> mockMonthlyStats;
    private Map<String, Object> mockProjectStats;
    private Map<String, Object> mockTechStats;

    @BeforeEach
    void setUp() {
        // 주간 통계 데이터
        mockWeeklyStats = new HashMap<>();
        mockWeeklyStats.put("total_logs", 15);
        mockWeeklyStats.put("total_time_spent", 240);
        mockWeeklyStats.put("average_logs_per_day", 2);
        mockWeeklyStats.put("most_used_technology", "Spring Boot");
        mockWeeklyStats.put("week_start_date", LocalDate.now().minusDays(7));

        // 월간 통계 데이터
        mockMonthlyStats = new HashMap<>();
        mockMonthlyStats.put("total_logs", 60);
        mockMonthlyStats.put("total_time_spent", 1000);
        mockMonthlyStats.put("average_logs_per_day", 2);
        mockMonthlyStats.put("month", "2025-01");

        // 프로젝트 통계 데이터
        mockProjectStats = new HashMap<>();
        mockProjectStats.put("project_id", 1L);
        mockProjectStats.put("project_name", "DevLog");
        mockProjectStats.put("total_logs", 25);
        mockProjectStats.put("progress_percentage", 75);

        // 기술 스택 통계 데이터
        mockTechStats = new HashMap<>();
        mockTechStats.put("tech_name", "Spring Boot");
        mockTechStats.put("usage_count", 30);
        mockTechStats.put("percentage", 45.0);
    }

    // ============= 주간 통계 테스트 =============

    @Test
    @DisplayName("현재 주 통계 조회 성공")
    void getCurrentWeekStats_Success() {
        // Given
        when(statisticsMapper.getWeeklyStats(any(), any()))
                .thenReturn(mockWeeklyStats);

        // When
        Map<String, Object> result = statisticsService.getCurrentWeekStats();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("total_logs")).isEqualTo(15);
        assertThat(result.get("average_logs_per_day")).isEqualTo(2);
        verify(statisticsMapper, times(1)).getWeeklyStats(any(), any());
    }

    @Test
    @DisplayName("지난 주 통계 조회 성공")
    void getLastWeekStats_Success() {
        // Given
        LocalDateTime lastWeekStart = LocalDateTime.now().minusDays(14);
        LocalDateTime lastWeekEnd = LocalDateTime.now().minusDays(7);

        when(statisticsMapper.getWeeklyStats(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(mockWeeklyStats);

        // When
        Map<String, Object> result = statisticsService.getWeeklyStats(lastWeekStart, lastWeekEnd);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("total_logs")).isEqualTo(15);
        verify(statisticsMapper, times(1)).getWeeklyStats(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
    }

    @Test
    @DisplayName("커스텀 날짜 범위 주간 통계 조회 성공")
    void getWeeklyStats_CustomDateRange_Success() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 8, 23, 59, 59);

        when(statisticsMapper.getWeeklyStats(startDate, endDate))
                .thenReturn(mockWeeklyStats);

        // When
        Map<String, Object> result = statisticsService.getWeeklyStats(startDate, endDate);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("total_logs")).isEqualTo(15);
        verify(statisticsMapper, times(1)).getWeeklyStats(startDate, endDate);
    }

    @Test
    @DisplayName("주간 통계 조회 실패 - 데이터 없음")
    void getWeeklyStats_NoData() {
        // Given
        when(statisticsMapper.getWeeklyStats(any(), any()))
                .thenReturn(new HashMap<>());

        // When
        Map<String, Object> result = statisticsService.getCurrentWeekStats();

        // Then
        assertThat(result).isEmpty();
        verify(statisticsMapper, times(1)).getWeeklyStats(any(), any());
    }

    // ============= 월간 통계 테스트 =============

    @Test
    @DisplayName("현재 월 통계 조회 성공")
    void getCurrentMonthStats_Success() {
        // Given
        when(statisticsMapper.getMonthlyStats(any(), any()))
                .thenReturn(mockMonthlyStats);

        // When
        Map<String, Object> result = statisticsService.getCurrentMonthStats();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("total_logs")).isEqualTo(60);
        assertThat(result.get("total_time_spent")).isEqualTo(1000);
        verify(statisticsMapper, times(1)).getMonthlyStats(any(), any());
    }

    @Test
    @DisplayName("지난 월 통계 조회 성공")
    void getLastMonthStats_Success() {
        // Given
        LocalDateTime lastMonthStart = LocalDateTime.now().minusDays(30);
        LocalDateTime lastMonthEnd = LocalDateTime.now();

        when(statisticsMapper.getMonthlyStats(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(mockMonthlyStats);

        // When
        Map<String, Object> result = statisticsService.getMonthlyStats(lastMonthStart, lastMonthEnd);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("total_logs")).isEqualTo(60);
        verify(statisticsMapper, times(1)).getMonthlyStats(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
    }

    @Test
    @DisplayName("커스텀 날짜 범위 월간 통계 조회 성공")
    void getMonthlyStats_CustomDateRange_Success() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 31, 23, 59, 59);

        when(statisticsMapper.getMonthlyStats(startDate, endDate))
                .thenReturn(mockMonthlyStats);

        // When
        Map<String, Object> result = statisticsService.getMonthlyStats(startDate, endDate);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("total_logs")).isEqualTo(60);
        verify(statisticsMapper, times(1)).getMonthlyStats(startDate, endDate);
    }

    // ============= 프로젝트 통계 테스트 =============

    @Test
    @DisplayName("프로젝트 통계 조회 성공")
    void getProjectStats_Success() {
        // Given
        when(statisticsMapper.getProjectStats(1L)).thenReturn(mockProjectStats);

        // When
        Map<String, Object> result = statisticsService.getProjectStats(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("project_name")).isEqualTo("DevLog");
        assertThat(result.get("total_logs")).isEqualTo(25);
        assertThat(result.get("progress_percentage")).isEqualTo(75);
        verify(statisticsMapper, times(1)).getProjectStats(1L);
    }

    @Test
    @DisplayName("프로젝트 통계 조회 실패 - 프로젝트 없음")
    void getProjectStats_ProjectNotFound() {
        // Given
        when(statisticsMapper.getProjectStats(999L)).thenReturn(new HashMap<>());

        // When
        Map<String, Object> result = statisticsService.getProjectStats(999L);

        // Then
        assertThat(result).isEmpty();
        verify(statisticsMapper, times(1)).getProjectStats(999L);
    }

    // ============= 기술 스택 통계 테스트 =============

    @Test
    @DisplayName("기술 스택 통계 조회 성공")
    void getTechStackStats_Success() {
        // Given
        when(statisticsMapper.getTechStackStats()).thenReturn(mockTechStats);

        // When
        Map<String, Object> result = statisticsService.getTechStackStats();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("tech_name")).isEqualTo("Spring Boot");
        assertThat(result.get("usage_count")).isEqualTo(30);
        assertThat(result.get("percentage")).isEqualTo(45.0);
        verify(statisticsMapper, times(1)).getTechStackStats();
    }

    // ============= 헬퍼 메서드 테스트 =============

    @Test
    @DisplayName("정수값 추출 성공")
    void getIntegerValue_Success() {
        // Given
        Map<String, Object> data = new HashMap<>();
        data.put("count", 100);
        data.put("percentage", 50.5);
        data.put("invalid", "text");

        // When
        Integer countValue = statisticsService.getIntegerValue(data, "count");
        Integer percentageValue = statisticsService.getIntegerValue(data, "percentage");
        Integer defaultValue = statisticsService.getIntegerValue(data, "nonexistent");

        // Then
        assertThat(countValue).isEqualTo(100);
        assertThat(percentageValue).isEqualTo(50); // 소수점 버림
        assertThat(defaultValue).isEqualTo(0); // 기본값
    }

    @Test
    @DisplayName("문자열값 추출 성공")
    void getStringValue_Success() {
        // Given
        Map<String, Object> data = new HashMap<>();
        data.put("tech_name", "Spring Boot");
        data.put("description", null);

        // When
        String techName = statisticsService.getStringValue(data, "tech_name");
        String nullValue = statisticsService.getStringValue(data, "description");
        String defaultValue = statisticsService.getStringValue(data, "nonexistent");

        // Then
        assertThat(techName).isEqualTo("Spring Boot");
        assertThat(nullValue).isNull();
        assertThat(defaultValue).isEqualTo(""); // 기본값
    }

    @Test
    @DisplayName("LocalDateTime값 추출 성공")
    void getLocalDateTimeValue_Success() {
        // Given
        LocalDateTime testDateTime = LocalDateTime.now();
        Map<String, Object> data = new HashMap<>();
        data.put("created_at", testDateTime);
        data.put("invalid", "not a datetime");

        // When
        LocalDateTime resultDateTime = statisticsService.getLocalDateTimeValue(data, "created_at");
        LocalDateTime invalidValue = statisticsService.getLocalDateTimeValue(data, "invalid");
        LocalDateTime nonexistentValue = statisticsService.getLocalDateTimeValue(data, "nonexistent");

        // Then
        assertThat(resultDateTime).isEqualTo(testDateTime);
        assertThat(invalidValue).isNull();
        assertThat(nonexistentValue).isNull();
    }

    // ============= 데이터 검증 테스트 =============

    @Test
    @DisplayName("통계 데이터가 null이 아닌지 검증")
    void validateStatisticsData_NotNull() {
        // Given
        when(statisticsMapper.getWeeklyStats(any(), any()))
                .thenReturn(mockWeeklyStats);

        // When
        Map<String, Object> result = statisticsService.getCurrentWeekStats();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("통계 데이터 필수 항목 포함 여부 검증")
    void validateStatisticsData_ContainsRequired() {
        // Given
        when(statisticsMapper.getWeeklyStats(any(), any()))
                .thenReturn(mockWeeklyStats);

        // When
        Map<String, Object> result = statisticsService.getCurrentWeekStats();

        // Then
        assertThat(result)
                .containsKeys("total_logs", "total_time_spent", "average_logs_per_day");
    }

    @Test
    @DisplayName("통계 수치가 양수인지 검증")
    void validateStatisticsValues_Positive() {
        // Given
        when(statisticsMapper.getProjectStats(1L)).thenReturn(mockProjectStats);

        // When
        Map<String, Object> result = statisticsService.getProjectStats(1L);

        // Then
        Integer totalLogs = ((Number) result.get("total_logs")).intValue();
        assertThat(totalLogs).isGreaterThan(0);
    }
}
