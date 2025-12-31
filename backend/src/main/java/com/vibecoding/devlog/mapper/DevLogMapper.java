package com.vibecoding.devlog.mapper;

import com.vibecoding.devlog.model.DevLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 개발 로그 MyBatis 매퍼 인터페이스
 *
 * 개발 로그 관련 데이터베이스 작업을 처리합니다.
 * DevLogMapper.xml과 매핑됩니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Mapper
public interface DevLogMapper {

    /**
     * 모든 개발 로그 조회 (프로젝트, 태그 정보 포함)
     *
     * @param projectId 필터링할 프로젝트 ID (선택사항)
     * @param startDate 시작 날짜 (선택사항)
     * @param endDate 종료 날짜 (선택사항)
     * @return 개발 로그 목록
     */
    List<DevLog> findAll(@Param("projectId") Long projectId,
                         @Param("startDate") LocalDateTime startDate,
                         @Param("endDate") LocalDateTime endDate);

    /**
     * ID로 개발 로그 조회 (프로젝트, 태그 정보 포함)
     *
     * @param id 로그 ID
     * @return 개발 로그 정보, 없으면 null
     */
    DevLog findById(@Param("id") Long id);

    /**
     * 개발 로그 검색
     * 제목, 설명, 성과, 도전과제, 학습내용에서 검색
     *
     * @param keyword 검색 키워드
     * @return 검색 결과 목록
     */
    List<DevLog> search(@Param("keyword") String keyword);

    /**
     * 날짜 범위로 개발 로그 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 개발 로그 목록
     */
    List<DevLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);

    /**
     * 프로젝트별 개발 로그 조회
     *
     * @param projectId 프로젝트 ID
     * @return 개발 로그 목록
     */
    List<DevLog> findByProjectId(@Param("projectId") Long projectId);

    /**
     * 캘린더 데이터 조회 (년월별 로그 개수)
     *
     * @param year 년도
     * @param month 월
     * @return 날짜별 로그 개수 (date, count)
     */
    List<Map<String, Object>> findCalendarData(@Param("year") int year,
                                                @Param("month") int month);

    /**
     * 개발 로그 생성
     *
     * @param devLog 생성할 로그 정보
     * @return 생성된 행 수
     */
    int insert(DevLog devLog);

    /**
     * 개발 로그 수정
     *
     * @param devLog 수정할 로그 정보
     * @return 수정된 행 수
     */
    int update(DevLog devLog);

    /**
     * 개발 로그 삭제
     *
     * @param id 로그 ID
     * @return 삭제된 행 수
     */
    int delete(@Param("id") Long id);

    /**
     * 로그와 태그 연결
     *
     * @param logId 로그 ID
     * @param tagId 태그 ID
     * @return 생성된 행 수
     */
    int insertLogTechTag(@Param("logId") Long logId, @Param("tagId") Long tagId);

    /**
     * 로그의 모든 태그 연결 삭제
     *
     * @param logId 로그 ID
     * @return 삭제된 행 수
     */
    int deleteLogTechTags(@Param("logId") Long logId);

    /**
     * 전체 개발 로그 수 조회
     *
     * @return 전체 로그 수
     */
    int count();

    /**
     * 프로젝트별 개발 로그 수 조회
     *
     * @param projectId 프로젝트 ID
     * @return 해당 프로젝트의 로그 수
     */
    int countByProjectId(@Param("projectId") Long projectId);

    /**
     * 날짜 범위별 개발 로그 수 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 로그 수
     */
    int countByDateRange(@Param("startDate") LocalDateTime startDate,
                         @Param("endDate") LocalDateTime endDate);

    /**
     * 최근 개발 로그 조회
     *
     * @param limit 조회할 개수
     * @return 최근 로그 목록
     */
    List<DevLog> findRecent(@Param("limit") int limit);
}
