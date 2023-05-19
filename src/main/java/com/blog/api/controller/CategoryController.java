package com.blog.api.controller;

import com.blog.api.dto.CategoryDto;
import com.blog.api.util.response.SuccessResponse;
import com.blog.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/post-category")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<List<CategoryDto>> postByCategoryList() {
        List<CategoryDto> postCategoryDTOList = categoryService.postByCategoryList();

        return SuccessResponse.success(postCategoryDTOList);
    }
}
