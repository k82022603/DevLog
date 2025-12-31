package com.vibecoding.devlog.service;

import com.vibecoding.devlog.mapper.ProjectMapper;
import com.vibecoding.devlog.model.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 프로젝트 서비스
 *
 * 프로젝트 관련 비즈니스 로직을 처리합니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectMapper projectMapper;

    /**
     * 모든 프로젝트 조회
     *
     * @return 전체 프로젝트 목록
     */
    public List<Project> findAll() {
        log.debug("전체 프로젝트 조회");
        return projectMapper.findAll();
    }

    /**
     * ID로 프로젝트 조회
     *
     * @param id 프로젝트 ID
     * @return 프로젝트 Optional
     */
    public Optional<Project> findById(Long id) {
        log.debug("프로젝트 조회 - id: {}", id);
        return Optional.ofNullable(projectMapper.findById(id));
    }

    /**
     * 키워드로 프로젝트 검색
     *
     * @param keyword 검색 키워드
     * @return 검색된 프로젝트 목록
     */
    public List<Project> search(String keyword) {
        log.debug("프로젝트 검색 - keyword: {}", keyword);
        return projectMapper.searchByName(keyword);
    }

    /**
     * 이름으로 프로젝트 검색
     *
     * @param keyword 검색 키워드
     * @return 검색된 프로젝트 목록
     */
    public List<Project> searchByName(String keyword) {
        log.debug("이름으로 프로젝트 검색 - keyword: {}", keyword);
        return projectMapper.searchByName(keyword);
    }

    /**
     * 상태별 프로젝트 조회
     *
     * @param status 프로젝트 상태
     * @return 해당 상태의 프로젝트 목록
     */
    public List<Project> findByStatus(String status) {
        log.debug("상태별 프로젝트 조회 - status: {}", status);
        return projectMapper.findByStatus(status);
    }

    /**
     * 프로젝트 생성
     *
     * @param project 생성할 프로젝트 정보
     * @return 생성된 프로젝트
     * @throws IllegalArgumentException 유효성 검증 실패 시
     */
    @Transactional
    public Project create(Project project) {
        log.debug("프로젝트 생성 - name: {}", project.getName());

        validateProject(project);

        if (project.getStatus() == null) {
            project.setStatus("ACTIVE");
        }

        if (project.getProgress() == null) {
            project.setProgress(0);
        }

        projectMapper.insert(project);
        log.info("프로젝트 생성 완료 - id: {}, name: {}", project.getId(), project.getName());

        return project;
    }

    /**
     * 프로젝트 수정
     *
     * @param id 수정할 프로젝트 ID
     * @param project 수정할 프로젝트 정보
     * @return 수정된 프로젝트
     * @throws IllegalArgumentException 프로젝트를 찾을 수 없거나 유효성 검증 실패 시
     */
    @Transactional
    public Project update(Long id, Project project) {
        log.debug("프로젝트 수정 - id: {}", id);

        Project existingProject = projectMapper.findById(id);
        if (existingProject == null) {
            throw new IllegalArgumentException("프로젝트를 찾을 수 없습니다: " + id);
        }

        validateProject(project);

        project.setId(id);
        projectMapper.update(project);
        log.info("프로젝트 수정 완료 - id: {}, name: {}", id, project.getName());

        return projectMapper.findById(id);
    }

    /**
     * 프로젝트 삭제
     *
     * @param id 삭제할 프로젝트 ID
     * @throws IllegalArgumentException 프로젝트를 찾을 수 없을 때
     */
    @Transactional
    public void delete(Long id) {
        log.debug("프로젝트 삭제 - id: {}", id);

        Project project = projectMapper.findById(id);
        if (project == null) {
            throw new IllegalArgumentException("프로젝트를 찾을 수 없습니다: " + id);
        }

        projectMapper.delete(id);
        log.info("프로젝트 삭제 완료 - id: {}, name: {}", id, project.getName());
    }

    /**
     * 프로젝트 진행률 업데이트
     *
     * @param id 프로젝트 ID
     * @param progress 진행률 (0-100)
     * @return 업데이트된 프로젝트
     * @throws IllegalArgumentException 프로젝트를 찾을 수 없거나 진행률이 유효하지 않을 때
     */
    @Transactional
    public Project updateProgress(Long id, Integer progress) {
        log.debug("프로젝트 진행률 업데이트 - id: {}, progress: {}", id, progress);

        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("진행률은 0에서 100 사이여야 합니다");
        }

        Project project = projectMapper.findById(id);
        if (project == null) {
            throw new IllegalArgumentException("프로젝트를 찾을 수 없습니다: " + id);
        }

        projectMapper.updateProgress(id, progress);
        log.info("프로젝트 진행률 업데이트 완료 - id: {}, progress: {}", id, progress);

        return projectMapper.findById(id);
    }

    /**
     * 프로젝트 상태 변경
     *
     * @param id 프로젝트 ID
     * @param status 새로운 상태
     * @return 업데이트된 프로젝트
     * @throws IllegalArgumentException 프로젝트를 찾을 수 없거나 상태가 유효하지 않을 때
     */
    @Transactional
    public Project updateStatus(Long id, String status) {
        log.debug("프로젝트 상태 변경 - id: {}, status: {}", id, status);

        validateStatus(status);

        Project project = projectMapper.findById(id);
        if (project == null) {
            throw new IllegalArgumentException("프로젝트를 찾을 수 없습니다: " + id);
        }

        projectMapper.updateStatus(id, status);
        log.info("프로젝트 상태 변경 완료 - id: {}, status: {}", id, status);

        return projectMapper.findById(id);
    }

    /**
     * 전체 프로젝트 수 조회
     *
     * @return 전체 프로젝트 수
     */
    public int count() {
        return projectMapper.count();
    }

    /**
     * 상태별 프로젝트 수 조회
     *
     * @param status 프로젝트 상태
     * @return 해당 상태의 프로젝트 수
     */
    public int countByStatus(String status) {
        return projectMapper.countByStatus(status);
    }

    /**
     * 프로젝트 유효성 검증
     *
     * @param project 검증할 프로젝트
     * @throws IllegalArgumentException 유효성 검증 실패 시
     */
    private void validateProject(Project project) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("프로젝트 이름은 필수입니다");
        }

        if (project.getName().length() > 100) {
            throw new IllegalArgumentException("프로젝트 이름은 100자를 초과할 수 없습니다");
        }

        if (project.getDescription() != null && project.getDescription().length() > 500) {
            throw new IllegalArgumentException("프로젝트 설명은 500자를 초과할 수 없습니다");
        }

        if (project.getStatus() != null) {
            validateStatus(project.getStatus());
        }

        if (project.getProgress() != null && (project.getProgress() < 0 || project.getProgress() > 100)) {
            throw new IllegalArgumentException("진행률은 0에서 100 사이여야 합니다");
        }
    }

    /**
     * 프로젝트 상태 유효성 검증
     *
     * @param status 검증할 상태
     * @throws IllegalArgumentException 상태가 유효하지 않을 때
     */
    private void validateStatus(String status) {
        if (!status.equals("ACTIVE") && !status.equals("COMPLETED") &&
            !status.equals("ON_HOLD") && !status.equals("ARCHIVED")) {
            throw new IllegalArgumentException(
                "유효하지 않은 프로젝트 상태입니다. ACTIVE, COMPLETED, ON_HOLD, ARCHIVED 중 하나여야 합니다");
        }
    }
}
