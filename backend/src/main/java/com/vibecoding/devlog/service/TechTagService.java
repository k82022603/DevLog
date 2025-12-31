package com.vibecoding.devlog.service;

import com.vibecoding.devlog.mapper.TechTagMapper;
import com.vibecoding.devlog.model.TechTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 기술 태그 서비스
 *
 * 기술 태그 관련 비즈니스 로직을 처리합니다.
 * 태그 자동 생성 및 사용 횟수 관리를 담당합니다.
 *
 * @author DevLog Team
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechTagService {

    private final TechTagMapper techTagMapper;

    /**
     * 모든 기술 태그 조회
     *
     * @return 전체 태그 목록
     */
    public List<TechTag> findAll() {
        log.debug("Finding all tech tags");
        return techTagMapper.findAll();
    }

    /**
     * ID로 기술 태그 조회
     *
     * @param id 태그 ID
     * @return Optional로 감싼 태그
     */
    public Optional<TechTag> findById(Long id) {
        log.debug("Finding tech tag by id: {}", id);
        return Optional.ofNullable(techTagMapper.findById(id));
    }

    /**
     * 이름으로 기술 태그 조회
     *
     * @param name 태그 이름
     * @return Optional로 감싼 태그
     */
    public Optional<TechTag> findByName(String name) {
        log.debug("Finding tech tag by name: {}", name);
        return Optional.ofNullable(techTagMapper.findByName(name));
    }

    /**
     * 카테고리별 기술 태그 조회
     *
     * @param category 태그 카테고리
     * @return 해당 카테고리의 태그 목록
     */
    public List<TechTag> findByCategory(String category) {
        log.debug("Finding tech tags by category: {}", category);
        validateCategory(category);
        return techTagMapper.findByCategory(category);
    }

    /**
     * 인기 태그 조회
     *
     * @param limit 조회할 개수
     * @return 인기 태그 목록
     */
    public List<TechTag> findPopular(int limit) {
        log.debug("Finding popular tech tags, limit: {}", limit);
        if (limit <= 0) {
            limit = 10;
        }
        return techTagMapper.findPopular(limit);
    }

    /**
     * 기술 태그 검색
     *
     * @param keyword 검색 키워드
     * @return 검색 결과 목록
     */
    public List<TechTag> search(String keyword) {
        log.debug("Searching tech tags by keyword: {}", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return techTagMapper.search(keyword.trim());
    }

    /**
     * 기술 태그 생성
     *
     * @param techTag 생성할 태그 정보
     * @return 생성된 태그
     */
    @Transactional
    public TechTag create(TechTag techTag) {
        log.debug("Creating new tech tag: {}", techTag.getName());

        validateTechTag(techTag);

        // 중복 체크
        TechTag existing = techTagMapper.findByName(techTag.getName());
        if (existing != null) {
            throw new IllegalArgumentException("Tech tag already exists: " + techTag.getName());
        }

        // 기본값 설정
        if (techTag.getUsageCount() == null) {
            techTag.setUsageCount(0);
        }
        if (techTag.getCategory() == null) {
            techTag.setCategory("TOOL");
        }

        int result = techTagMapper.insert(techTag);
        if (result == 0) {
            throw new RuntimeException("Failed to create tech tag");
        }

        log.info("Tech tag created successfully with id: {}", techTag.getId());
        return techTag;
    }

    /**
     * 기술 태그 수정
     *
     * @param id 태그 ID
     * @param techTag 수정할 태그 정보
     * @return 수정된 태그
     */
    @Transactional
    public TechTag update(Long id, TechTag techTag) {
        log.debug("Updating tech tag with id: {}", id);

        TechTag existing = techTagMapper.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Tech tag not found with id: " + id);
        }

        validateTechTag(techTag);

        techTag.setId(id);
        int result = techTagMapper.update(techTag);
        if (result == 0) {
            throw new RuntimeException("Failed to update tech tag");
        }

        log.info("Tech tag updated successfully with id: {}", id);
        return techTagMapper.findById(id);
    }

    /**
     * 기술 태그 삭제
     *
     * @param id 태그 ID
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting tech tag with id: {}", id);

        TechTag existing = techTagMapper.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Tech tag not found with id: " + id);
        }

        int result = techTagMapper.delete(id);
        if (result == 0) {
            throw new RuntimeException("Failed to delete tech tag");
        }

        log.info("Tech tag deleted successfully with id: {}", id);
    }

    /**
     * 태그 이름으로 조회 또는 생성
     * 태그가 없으면 자동으로 생성합니다.
     *
     * @param name 태그 이름
     * @param category 태그 카테고리 (선택사항)
     * @return 조회 또는 생성된 태그
     */
    @Transactional
    public TechTag findOrCreate(String name, String category) {
        log.debug("Finding or creating tech tag: {}", name);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name is required");
        }

        String trimmedName = name.trim();

        // 기존 태그 조회
        TechTag existing = techTagMapper.findByName(trimmedName);
        if (existing != null) {
            log.debug("Tech tag found: {}", trimmedName);
            return existing;
        }

        // 새 태그 생성
        log.debug("Creating new tech tag: {}", trimmedName);
        TechTag newTag = TechTag.builder()
                .name(trimmedName)
                .category(category != null ? category : "TOOL")
                .usageCount(0)
                .build();

        techTagMapper.insert(newTag);
        log.info("New tech tag created: {} (id: {})", trimmedName, newTag.getId());

        return newTag;
    }

    /**
     * 여러 태그를 한 번에 조회 또는 생성
     *
     * @param tagNames 태그 이름 목록
     * @return 태그 목록
     */
    @Transactional
    public List<TechTag> findOrCreateMultiple(List<String> tagNames) {
        log.debug("Finding or creating multiple tech tags: {}", tagNames);

        List<TechTag> tags = new ArrayList<>();

        if (tagNames == null || tagNames.isEmpty()) {
            return tags;
        }

        for (String name : tagNames) {
            if (name != null && !name.trim().isEmpty()) {
                TechTag tag = findOrCreate(name.trim(), null);
                tags.add(tag);
            }
        }

        return tags;
    }

    /**
     * 사용 횟수 증가
     *
     * @param id 태그 ID
     */
    @Transactional
    public void incrementUsage(Long id) {
        log.debug("Incrementing usage count for tag: {}", id);

        TechTag existing = techTagMapper.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Tech tag not found with id: " + id);
        }

        techTagMapper.incrementUsageCount(id);
        log.debug("Usage count incremented for tag: {}", id);
    }

    /**
     * 사용 횟수 감소
     *
     * @param id 태그 ID
     */
    @Transactional
    public void decrementUsage(Long id) {
        log.debug("Decrementing usage count for tag: {}", id);

        TechTag existing = techTagMapper.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Tech tag not found with id: " + id);
        }

        techTagMapper.decrementUsageCount(id);
        log.debug("Usage count decremented for tag: {}", id);
    }

    /**
     * 전체 태그 수 조회
     *
     * @return 전체 태그 수
     */
    public int getTotalCount() {
        return techTagMapper.count();
    }

    /**
     * 카테고리별 태그 수 조회
     *
     * @param category 태그 카테고리
     * @return 해당 카테고리의 태그 수
     */
    public int getCountByCategory(String category) {
        validateCategory(category);
        return techTagMapper.countByCategory(category);
    }

    /**
     * 기술 태그 유효성 검증
     *
     * @param techTag 검증할 태그
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateTechTag(TechTag techTag) {
        if (techTag == null) {
            throw new IllegalArgumentException("Tech tag cannot be null");
        }
        if (techTag.getName() == null || techTag.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tech tag name is required");
        }
        if (techTag.getCategory() != null) {
            validateCategory(techTag.getCategory());
        }
    }

    /**
     * 카테고리 유효성 검증
     *
     * @param category 검증할 카테고리
     * @throws IllegalArgumentException 유효하지 않은 카테고리인 경우
     */
    private void validateCategory(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (!category.matches("^(LANGUAGE|FRAMEWORK|DATABASE|TOOL|LIBRARY|PLATFORM)$")) {
            throw new IllegalArgumentException(
                "Invalid category. Must be one of: LANGUAGE, FRAMEWORK, DATABASE, TOOL, LIBRARY, PLATFORM"
            );
        }
    }
}
