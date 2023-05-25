package com.blog.api.dto;

import com.blog.api.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CommentDto {

    private Long id;
    @NotBlank(message = "댓글을 작성해주세요.")
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<SubCommentDto> subCommentDtoList;

    // Entity To Dto
    public static CommentDto convertToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUserId())
                .createdAt(comment.getDate().getCreatedAt())
                .updatedAt(comment.getDate().getUpdateAt())
                .build();
    }

    //Entity To DtoList
    public static List<CommentDto> convertToCommentDtoList(List<Comment> comments) {
        Stream<Comment> stream = comments.stream();

        return stream.map(CommentDto::convertToCommentDto).collect(Collectors.toList());
    }


}
