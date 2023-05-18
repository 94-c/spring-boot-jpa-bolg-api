package com.blog.api.dto;

import com.blog.api.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CommentDto {

    private Long id;
    @NotBlank
    private String content;
    private UserDto user;

    // Entity To Dto
    public static CommentDto converToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(UserDto.convertToUserDTO(comment.getUser()))
                .build();
    }


}
