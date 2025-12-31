package com.vibecoding.devlog.service;

import com.vibecoding.devlog.mapper.TechTagMapper;
import com.vibecoding.devlog.model.TechTag;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * TechTagService 단위 테스트
 *
 * TDD 원칙에 따라 작성되었으며, TechTag의 CRUD 및 비즈니스 로직을 검증합니다.
 * 기술 태그 관리, 사용 횟수 추적, 자동 생성 기능을 테스트합니다.
 *
 * @author DevLog Test Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TechTagService 테스트")
public class TechTagServiceTest {

    @Mock
    private TechTagMapper techTagMapper;

    @InjectMocks
    private TechTagService techTagService;

    private TechTag testTechTag;
    private List<TechTag> testTechTags;

    @BeforeEach
    void setUp() {
        // 테스트용 기술 태그
        testTechTag = new TechTag();
        testTechTag.setId(1L);
        testTechTag.setName("Spring Boot");
        testTechTag.setCategory("FRAMEWORK");
        testTechTag.setUsageCount(15);

        testTechTags = Arrays.asList(testTechTag);
    }

    // ============= CREATE (생성) 테스트 =============

    @Test
    @DisplayName("기술 태그 생성 성공")
    void createTechTag_Success() {
        // Given
        TechTag newTag = new TechTag();
        newTag.setName("React");
        newTag.setCategory("FRAMEWORK");

        doNothing().when(techTagMapper).insert(any(TechTag.class));

        // When
        TechTag result = techTagService.create(newTag);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("React");
        assertThat(result.getCategory()).isEqualTo("FRAMEWORK");
        assertThat(result.getUsageCount()).isEqualTo(0);
        verify(techTagMapper, times(1)).insert(newTag);
    }

    @Test
    @DisplayName("기술 태그 생성 실패 - 이름이 null")
    void createTechTag_Fail_NullName() {
        // Given
        TechTag invalidTag = new TechTag();
        invalidTag.setName(null);
        invalidTag.setCategory("FRAMEWORK");

        // When & Then
        assertThatThrownBy(() -> techTagService.create(invalidTag))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기술 태그 이름은 필수입니다");
        verify(techTagMapper, never()).insert(any());
    }

    @Test
    @DisplayName("기술 태그 생성 실패 - 이름이 빈 문자열")
    void createTechTag_Fail_EmptyName() {
        // Given
        TechTag invalidTag = new TechTag();
        invalidTag.setName("   ");
        invalidTag.setCategory("FRAMEWORK");

        // When & Then
        assertThatThrownBy(() -> techTagService.create(invalidTag))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기술 태그 이름은 필수입니다");
        verify(techTagMapper, never()).insert(any());
    }

    @Test
    @DisplayName("기술 태그 생성 실패 - 유효하지 않은 카테고리")
    void createTechTag_Fail_InvalidCategory() {
        // Given
        TechTag invalidTag = new TechTag();
        invalidTag.setName("Spring Boot");
        invalidTag.setCategory("INVALID");

        // When & Then
        assertThatThrownBy(() -> techTagService.create(invalidTag))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 카테고리");
        verify(techTagMapper, never()).insert(any());
    }

    // ============= READ (조회) 테스트 =============

    @Test
    @DisplayName("모든 기술 태그 조회 성공")
    void findAll_Success() {
        // Given
        when(techTagMapper.findAll()).thenReturn(testTechTags);

        // When
        List<TechTag> result = techTagService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Spring Boot");
        verify(techTagMapper, times(1)).findAll();
    }

    @Test
    @DisplayName("ID로 기술 태그 조회 성공")
    void findById_Success() {
        // Given
        when(techTagMapper.findById(1L)).thenReturn(testTechTag);

        // When
        Optional<TechTag> result = techTagService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Spring Boot");
        verify(techTagMapper, times(1)).findById(1L);
    }

    @Test
    @DisplayName("ID로 기술 태그 조회 실패 - 존재하지 않음")
    void findById_Fail_NotFound() {
        // Given
        when(techTagMapper.findById(999L)).thenReturn(null);

        // When
        Optional<TechTag> result = techTagService.findById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(techTagMapper, times(1)).findById(999L);
    }

    @Test
    @DisplayName("이름으로 기술 태그 조회 성공")
    void findByName_Success() {
        // Given
        when(techTagMapper.findByName("Spring Boot")).thenReturn(testTechTag);

        // When
        Optional<TechTag> result = techTagService.findByName("Spring Boot");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(techTagMapper, times(1)).findByName("Spring Boot");
    }

