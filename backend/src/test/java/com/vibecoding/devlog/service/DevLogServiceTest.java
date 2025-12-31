package com.vibecoding.devlog.service;

import com.vibecoding.devlog.mapper.DevLogMapper;
import com.vibecoding.devlog.mapper.ProjectMapper;
import com.vibecoding.devlog.model.DevLog;
import com.vibecoding.devlog.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * DevLogService 단위 테스트
 *
 * TDD 원칙에 따라 DevLog의 CRUD 및 비즈니스 로직을 검증합니다.
 * 의존성(Mapper)을 Mock으로 처리하여 Service 로직만 격리 테스트합니다.
 *
 * @author DevLog Test Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DevLogService 테스트")
public class DevLogServiceTest {

    @Mock
    private DevLogMapper devLogMapper;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private DevLogService devLogService;

    private DevLog testDevLog;
    private Project testProject;
    private List<DevLog> testDevLogs;

    @BeforeEach
    void setUp() {
        // 테스트용 프로젝트
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("DevLog");
        testProject.setStatus("ACTIVE");

        // 테스트용 개발 로그
        testDevLog = new DevLog();
        testDevLog.setId(1L);
        testDevLog.setProjectId(1L);
        testDevLog.setTitle("Spring Boot 초기 설정");
        testDevLog.setContent("프로젝트 초기 설정 완료");
        testDevLog.setTags("Spring,Setup");
        testDevLog.setLogDate(LocalDateTime.now());

        testDevLogs = Arrays.asList(testDevLog);
    }

    // ============= CREATE (생성) 테스트 =============

    @Test
    @DisplayName("개발 로그 생성 성공")
    void createDevLog_Success() {
        // Given: 유효한 로그 데이터
        DevLog newLog = new DevLog();
        newLog.setProjectId(1L);
        newLog.setTitle("새로운 로그");
        newLog.setContent("로그 내용");
        newLog.setTags("test");
        newLog.setLogDate(LocalDateTime.now());

        when(projectMapper.findById(1L)).thenReturn(testProject);
        doNothing().when(devLogMapper).insert(any(DevLog.class));

        // When: 로그 생성
        DevLog result = devLogService.create(newLog);

        // Then: 로그가 생성됨
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("새로운 로그");
        verify(devLogMapper, times(1)).insert(newLog);
        verify(projectMapper, times(1)).findById(1L);
    }

    @Test
    @DisplayName("개발 로그 생성 실패 - 프로젝트 없음")
    void createDevLog_Fail_ProjectNotFound() {
        // Given: 존재하지 않는 프로젝트
        DevLog newLog = new DevLog();
        newLog.setProjectId(999L);
        newLog.setTitle("새로운 로그");
        newLog.setContent("내용");

        when(projectMapper.findById(999L)).thenReturn(null);

        // When & Then: 예외 발생
        assertThatThrownBy(() -> devLogService.create(newLog))
                .isInstanceOf(IllegalArgumentException.class);
        verify(devLogMapper, never()).insert(any());
    }

    @Test
    @DisplayName("개발 로그 생성 실패 - 제목 없음")
    void createDevLog_Fail_NoTitle() {
        // Given: 제목이 없는 로그
        DevLog newLog = new DevLog();
        newLog.setProjectId(1L);
        newLog.setTitle("");
        newLog.setContent("내용");

        // When & Then: 예외 발생
        assertThatThrownBy(() -> devLogService.create(newLog))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 필수입니다");
        verify(devLogMapper, never()).insert(any());
    }

    // ============= READ (조회) 테스트 =============

    @Test
    @DisplayName("모든 개발 로그 조회 성공")
    void findAll_Success() {
        // Given: 로그 목록이 있음
        when(devLogMapper.findAll()).thenReturn(testDevLogs);

        // When: 모든 로그 조회
        List<DevLog> result = devLogService.findAll();

        // Then: 로그 목록 반환
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Spring Boot 초기 설정");
        verify(devLogMapper, times(1)).findAll();
    }

    @Test
    @DisplayName("ID로 개발 로그 조회 성공")
    void findById_Success() {
        // Given: 로그가 존재
        when(devLogMapper.findById(1L)).thenReturn(testDevLog);

        // When: ID로 조회
        Optional<DevLog> result = devLogService.findById(1L);

        // Then: 로그 반환
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Spring Boot 초기 설정");
        verify(devLogMapper, times(1)).findById(1L);
    }

