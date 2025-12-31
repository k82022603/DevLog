package com.vibecoding.devlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibecoding.devlog.model.Project;
import com.vibecoding.devlog.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ProjectController 통합 테스트
 *
 * Spring Test를 사용한 REST API 엔드포인트 검증.
 * MockMvc를 통해 HTTP 요청/응답을 테스트합니다.
 *
 * @author DevLog Test Team
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProjectController 통합 테스트")
public class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    private Project testProject;
    private List<Project> testProjects;

    @BeforeEach
    void setUp() {
        // Given: 테스트용 프로젝트 데이터
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("DevLog");
        testProject.setDescription("개발자 로그 시스템");
        testProject.setStatus("ACTIVE");
        testProject.setProgress(50);

        testProjects = Arrays.asList(testProject);
    }

    // ============= GET /api/projects =============

    @Test
    @DisplayName("GET /api/projects - 모든 프로젝트 조회 성공")
    void getAllProjects_Success() throws Exception {
        // Given: 프로젝트 목록이 있음
        when(projectService.findAll()).thenReturn(testProjects);

        // When & Then: GET 요청을 보내고 응답 검증
        mockMvc.perform(get("/api/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("DevLog"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(projectService, times(1)).findAll();
    }

    // ============= GET /api/projects/{id} =============

    @Test
    @DisplayName("GET /api/projects/{id} - 특정 프로젝트 조회 성공")
    void getProjectById_Success() throws Exception {
        // Given: 프로젝트가 존재
        when(projectService.findById(1L)).thenReturn(Optional.of(testProject));

        // When & Then: GET 요청을 보내고 응답 검증
        mockMvc.perform(get("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("DevLog"))
                .andExpect(jsonPath("$.description").value("개발자 로그 시스템"));

        verify(projectService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/projects/{id} - 프로젝트 조회 실패 (404)")
    void getProjectById_Fail_NotFound() throws Exception {
        // Given: 프로젝트가 존재하지 않음
        when(projectService.findById(999L)).thenReturn(Optional.empty());

        // When & Then: 404 상태 코드 반환
        mockMvc.perform(get("/api/projects/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).findById(999L);
    }

    // ============= POST /api/projects =============

    @Test
    @DisplayName("POST /api/projects - 프로젝트 생성 성공")
    void createProject_Success() throws Exception {
        // Given: 생성할 프로젝트 데이터
        Project newProject = new Project();
        newProject.setName("새 프로젝트");
        newProject.setDescription("새 프로젝트 설명");
        newProject.setStatus("ACTIVE");
        newProject.setProgress(0);

        Project createdProject = new Project();
        createdProject.setId(2L);
        createdProject.setName("새 프로젝트");
        createdProject.setDescription("새 프로젝트 설명");
        createdProject.setStatus("ACTIVE");
        createdProject.setProgress(0);

        when(projectService.create(any(Project.class))).thenReturn(createdProject);

        // When & Then: POST 요청을 보내고 응답 검증
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProject)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("새 프로젝트"));

        verify(projectService, times(1)).create(any(Project.class));
    }

    @Test
    @DisplayName("POST /api/projects - 프로젝트 생성 실패 (400 - 유효성 검증)")
    void createProject_Fail_InvalidData() throws Exception {
        // Given: 유효하지 않은 프로젝트 데이터 (이름 없음)
        String invalidProjectJson = "{\"description\": \"설명만 있음\"}";

        // When & Then: 400 상태 코드 반환
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidProjectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // ============= PUT /api/projects/{id} =============

    @Test
    @DisplayName("PUT /api/projects/{id} - 프로젝트 수정 성공")
    void updateProject_Success() throws Exception {
        // Given: 수정할 프로젝트 데이터
        Project updateData = new Project();
        updateData.setName("수정된 프로젝트");
        updateData.setDescription("수정된 설명");
        updateData.setStatus("COMPLETED");
        updateData.setProgress(100);

        Project updatedProject = new Project();
        updatedProject.setId(1L);
        updatedProject.setName("수정된 프로젝트");
        updatedProject.setDescription("수정된 설명");
        updatedProject.setStatus("COMPLETED");
        updatedProject.setProgress(100);

        when(projectService.update(1L, updateData)).thenReturn(updatedProject);

        // When & Then: PUT 요청을 보내고 응답 검증
        mockMvc.perform(put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("수정된 프로젝트"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.progress").value(100));

        verify(projectService, times(1)).update(1L, updateData);
    }

    @Test
    @DisplayName("PUT /api/projects/{id} - 프로젝트 수정 실패 (404)")
    void updateProject_Fail_NotFound() throws Exception {
        // Given: 존재하지 않는 프로젝트
        Project updateData = new Project();
        updateData.setName("수정된 이름");

        when(projectService.update(999L, updateData))
                .thenThrow(new IllegalArgumentException("프로젝트를 찾을 수 없습니다: 999"));

        // When & Then: 404 또는 400 상태 코드 반환
        mockMvc.perform(put("/api/projects/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).update(999L, updateData);
    }

    // ============= DELETE /api/projects/{id} =============

    @Test
    @DisplayName("DELETE /api/projects/{id} - 프로젝트 삭제 성공")
    void deleteProject_Success() throws Exception {
        // Given: 삭제할 프로젝트가 존재
        doNothing().when(projectService).delete(1L);

        // When & Then: DELETE 요청을 보내고 응답 검증
        mockMvc.perform(delete("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(projectService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/projects/{id} - 프로젝트 삭제 실패 (404)")
    void deleteProject_Fail_NotFound() throws Exception {
        // Given: 존재하지 않는 프로젝트
        doThrow(new IllegalArgumentException("프로젝트를 찾을 수 없습니다: 999"))
                .when(projectService).delete(999L);

        // When & Then: 404 또는 400 상태 코드 반환
        mockMvc.perform(delete("/api/projects/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).delete(999L);
    }

    // ============= 추가 테스트 =============

    @Test
    @DisplayName("GET /api/projects?status=ACTIVE - 상태별 프로젝트 조회")
    void getProjectsByStatus_Success() throws Exception {
        // Given: ACTIVE 상태의 프로젝트가 있음
        when(projectService.findByStatus("ACTIVE")).thenReturn(testProjects);

        // When & Then: 상태 파라미터와 함께 GET 요청
        mockMvc.perform(get("/api/projects")
                .param("status", "ACTIVE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(projectService, times(1)).findByStatus("ACTIVE");
    }

    @Test
    @DisplayName("PATCH /api/projects/{id}/progress - 진행률 업데이트")
    void updateProgress_Success() throws Exception {
        // Given: 진행률 업데이트
        testProject.setProgress(75);
        when(projectService.updateProgress(1L, 75)).thenReturn(testProject);

        // When & Then: PATCH 요청을 보내고 응답 검증
        mockMvc.perform(patch("/api/projects/1/progress")
                .param("progress", "75")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progress").value(75));

        verify(projectService, times(1)).updateProgress(1L, 75);
    }
}
