package com.vibecoding.devlog.service;

import com.vibecoding.devlog.mapper.ProjectMapper;
import com.vibecoding.devlog.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * ProjectService 단위 테스트
 *
 * TDD 원칙에 따라 작성되었으며, ProjectService의 모든 비즈니스 로직을 검증합니다.
 * Mockito를 사용하여 ProjectMapper의 의존성을 격리합니다.
 *
 * @author DevLog Test Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectService 테스트")
public class ProjectServiceTest {

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private Project testProject;
    private List<Project> testProjects;

    /**
     * 테스트 전에 실행되는 설정 메서드
     * 테스트용 프로젝트 데이터를 초기화합니다.
     */
    @BeforeEach
    void setUp() {
        // Given: 테스트용 프로젝트 생성
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("DevLog");
        testProject.setDescription("개발자 로그 시스템");
        testProject.setStatus("ACTIVE");
        testProject.setProgress(50);

        testProjects = Arrays.asList(testProject);
    }

    // ============= CREATE (생성) 테스트 =============

    @Test
    @DisplayName("프로젝트 생성 성공 - 유효한 데이터")
    void createProject_Success() {
        // Given: 유효한 프로젝트 데이터
        Project newProject = new Project();
        newProject.setName("새 프로젝트");
        newProject.setDescription("새 프로젝트 설명");
        doNothing().when(projectMapper).insert(any(Project.class));

        // When: 프로젝트 생성
        Project result = projectService.create(newProject);

        // Then: 프로젝트가 생성되고 기본값이 설정됨
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("새 프로젝트");
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
        assertThat(result.getProgress()).isEqualTo(0);
        verify(projectMapper, times(1)).insert(newProject);
    }

    @Test
    @DisplayName("프로젝트 생성 실패 - 이름이 null")
    void createProject_Fail_NullName() {
        // Given: 이름이 null인 프로젝트
        Project invalidProject = new Project();
        invalidProject.setName(null);

        // When & Then: IllegalArgumentException 발생
        assertThatThrownBy(() -> projectService.create(invalidProject))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로젝트 이름은 필수입니다");
        verify(projectMapper, never()).insert(any());
    }

    @Test
    @DisplayName("프로젝트 생성 실패 - 이름이 빈 문자열")
    void createProject_Fail_EmptyName() {
        // Given: 빈 문자열 이름의 프로젝트
        Project invalidProject = new Project();
        invalidProject.setName("   ");

        // When & Then: IllegalArgumentException 발생
        assertThatThrownBy(() -> projectService.create(invalidProject))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로젝트 이름은 필수입니다");
        verify(projectMapper, never()).insert(any());
    }

    @Test
    @DisplayName("프로젝트 생성 실패 - 이름이 100자 초과")
    void createProject_Fail_NameTooLong() {
        // Given: 너무 긴 이름의 프로젝트
        Project invalidProject = new Project();
        invalidProject.setName("a".repeat(101));

        // When & Then: IllegalArgumentException 발생
        assertThatThrownBy(() -> projectService.create(invalidProject))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로젝트 이름은 100자를 초과할 수 없습니다");
        verify(projectMapper, never()).insert(any());
    }

    // ============= READ (조회) 테스트 =============

    @Test
    @DisplayName("모든 프로젝트 조회 성공")
    void findAll_Success() {
        // Given: 프로젝트 목록이 있음
        when(projectMapper.findAll()).thenReturn(testProjects);

        // When: 모든 프로젝트 조회
        List<Project> result = projectService.findAll();

        // Then: 프로젝트 목록 반환
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("DevLog");
        verify(projectMapper, times(1)).findAll();
    }

    @Test
    @DisplayName("ID로 프로젝트 조회 성공")
    void findById_Success() {
        // Given: 프로젝트가 존재
        when(projectMapper.findById(1L)).thenReturn(testProject);

        // When: ID로 프로젝트 조회
        Optional<Project> result = projectService.findById(1L);

        // Then: 프로젝트 반환
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("DevLog");
        verify(projectMapper, times(1)).findById(1L);
    }