    @Test
    @DisplayName("카테고리로 기술 태그 조회 성공")
    void findByCategory_Success() {
        // Given
        when(techTagMapper.findByCategory("FRAMEWORK")).thenReturn(testTechTags);

        // When
        List<TechTag> result = techTagService.findByCategory("FRAMEWORK");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("FRAMEWORK");
        verify(techTagMapper, times(1)).findByCategory("FRAMEWORK");
    }

    @Test
    @DisplayName("인기 기술 태그 조회 성공")
    void findPopular_Success() {
        // Given
        TechTag popularTag = new TechTag();
        popularTag.setId(1L);
        popularTag.setName("Spring Boot");
        popularTag.setUsageCount(50);

        List<TechTag> popularTags = Arrays.asList(popularTag);
        when(techTagMapper.findPopular(10)).thenReturn(popularTags);

        // When
        List<TechTag> result = techTagService.findPopular();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(techTagMapper, times(1)).findPopular(10);
    }

    @Test
    @DisplayName("키워드로 기술 태그 검색 성공")
    void search_Success() {
        // Given
        when(techTagMapper.search("Spring")).thenReturn(testTechTags);

        // When
        List<TechTag> result = techTagService.search("Spring");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).contains("Spring");
        verify(techTagMapper, times(1)).search("Spring");
    }

    // ============= UPDATE (수정) 테스트 =============

    @Test
    @DisplayName("기술 태그 수정 성공")
    void updateTechTag_Success() {
        // Given
        TechTag updateData = new TechTag();
        updateData.setName("Spring Boot 3.0");
        updateData.setCategory("FRAMEWORK");

        when(techTagMapper.findById(1L)).thenReturn(testTechTag);
        doNothing().when(techTagMapper).update(any(TechTag.class));

        // When
        techTagService.update(1L, updateData);

        // Then
        verify(techTagMapper, times(1)).findById(1L);
        verify(techTagMapper, times(1)).update(any(TechTag.class));
    }

    @Test
    @DisplayName("기술 태그 수정 실패 - 존재하지 않음")
    void updateTechTag_Fail_NotFound() {
        // Given
        TechTag updateData = new TechTag();
        updateData.setName("Updated");

        when(techTagMapper.findById(999L)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> techTagService.update(999L, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기술 태그를 찾을 수 없습니다: 999");
        verify(techTagMapper, never()).update(any());
    }

    // ============= DELETE (삭제) 테스트 =============

    @Test
    @DisplayName("기술 태그 삭제 성공")
    void deleteTechTag_Success() {
        // Given
        when(techTagMapper.findById(1L)).thenReturn(testTechTag);
        doNothing().when(techTagMapper).delete(1L);

        // When
        techTagService.delete(1L);

        // Then
        verify(techTagMapper, times(1)).findById(1L);
        verify(techTagMapper, times(1)).delete(1L);
    }

    @Test
    @DisplayName("기술 태그 삭제 실패 - 존재하지 않음")
    void deleteTechTag_Fail_NotFound() {
        // Given
        when(techTagMapper.findById(999L)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> techTagService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기술 태그를 찾을 수 없습니다: 999");
        verify(techTagMapper, never()).delete(anyLong());
    }

    // ============= FIND OR CREATE 테스트 =============

    @Test
    @DisplayName("기존 기술 태그 찾거나 생성 - 존재하는 경우")
    void findOrCreate_Existing() {
        // Given
        when(techTagMapper.findByName("Spring Boot")).thenReturn(testTechTag);

        // When
        TechTag result = techTagService.findOrCreate("Spring Boot", "FRAMEWORK");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(techTagMapper, times(1)).findByName("Spring Boot");
        verify(techTagMapper, never()).insert(any());
    }

    @Test
    @DisplayName("기존 기술 태그 찾거나 생성 - 없는 경우 생성")
    void findOrCreate_NotExisting() {
        // Given
        when(techTagMapper.findByName("Kotlin")).thenReturn(null);
        doNothing().when(techTagMapper).insert(any(TechTag.class));

        // When
        TechTag result = techTagService.findOrCreate("Kotlin", "LANGUAGE");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Kotlin");
        assertThat(result.getCategory()).isEqualTo("LANGUAGE");
        verify(techTagMapper, times(1)).findByName("Kotlin");
        verify(techTagMapper, times(1)).insert(any(TechTag.class));
    }

    @Test
    @DisplayName("여러 기술 태그 찾거나 생성 성공")
    void findOrCreateMultiple_Success() {
        // Given
        List<String> tagNames = Arrays.asList("Spring Boot", "React", "PostgreSQL");
        when(techTagMapper.findByName("Spring Boot")).thenReturn(testTechTag);
        when(techTagMapper.findByName("React")).thenReturn(null);
        when(techTagMapper.findByName("PostgreSQL")).thenReturn(null);
        doNothing().when(techTagMapper).insert(any(TechTag.class));

        // When
        List<TechTag> result = techTagService.findOrCreateMultiple(tagNames);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        verify(techTagMapper, times(3)).findByName(anyString());
        verify(techTagMapper, times(2)).insert(any(TechTag.class));
    }

    // ============= USAGE COUNT 테스트 =============

    @Test
    @DisplayName("기술 태그 사용 횟수 증가 성공")
    void incrementUsage_Success() {
        // Given
        when(techTagMapper.findById(1L)).thenReturn(testTechTag);
        doNothing().when(techTagMapper).incrementUsage(1L);
        testTechTag.setUsageCount(16);
        when(techTagMapper.findById(1L)).thenReturn(testTechTag);

        // When
        TechTag result = techTagService.incrementUsage(1L);

        // Then
        assertThat(result).isNotNull();
        verify(techTagMapper, times(2)).findById(1L);
        verify(techTagMapper, times(1)).incrementUsage(1L);
    }

    @Test
    @DisplayName("기술 태그 사용 횟수 감소 성공")
    void decrementUsage_Success() {
        // Given
        testTechTag.setUsageCount(10);
        when(techTagMapper.findById(1L)).thenReturn(testTechTag);
        doNothing().when(techTagMapper).decrementUsage(1L);
        testTechTag.setUsageCount(9);
        when(techTagMapper.findById(1L)).thenReturn(testTechTag);

        // When
        TechTag result = techTagService.decrementUsage(1L);

        // Then
        assertThat(result).isNotNull();
        verify(techTagMapper, times(2)).findById(1L);
        verify(techTagMapper, times(1)).decrementUsage(1L);
    }

    @Test
    @DisplayName("기술 태그 사용 횟수 감소 실패 - 이미 0")
    void decrementUsage_Fail_AlreadyZero() {
        // Given
        testTechTag.setUsageCount(0);
        when(techTagMapper.findById(1L)).thenReturn(testTechTag);

        // When & Then
        assertThatThrownBy(() -> techTagService.decrementUsage(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 횟수가 이미 0입니다");
        verify(techTagMapper, never()).decrementUsage(anyLong());
    }

    // ============= COUNT (개수 조회) 테스트 =============

    @Test
    @DisplayName("전체 기술 태그 수 조회 성공")
    void count_Success() {
        // Given
        when(techTagMapper.count()).thenReturn(15);

        // When
        int result = techTagService.getTotalCount();

        // Then
        assertThat(result).isEqualTo(15);
        verify(techTagMapper, times(1)).count();
    }

    @Test
    @DisplayName("카테고리별 기술 태그 수 조회 성공")
    void countByCategory_Success() {
        // Given
        when(techTagMapper.countByCategory("FRAMEWORK")).thenReturn(8);

        // When
        int result = techTagService.getCountByCategory("FRAMEWORK");

        // Then
        assertThat(result).isEqualTo(8);
        verify(techTagMapper, times(1)).countByCategory("FRAMEWORK");
    }

    // ============= 유효성 검사 테스트 =============

    @Test
    @DisplayName("카테고리 유효성 검사 - 유효함")
    void validateCategory_Valid() {
        // Given
        String[] validCategories = {"LANGUAGE", "FRAMEWORK", "DATABASE", "TOOL", "LIBRARY", "PLATFORM"};

        // When & Then
        for (String category : validCategories) {
            assertThatCode(() -> techTagService.validateCategory(category))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    @DisplayName("카테고리 유효성 검사 - 유효하지 않음")
    void validateCategory_Invalid() {
        // Given
        String invalidCategory = "INVALID";

        // When & Then
        assertThatThrownBy(() -> techTagService.validateCategory(invalidCategory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 카테고리");
    }

    @Test
    @DisplayName("기술 태그 유효성 검사 - 유효함")
    void validateTechTag_Valid() {
        // Given
        TechTag validTag = new TechTag();
        validTag.setName("Spring Boot");
        validTag.setCategory("FRAMEWORK");

        // When & Then
        assertThatCode(() -> techTagService.validateTechTag(validTag))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("기술 태그 유효성 검사 - 유효하지 않음 (카테고리)")
    void validateTechTag_InvalidCategory() {
        // Given
        TechTag invalidTag = new TechTag();
        invalidTag.setName("Spring Boot");
        invalidTag.setCategory("INVALID");

        // When & Then
        assertThatThrownBy(() -> techTagService.validateTechTag(invalidTag))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
