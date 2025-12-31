package com.vibecoding.devlog.service;

import com.vibecoding.devlog.dto.request.DevLogCreateRequest;
import com.vibecoding.devlog.mapper.DevLogMapper;
import com.vibecoding.devlog.model.DevLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 개발 로그 서비스
 *
 * 개발 로그 관련 비즈니스 로직을 처리합니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DevLogService {

    private final DevLogMapper devLogMapper;

    /**
     * 모든 개발 로그 조회
     *
     * @param projectId 필터링할 프로젝트 ID (선택사항)
     * @param startDate 시작 날짜 (선택사항)
     * @param endDate 종료 날짜 (선택사항)
     * @return 개발 로그 목록
     */
    public List<DevLog> findAll(Long projectId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding all dev logs - projectId: {}, startDate: {}, endDate: {}",
                projectId, startDate, endDate);
        return devLogMapper.findAll(projectId, startDate, endDate);
    }

    /**
     * ID로 개발 로그 조회
     *
     * @param id 로그 ID
     * @return Optional로 감싼 개발 로그
     */
    public Optional<DevLog> findById(Long id) {
        log.debug("Finding dev log by id: {}", id);
        return Optional.ofNullable(devLogMapper.findById(id));
    }

    /**
     * 개발 로그 검색
     *
     * @param keyword 검색 키워드
     * @return 검색 결과 목록
     */
    public List<DevLog> search(String keyword) {
        log.debug("Searching dev logs by keyword: {}", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return devLogMapper.findAll(null, null, null);
        }
        return devLogMapper.search(keyword.trim());
    }

    /**
     * 날짜 범위로 개발 로그 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 개발 로그 목록
     */
    public List<DevLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding dev logs by date range: {} - {}", startDate, endDate);
        return devLogMapper.findByDateRange(startDate, endDate);
    }

    /**
     * 프로젝트별 개발 로그 조회
     *
     * @param projectId 프로젝트 ID
     * @return 개발 로그 목록
     */
    public List<DevLog> findByProjectId(Long projectId) {
        log.debug("Finding dev logs by projectId: {}", projectId);
        return devLogMapper.findByProjectId(projectId);
    }

    /**
     * 캘린더 데이터 조회
     *
     * @param year 년도
     * @param month 월
     * @return 날짜별 로그 개수
     */
    public List<Map<String, Object>> findCalendarData(int year, int month) {
        log.debug("Finding calendar data for {}-{}", year, month);
        return devLogMapper.findCalendarData(year, month);
    }

    /**
     * 최근 개발 로그 조회
     *
     * @param limit 조회할 개수
     * @return 최근 로그 목록
     */
    public List<DevLog> findRecent(int limit) {
        log.debug("Finding recent dev logs, limit: {}", limit);
        return devLogMapper.findRecent(limit);
    }

    /**
     * 개발 로그 생성
     *
     * @param request 생성 요청 DTO
     * @return 생성된 개발 로그
     */
    @Transactional
    public DevLog create(DevLogCreateRequest request) {
        log.debug("Creating new dev log: {}", request.getTitle());

        validateDevLog(request);

        // DTO를 엔티티로 변환 (작업 시간 자동 계산 포함)
        DevLog devLog = request.toEntity();

        // 로그 날짜가 없으면 현재 날짜로 설정
        if (devLog.getLogDate() == null) {
            devLog.setLogDate(LocalDate.now());
        }

        int result = devLogMapper.insert(devLog);
        if (result == 0) {
            throw new RuntimeException("Failed to create dev log");
        }

        // 기술 태그 연결
        if (request.getTechTagIds() != null && !request.getTechTagIds().isEmpty()) {
            for (Long tagId : request.getTechTagIds()) {
                devLogMapper.insertLogTechTag(devLog.getId(), tagId);
            }
        }

        log.info("Dev log created successfully with id: {}", devLog.getId());
        return devLogMapper.findById(devLog.getId());
    }

    /**
     * 개발 로그 수정
     *
     * @param id 로그 ID
     * @param request 수정 요청 DTO
     * @return 수정된 개발 로그
     */
    @Transactional
    public DevLog update(Long id, DevLogCreateRequest request) {
        log.debug("Updating dev log with id: {}", id);

        DevLog existing = devLogMapper.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Dev log not found with id: " + id);
        }

        validateDevLog(request);

        DevLog devLog = request.toEntity();
        devLog.setId(id);

        int result = devLogMapper.update(devLog);
        if (result == 0) {
            throw new RuntimeException("Failed to update dev log");
        }

        // 기존 태그 연결 삭제 후 새로 연결
        devLogMapper.deleteLogTechTags(id);
        if (request.getTechTagIds() != null && !request.getTechTagIds().isEmpty()) {
            for (Long tagId : request.getTechTagIds()) {
                devLogMapper.insertLogTechTag(id, tagId);
            }
        }

        log.info("Dev log updated successfully with id: {}", id);
        return devLogMapper.findById(id);
    }

    /**
     * 개발 로그 삭제
     *
     * @param id 로그 ID
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting dev log with id: {}", id);

        DevLog existing = devLogMapper.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Dev log not found with id: " + id);
        }

        // 태그 연결 먼저 삭제
        devLogMapper.deleteLogTechTags(id);

        int result = devLogMapper.delete(id);
        if (result == 0) {
            throw new RuntimeException("Failed to delete dev log");
        }

        log.info("Dev log deleted successfully with id: {}", id);
    }

    /**
     * 전체 개발 로그 수 조회
     *
     * @return 전체 로그 수
     */
    public int getTotalCount() {
        return devLogMapper.count();
    }

    /**
     * 프로젝트별 개발 로그 수 조회
     *
     * @param projectId 프로젝트 ID
     * @return 해당 프로젝트의 로그 수
     */
    public int getCountByProjectId(Long projectId) {
        return devLogMapper.countByProjectId(projectId);
    }

    /**
     * 날짜 범위별 개발 로그 수 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 로그 수
     */
    public int getCountByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return devLogMapper.countByDateRange(startDate, endDate);
    }

    /**
     * 개발 로그 유효성 검증
     *
     * @param request 검증할 요청 DTO
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateDevLog(DevLogCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Dev log request cannot be null");
        }
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Dev log title is required");
        }
        if (request.getStartTime() != null && request.getEndTime() != null) {
            if (request.getEndTime().isBefore(request.getStartTime())) {
                throw new IllegalArgumentException("End time must be after start time");
            }
        }
    }
}