    @Test
    @DisplayName("ID로 프로젝트 조회 실패 - 존재하지 않음")
    void findById_Fail_NotFound() {
        // Given: 프로젝트가 존재하지 않음
        when(projectMapper.findById(999L)).thenReturn(null);

        // When: ID로 프로젝트 조회
        Optional<Project> result = projectService.findById(999L);

        // Then: 빈 Optional 반환
        assertThat(result).isEmpty();
        verify(projectMapper, times(1)).findById(999L);
    }

    @Test
    @DisplayName("키워드로 프로젝트 검색 성공")
    void searchByName_Success() {
        // Given: 검색 결과가 있음
        when(projectMapper.searchByName("Dev")).thenReturn(testProjects);

        // When: 키워드로 검색
        List<Project> result = projectService.searchByName("Dev");

        // Then: 검색 결과 반환
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(projectMapper, times(1)).searchByName("Dev");
    }

    @Test
    @DisplayName("상태별 프로젝트 조회 성공")
    void findByStatus_Success() {
        // Given: ACTIVE 상태의 프로젝트가 있음
        when(projectMapper.findByStatus("ACTIVE")).thenReturn(testProjects);

        // When: 상태별로 조회
        List<Project> result = projectService.findByStatus("ACTIVE");

        // Then: 해당 상태의 프로젝트 반환
        assertThat(result).isNotNull();
        assertThat(result.get(0).getStatus()).isEqualTo("ACTIVE");
        verify(projectMapper, times(1)).findByStatus("ACTIVE");
    }

    // ============= UPDATE (수정) 테스트 =============

    @Test
    @DisplayName("프로젝트 수정 성공")
    void updateProject_Success() {
        // Given: 수정할 프로젝트 정보
        Project updateData = new Project();
        updateData.setName("수정된 프로젝트");
        updateData.setDescription("수정된 설명");
        updateData.setStatus("ACTIVE");

        when(projectMapper.findById(1L)).thenReturn(testProject);
        doNothing().when(projectMapper).update(any(Project.class));
        when(projectMapper.findById(1L)).thenReturn(testProject);

        // When: 프로젝트 수정
        Project result = projectService.update(1L, updateData);

        // Then: 프로젝트가 수정됨
        assertThat(result).isNotNull();
        verify(projectMapper, times(2)).findById(1L);
        verify(projectMapper, times(1)).update(any(Project.class));
    }