    @Test
    @DisplayName("프로젝트별 개발 로그 조회 성공")
    void findByProjectId_Success() {
        // Given: 프로젝트의 로그가 있음
        when(devLogMapper.findByProjectId(1L)).thenReturn(testDevLogs);

        // When: 프로젝트별 조회
        List<DevLog> result = devLogService.findByProjectId(1L);

        // Then: 로그 목록 반환
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProjectId()).isEqualTo(1L);
        verify(devLogMapper, times(1)).findByProjectId(1L);
    }

    @Test
    @DisplayName("날짜 범위로 개발 로그 조회 성공")
    void findByDateRange_Success() {
        // Given: 날짜 범위 검색
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        when(devLogMapper.findByDateRange(startDate, endDate)).thenReturn(testDevLogs);

        // When: 날짜 범위로 조회
        List<DevLog> result = devLogService.findByDateRange(startDate, endDate);

        // Then: 로그 목록 반환
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(devLogMapper, times(1)).findByDateRange(startDate, endDate);
    }

    @Test
    @DisplayName("키워드로 개발 로그 검색 성공")
    void search_Success() {
        // Given: 검색 결과가 있음
        when(devLogMapper.searchByKeyword("Spring")).thenReturn(testDevLogs);

        // When: 키워드 검색
        List<DevLog> result = devLogService.search("Spring");

        // Then: 검색 결과 반환
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).contains("Spring");
        verify(devLogMapper, times(1)).searchByKeyword("Spring");
    }

    // ============= UPDATE (수정) 테스트 =============

    @Test
    @DisplayName("개발 로그 수정 성공")
    void updateDevLog_Success() {
        // Given: 수정할 로그 데이터
        DevLog updateData = new DevLog();
        updateData.setTitle("수정된 제목");
        updateData.setContent("수정된 내용");

        when(devLogMapper.findById(1L)).thenReturn(testDevLog);
        doNothing().when(devLogMapper).update(any(DevLog.class));

        // When: 로그 수정
        devLogService.update(1L, updateData);

        // Then: 로그가 수정됨
        verify(devLogMapper, times(1)).findById(1L);
        verify(devLogMapper, times(1)).update(any(DevLog.class));
    }

    @Test
    @DisplayName("개발 로그 수정 실패 - 존재하지 않음")
    void updateDevLog_Fail_NotFound() {
        // Given: 존재하지 않는 로그
        DevLog updateData = new DevLog();
        updateData.setTitle("수정된 제목");

        when(devLogMapper.findById(999L)).thenReturn(null);

        // When & Then: 예외 발생
        assertThatThrownBy(() -> devLogService.update(999L, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("개발 로그를 찾을 수 없습니다: 999");
        verify(devLogMapper, never()).update(any());
    }

    // ============= DELETE (삭제) 테스트 =============

    @Test
    @DisplayName("개발 로그 삭제 성공")
    void deleteDevLog_Success() {
        // Given: 삭제할 로그가 존재
        when(devLogMapper.findById(1L)).thenReturn(testDevLog);
        doNothing().when(devLogMapper).delete(1L);

        // When: 로그 삭제
        devLogService.delete(1L);

        // Then: 로그가 삭제됨
        verify(devLogMapper, times(1)).findById(1L);
        verify(devLogMapper, times(1)).delete(1L);
    }

    @Test
    @DisplayName("개발 로그 삭제 실패 - 존재하지 않음")
    void deleteDevLog_Fail_NotFound() {
        // Given: 존재하지 않는 로그
        when(devLogMapper.findById(999L)).thenReturn(null);

        // When & Then: 예외 발생
        assertThatThrownBy(() -> devLogService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("개발 로그를 찾을 수 없습니다: 999");
        verify(devLogMapper, never()).delete(any());
    }

    // ============= COUNT (개수 조회) 테스트 =============

    @Test
    @DisplayName("전체 개발 로그 수 조회 성공")
    void count_Success() {
        // Given: 로그 개수
        when(devLogMapper.count()).thenReturn(42);

        // When: 개수 조회
        int result = devLogService.count();

        // Then: 개수 반환
        assertThat(result).isEqualTo(42);
        verify(devLogMapper, times(1)).count();
    }

    @Test
    @DisplayName("프로젝트별 개발 로그 수 조회 성공")
    void countByProjectId_Success() {
        // Given: 프로젝트별 로그 개수
        when(devLogMapper.countByProjectId(1L)).thenReturn(10);

        // When: 프로젝트별 개수 조회
        int result = devLogService.countByProjectId(1L);

        // Then: 개수 반환
        assertThat(result).isEqualTo(10);
        verify(devLogMapper, times(1)).countByProjectId(1L);
    }
}
