package com.blog.api.dto;

import com.blog.api.entity.Post;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostDto {
    private Long id;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private Long viewCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private UserDto user;
    @NotBlank(message = "카테고리를 선택해주세요.")
    private String category;

    private List<CommentDto> commentList;

    public static PostDto convertToPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getDate().getCreatedAt())
                .updatedAt(post.getDate().getUpdateAt())
                .user(UserDto.convertToUserDTO(post.getUser()))
                //.category(CategoryDto.convertToCategoryDto(post.getCategory()).getName())
                .build();
    }
}
