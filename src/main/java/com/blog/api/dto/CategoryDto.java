package com.blog.api.dto;

import com.blog.api.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer parentId;
    private Long userId;

    private List<PostDto> postList;

    public static CategoryDto convertToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .userId(category.getUserId())
                .createdAt(category.getDate().getCreatedAt())
                .updatedAt(category.getDate().getUpdateAt())
                .build();
    }

}