    @Test
    @DisplayName("프로젝트 수정 실패 - 존재하지 않는 프로젝트")
    void updateProject_Fail_NotFound() {
        // Given: 존재하지 않는 프로젝트
        Project updateData = new Project();
        updateData.setName("수정된 이름");

        when(projectMapper.findById(999L)).thenReturn(null);

        // When & Then: IllegalArgumentException 발생
        assertThatThrownBy(() -> projectService.update(999L, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로젝트를 찾을 수 없습니다: 999");
        verify(projectMapper, never()).update(any());
    }

    @Test
    @DisplayName("프로젝트 진행률 업데이트 성공")
    void updateProgress_Success() {
        // Given: 유효한 진행률
        when(projectMapper.findById(1L)).thenReturn(testProject);
        doNothing().when(projectMapper).updateProgress(1L, 75);
        testProject.setProgress(75);
        when(projectMapper.findById(1L)).thenReturn(testProject);

        // When: 진행률 업데이트
        Project result = projectService.updateProgress(1L, 75);

        // Then: 진행률이 업데이트됨
        assertThat(result).isNotNull();
        verify(projectMapper, times(2)).findById(1L);
        verify(projectMapper, times(1)).updateProgress(1L, 75);
    }

    @Test
    @DisplayName("프로젝트 진행률 업데이트 실패 - 범위 벗어남 (음수)")
    void updateProgress_Fail_NegativeProgress() {
        // Given: 음수 진행률
        // When & Then: IllegalArgumentException 발생
        assertThatThrownBy(() -> projectService.updateProgress(1L, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("진행률은 0에서 100 사이여야 합니다");
        verify(projectMapper, never()).updateProgress(anyLong(), anyInt());
    }

    @Test
    @DisplayName("프로젝트 진행률 업데이트 실패 - 범위 벗어남 (100 초과)")
    void updateProgress_Fail_ExcessiveProgress() {
        // Given: 100을 초과하는 진행률
        // When & Then: IllegalArgumentException 발생
        assertThatThrownBy(() -> projectService.updateProgress(1L, 101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("진행률은 0에서 100 사이여야 합니다");
        verify(projectMapper, never()).updateProgress(anyLong(), anyInt());
    }

    @Test
    @DisplayName("프로젝트 상태 변경 성공")
    void updateStatus_Success() {
        // Given: 유효한 상태
        when(projectMapper.findById(1L)).thenReturn(testProject);
        doNothing().when(projectMapper).updateStatus(1L, "COMPLETED");
        testProject.setStatus("COMPLETED");
        when(projectMapper.findById(1L)).thenReturn(testProject);

        // When: 상태 변경
        Project result = projectService.updateStatus(1L, "COMPLETED");

        // Then: 상태가 변경됨
        assertThat(result).isNotNull();
        verify(projectMapper, times(2)).findById(1L);
        verify(projectMapper, times(1)).updateStatus(1L, "COMPLETED");
    }

    @Test
    @DisplayName("프로젝트 상태 변경 실패 - 유효하지 않은 상태")
    void updateStatus_Fail_InvalidStatus() {
        // Given: 유효하지 않은 상태
        // When & Then: IllegalArgumentException 발생
        assertThatThrownBy(() -> projectService.updateStatus(1L, "INVALID_STATUS"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 프로젝트 상태입니다. ACTIVE, COMPLETED, ON_HOLD, ARCHIVED 중 하나여야 합니다");
        verify(projectMapper, never()).updateStatus(anyLong(), anyString());
    }

    // ============= DELETE (삭제) 테스트 =============

    @Test
    @DisplayName("프로젝트 삭제 성공")
    void deleteProject_Success() {
        // Given: 삭제할 프로젝트가 존재
        when(projectMapper.findById(1L)).thenReturn(testProject);
        doNothing().when(projectMapper).delete(1L);

        // When: 프로젝트 삭제
        projectService.delete(1L);

        // Then: 프로젝트가 삭제됨
        verify(projectMapper, times(1)).findById(1L);
        verify(projectMapper, times(1)).delete(1L);
    }

    @Test
    @DisplayName("프로젝트 삭제 실패 - 존재하지 않는 프로젝트")
    void deleteProject_Fail_NotFound() {
        // Given: 삭제할 프로젝트가 존재하지 않음
        when(projectMapper.findById(999L)).thenReturn(null);

        // When & Then: IllegalArgumentException 발생
        assertThatThrownBy(() -> projectService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로젝트를 찾을 수 없습니다: 999");
        verify(projectMapper, never()).delete(anyLong());
    }

    // ============= COUNT (개수 조회) 테스트 =============

    @Test
    @DisplayName("전체 프로젝트 수 조회 성공")
    void count_Success() {
        // Given: 프로젝트 개수가 있음
        when(projectMapper.count()).thenReturn(5);

        // When: 프로젝트 개수 조회
        int result = projectService.count();

        // Then: 프로젝트 개수 반환
        assertThat(result).isEqualTo(5);
        verify(projectMapper, times(1)).count();
    }

    @Test
    @DisplayName("상태별 프로젝트 수 조회 성공")
    void countByStatus_Success() {
        // Given: ACTIVE 상태의 프로젝트 개수가 있음
        when(projectMapper.countByStatus("ACTIVE")).thenReturn(3);

        // When: 상태별 프로젝트 개수 조회
        int result = projectService.countByStatus("ACTIVE");

        // Then: 상태별 프로젝트 개수 반환
        assertThat(result).isEqualTo(3);
        verify(projectMapper, times(1)).countByStatus("ACTIVE");
    }
}
