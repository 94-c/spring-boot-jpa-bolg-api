package com.blog.api.service;

import com.blog.api.dto.CategoryDto;
import com.blog.api.entity.Category;
import com.blog.api.repository.CategoryRepository;
import com.blog.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> postByCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();

        Stream<Category> stream = categoryList.stream();

        return stream.map(CategoryDto::convertToCategoryDto).collect(Collectors.toList());
    }
}
