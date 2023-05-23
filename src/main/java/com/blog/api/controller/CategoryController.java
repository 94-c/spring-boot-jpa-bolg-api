package com.blog.api.controller;

import com.blog.api.dto.CategoryDto;
import com.blog.api.entity.Category;
import com.blog.api.util.response.SuccessResponse;
import com.blog.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessResponse<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto,
                                                       Principal principal) {
        CategoryDto createCategory = categoryService.createCategory(categoryDto, principal.getName());

        return SuccessResponse.success(createCategory);
    }

    @GetMapping("categories/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<CategoryDto> getCategory(@PathVariable(name = "id") Long categoryId) {

        CategoryDto findByCategory = categoryService.getCategory(categoryId);

        return SuccessResponse.success(findByCategory);
    }

    @PutMapping("categories/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto dto,
                                                       @PathVariable(name = "id") Long categoryId) {

        CategoryDto updateCategory = categoryService.updateCategory(categoryId, dto);

        return SuccessResponse.success(updateCategory);
    }

    @DeleteMapping("categories/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<String> deleteCategory(@PathVariable(name = "id") Long categoryId) {

        categoryService.deleteCategory(categoryId);

        return SuccessResponse.success(null);
    }

}
