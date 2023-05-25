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
public class SubCommentDto {

    private Long id;
    @NotBlank(message = "댓글을 작성해주세요.")
    private String content;
    private Long userId;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //Entity To Dto
    public static SubCommentDto convertToSubCommentDto(Comment comment) {
        return SubCommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUserId())
                .parentId(comment.getParentId())
                .createdAt(comment.getDate().getCreatedAt())
                .updatedAt(comment.getDate().getUpdateAt())
                .build();
    }
    public static List<SubCommentDto> convertToSubCommentDtoList(List<Comment> comments) {
        Stream<Comment> stream = comments.stream();

        return stream.map(SubCommentDto::convertToSubCommentDto).collect(Collectors.toList());
    }


}
