package com.blog.api.dto;

import com.blog.api.entity.Post;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private Long userId;
    @NotBlank(message = "카테고리를 선택해주세요.")
    private String category;

    private List<CommentDto> commentList;

    //Entity To Dto
    public static PostDto convertToPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getDate().getCreatedAt())
                .updatedAt(post.getDate().getUpdateAt())
                .userId(post.getUserId())
                .build();
    }

    //Entity To DtoList
    public static List<PostDto> convertToPostDtoList(List<Post> posts) {
        Stream<Post> stream = posts.stream();

        return stream.map(PostDto::convertToPostDto).collect(Collectors.toList());
    }

}
