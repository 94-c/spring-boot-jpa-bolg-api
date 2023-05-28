package com.blog.api.service;

import com.blog.api.dto.CategoryDto;
import com.blog.api.dto.PostDto;
import com.blog.api.entity.Category;
import com.blog.api.entity.User;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.exception.NotFoundException;
import com.blog.api.repository.CategoryRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.util.resource.PageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final static String DEFAULT_CATEGORY = "Default";

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResource<CategoryDto> findAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Category> categories = categoryRepository.findAll(pageable);

        List<Category> listOfCategories = categories.getContent();

        List<CategoryDto> content = listOfCategories.stream().map(CategoryDto::convertToCategoryDto).collect(Collectors.toList());

        PageResource<CategoryDto> pageResource = new PageResource<>();

        pageResource.setContent(content);
        pageResource.setPageNo(pageNo);
        pageResource.setPageSize(pageSize);
        pageResource.setTotalElements(categories.getTotalElements());
        pageResource.setTotalPages(categories.getTotalPages());
        pageResource.setLast(pageResource.isLast());

        return pageResource;
    }


    public CategoryDto createCategory(CategoryDto dto, String email) {
        Optional<User> findByUser = userRepository.findByEmail(email);

        User user = findByUser.orElseThrow(() -> new NotFoundException(404, "유저를 찾을 수 없습니다."));

        Category category = Category.builder()
                .name(dto.getName())
                .userId(user.getId())
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();

        Category createCategory = categoryRepository.save(category);


        return CategoryDto.convertToCategoryDto(createCategory);
    }

    public CategoryDto getCategory(Long categoryId) {
        Optional<Category> findByCategoryId = categoryRepository.findById(categoryId);

        Category findByCategory = findByCategoryId.orElseThrow(() -> new NotFoundException(404, "해당 카테고리가 존재 하지 않습니다."));

        List<PostDto> postDtos = PostDto.convertToPostDtoList(findByCategory.getPosts());

        //TODO 자식 카테고리도 출력 해야 함.
        return CategoryDto.builder()
                .id(findByCategory.getId())
                .name(findByCategory.getName())
                .postList(postDtos)
                .createdAt(findByCategory.getDate().getCreatedAt())
                .updatedAt(findByCategory.getDate().getUpdateAt())
                .build();
    }

    public CategoryDto updateCategory(Long categoryId, CategoryDto dto) {
        Optional<Category> findByCategoryId = categoryRepository.findById(categoryId);

        Category findByCategory = findByCategoryId.orElseThrow(() -> new NotFoundException(404, "해당 카테고리가 존재 하지 않습니다."));

        findByCategory.changeName(dto.getName());
        findByCategory.getDate().changeUpdateAt(LocalDateTime.now());

        Category updateCategory = categoryRepository.save(findByCategory);

        return CategoryDto.builder()
                .id(updateCategory.getId())
                .name(updateCategory.getName())
                .createdAt(updateCategory.getDate().getCreatedAt())
                .updatedAt(updateCategory.getDate().getUpdateAt())
                .build();
    }

    public void deleteCategory(Long categoryId) {
        Optional<Category> findByCategoryId = categoryRepository.findById(categoryId);

        Category category = findByCategoryId.orElseThrow(() -> new NotFoundException(404, "해당 카테고리가 존재하지 않습니다."));

        categoryRepository.delete(category);
    }

}
