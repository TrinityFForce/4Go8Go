package org.trinityfforce.sagopalgo.category.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.category.dto.request.AddCategoryRequestDto;
import org.trinityfforce.sagopalgo.category.dto.request.ModifyCategoryRequestDto;
import org.trinityfforce.sagopalgo.category.dto.response.AddCategoryResponseDto;
import org.trinityfforce.sagopalgo.category.dto.response.GetCategorysResponseDto;
import org.trinityfforce.sagopalgo.category.dto.response.ModifyCategoryResponseDto;
import org.trinityfforce.sagopalgo.category.entity.Category;
import org.trinityfforce.sagopalgo.category.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public AddCategoryResponseDto addCategory(AddCategoryRequestDto requestDto)
        throws BadRequestException {
        if (isCategoryExists(requestDto.getName())) {
            throw new BadRequestException("이미 존재하는 카테고리 입니다");
        }
        Category category = new Category(requestDto);
        Category saveCategory = categoryRepository.save(category);

        return new AddCategoryResponseDto(saveCategory);
    }

    private boolean isCategoryExists(String category) {
        return categoryRepository.existsByName(category);
    }

    public List<GetCategorysResponseDto> getCategorys() {
        return categoryRepository.findAll().stream()
            .map(category -> new GetCategorysResponseDto(category))
            .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "item", allEntries = true)
    public ModifyCategoryResponseDto modifyCategory(Long categorieId,
        ModifyCategoryRequestDto requestDto)
        throws BadRequestException {
        Category category = findCategoryById(categorieId);
        category.update(requestDto);
        Category saveCategory = categoryRepository.save(category);
        return new ModifyCategoryResponseDto(saveCategory);
    }

    private Category findCategoryById(Long id) throws BadRequestException {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new BadRequestException("존재 하지 않은 카테고리 입니다"));
    }

    private Category findCategoryByName(String name) throws BadRequestException {
        return categoryRepository.findByName(name)
            .orElseThrow(() -> new BadRequestException("존재 하지 않은 카테고리 입니다"));
    }

    @Transactional
    public void removeCategory(Long categorieId) throws BadRequestException {
        Category category = findCategoryById(categorieId);
        categoryRepository.delete(category);
    }
}
