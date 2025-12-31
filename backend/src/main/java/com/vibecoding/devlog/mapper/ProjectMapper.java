package com.vibecoding.devlog.mapper;

import com.vibecoding.devlog.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 프로젝트 MyBatis 매퍼 인터페이스
 * 
 * 프로젝트 관련 데이터베이스 작업을 처리합니다.
 * ProjectMapper.xml과 매핑됩니다.
 * 
 * @author DevLog Team
 * @version 1.0
 */
@Mapper
public interface ProjectMapper {
    
    /**
     * 모든 프로젝트 조회
     * 
     * @return 전체 프로젝트 목록
     */
    List<Project> findAll();
    
    /**
     * ID로 프로젝트 조회
     * 
     * @param id 프로젝트 ID
     * @return 프로젝트 정보, 없으면 null
     */
    Project findById(@Param("id") Long id);
    
    /**
     * 상태별 프로젝트 조회
     * 
     * @param status 프로젝트 상태 (ACTIVE, COMPLETED, ON_HOLD, ARCHIVED)
     * @return 해당 상태의 프로젝트 목록
     */
    List<Project> findByStatus(@Param("status") String status);
    
    /**
     * 프로젝트 이름으로 검색
     * 
     * @param keyword 검색 키워드
     * @return 검색 결과 프로젝트 목록
     */
    List<Project> searchByName(@Param("keyword") String keyword);
    
    /**
     * 프로젝트 생성
     * 
     * @param project 생성할 프로젝트 정보
     * @return 생성된 행 수
     */
    int insert(Project project);
    
    /**
     * 프로젝트 수정
     * 
     * @param project 수정할 프로젝트 정보
     * @return 수정된 행 수
     */
    int update(Project project);
    
    /**
     * 프로젝트 진행률 업데이트
     * 
     * @param id 프로젝트 ID
     * @param progress 진행률 (0-100)
     * @return 수정된 행 수
     */
    int updateProgress(@Param("id") Long id, @Param("progress") Integer progress);
    
    /**
     * 프로젝트 상태 변경
     * 
     * @param id 프로젝트 ID
     * @param status 새로운 상태
     * @return 수정된 행 수
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 프로젝트 삭제
     * 
     * @param id 프로젝트 ID
     * @return 삭제된 행 수
     */
    int delete(@Param("id") Long id);
    
    /**
     * 전체 프로젝트 수 조회
     * 
     * @return 전체 프로젝트 수
     */
    int count();
    
    /**
     * 상태별 프로젝트 수 조회
     * 
     * @param status 프로젝트 상태
     * @return 해당 상태의 프로젝트 수
     */
    int countByStatus(@Param("status") String status);
}
