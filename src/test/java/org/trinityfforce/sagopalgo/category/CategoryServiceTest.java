package org.trinityfforce.sagopalgo.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_CATEGORY1;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_CATEGORY2;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_CATEGORY_ID;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_CATEGORY_NAME1;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_CATEGORY_NAME2;

import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trinityfforce.sagopalgo.category.dto.request.AddCategoryRequestDto;
import org.trinityfforce.sagopalgo.category.dto.request.ModifyCategoryRequestDto;
import org.trinityfforce.sagopalgo.category.dto.response.AddCategoryResponseDto;
import org.trinityfforce.sagopalgo.category.dto.response.ModifyCategoryResponseDto;
import org.trinityfforce.sagopalgo.category.entity.Category;
import org.trinityfforce.sagopalgo.category.repository.CategoryRepository;
import org.trinityfforce.sagopalgo.category.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 추가 성공 테스트")
    void addCategorySuccess() throws BadRequestException {
        //given
        AddCategoryRequestDto addCategoryRequestDto = new AddCategoryRequestDto(
            TEST_CATEGORY_NAME1);
        given(categoryRepository.save(any())).willReturn(
            TEST_CATEGORY1);

        //when
        AddCategoryResponseDto result = categoryService.addCategory(addCategoryRequestDto);

        //then
        verify(categoryRepository, times(1)).save(any(Category.class));
        assertEquals(result.getId(), TEST_CATEGORY_ID);
        assertEquals(result.getName(), TEST_CATEGORY_NAME1);
    }

    @Test
    @DisplayName("카테고리 추가 실패 테스트 (중복)")
    void addCategoryFailure() {
        //given
        AddCategoryRequestDto addCategoryRequestDto = new AddCategoryRequestDto(
            TEST_CATEGORY_NAME1);
        given(categoryRepository.existsByName(TEST_CATEGORY_NAME1)).willReturn(true);

        //when
        Exception exception = assertThrows(BadRequestException.class, () -> {
            categoryService.addCategory(addCategoryRequestDto);
        });

        //then
        assertEquals("이미 존재하는 카테고리 입니다", exception.getMessage());
    }

    @Test
    @DisplayName("카테고리 수정 성공 테스트")
    void modifyCategorySuccess() throws BadRequestException {
        //given
        ModifyCategoryRequestDto modifyCategoryRequestDto = new ModifyCategoryRequestDto(
            TEST_CATEGORY_NAME2);
        given(categoryRepository.findById(any())).willReturn(Optional.of(TEST_CATEGORY1));
        given(categoryRepository.save(any())).willReturn(
            TEST_CATEGORY2);

        //when
        ModifyCategoryResponseDto result = categoryService.modifyCategory(TEST_CATEGORY_ID,
            modifyCategoryRequestDto);

        //then
        verify(categoryRepository, times(1)).save(any(Category.class));
        assertEquals(result.getId(), TEST_CATEGORY_ID);
        assertEquals(result.getName(), TEST_CATEGORY_NAME2);
    }

    @Test
    @DisplayName("카테고리 수정 실패 테스트")
    void modifyCategoryFailure() {
        //given
        ModifyCategoryRequestDto modifyCategoryRequestDto = new ModifyCategoryRequestDto(
            TEST_CATEGORY_NAME2);
        given(categoryRepository.findById(any())).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(BadRequestException.class, () -> {
            categoryService.modifyCategory(TEST_CATEGORY_ID, modifyCategoryRequestDto);
        });

        //then
        assertEquals("존재 하지 않은 카테고리 입니다", exception.getMessage());
    }

    @Test
    @DisplayName("카테고리 삭제 성공 테스트")
    void removeCategorySuccess() throws BadRequestException {
        //given
        given(categoryRepository.findById(TEST_CATEGORY_ID)).willReturn(
            Optional.of(TEST_CATEGORY1));

        //when-then
        categoryService.removeCategory(TEST_CATEGORY_ID);
    }

    @Test
    @DisplayName("카테고리 삭제 실패 테스트")
    void removeCategoryFailure(){
        //given
        given(categoryRepository.findById(TEST_CATEGORY_ID)).willReturn(Optional.empty());

        //when-then
        Exception exception = assertThrows(BadRequestException.class, () -> {
            categoryService.removeCategory(TEST_CATEGORY_ID);
        });

        //then
        assertEquals("존재 하지 않은 카테고리 입니다", exception.getMessage());
    }
}
