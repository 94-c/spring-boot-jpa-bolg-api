package com.blog.api.dto;

import com.blog.api.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    //Entity To DtoList
    public static List<CommentDto> convertToCommentDtoList(List<Comment> comments) {
        Stream<Comment> stream = comments.stream();

        return stream.map(CommentDto::converToCommentDto).collect(Collectors.toList());
    }


}
