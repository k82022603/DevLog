package com.vibecoding.devlog.mapper;

import com.vibecoding.devlog.model.TechTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 기술 태그 MyBatis 매퍼 인터페이스
 *
 * 기술 태그 관련 데이터베이스 작업을 처리합니다.
 * TechTagMapper.xml과 매핑됩니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Mapper
public interface TechTagMapper {

    /**
     * 모든 기술 태그 조회
     *
     * @return 전체 태그 목록
     */
    List<TechTag> findAll();

    /**
     * ID로 기술 태그 조회
     *
     * @param id 태그 ID
     * @return 태그 정보, 없으면 null
     */
    TechTag findById(@Param("id") Long id);

    /**
     * 이름으로 기술 태그 조회
     *
     * @param name 태그 이름
     * @return 태그 정보, 없으면 null
     */
    TechTag findByName(@Param("name") String name);

    /**
     * 카테고리별 기술 태그 조회
     *
     * @param category 태그 카테고리
     * @return 해당 카테고리의 태그 목록
     */
    List<TechTag> findByCategory(@Param("category") String category);

    /**
     * 인기 태그 조회 (사용 횟수 기준)
     *
     * @param limit 조회할 개수
     * @return 인기 태그 목록
     */
    List<TechTag> findPopular(@Param("limit") int limit);

    /**
     * 기술 태그 검색
     *
     * @param keyword 검색 키워드
     * @return 검색 결과 목록
     */
    List<TechTag> search(@Param("keyword") String keyword);

    /**
     * 기술 태그 생성
     *
     * @param techTag 생성할 태그 정보
     * @return 생성된 행 수
     */
    int insert(TechTag techTag);

    /**
     * 기술 태그 수정
     *
     * @param techTag 수정할 태그 정보
     * @return 수정된 행 수
     */
    int update(TechTag techTag);

    /**
     * 기술 태그 삭제
     *
     * @param id 태그 ID
     * @return 삭제된 행 수
     */
    int delete(@Param("id") Long id);

    /**
     * 사용 횟수 증가
     *
     * @param id 태그 ID
     * @return 수정된 행 수
     */
    int incrementUsageCount(@Param("id") Long id);

    /**
     * 사용 횟수 감소
     *
     * @param id 태그 ID
     * @return 수정된 행 수
     */
    int decrementUsageCount(@Param("id") Long id);

    /**
     * 전체 태그 수 조회
     *
     * @return 전체 태그 수
     */
    int count();

    /**
     * 카테고리별 태그 수 조회
     *
     * @param category 태그 카테고리
     * @return 해당 카테고리의 태그 수
     */
    int countByCategory(@Param("category") String category);
}
