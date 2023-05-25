package com.blog.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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


}
