package org.trinityfforce.sagopalgo.category.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trinityfforce.sagopalgo.category.dto.request.AddCategoryRequestDto;
import org.trinityfforce.sagopalgo.category.dto.request.ModifyCategoryRequestDto;
import org.trinityfforce.sagopalgo.category.dto.response.AddCategoryResponseDto;
import org.trinityfforce.sagopalgo.category.dto.response.GetCategorysResponseDto;
import org.trinityfforce.sagopalgo.category.dto.response.ModifyCategoryResponseDto;
import org.trinityfforce.sagopalgo.category.service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<AddCategoryResponseDto> addCategory(
        @RequestBody @Valid AddCategoryRequestDto requestDto)
        throws BadRequestException {
        return ResponseEntity.ok(categoryService.addCategory(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<GetCategorysResponseDto>> getCategorys() {
        return ResponseEntity.ok(categoryService.getCategorys());
    }

    @PutMapping("/{categorieId}")
    public ResponseEntity<ModifyCategoryResponseDto> modifyCategory(
        @PathVariable Long categorieId,
        @RequestBody ModifyCategoryRequestDto requestDto) throws BadRequestException {
        return ResponseEntity.ok(categoryService.modifyCategory(categorieId, requestDto));
    }

    @DeleteMapping("/{categorieId}")
    public ResponseEntity removeCategory(@PathVariable Long categorieId)
        throws BadRequestException {
        categoryService.removeCategory(categorieId);
        return ResponseEntity.ok().build();
    }
}
