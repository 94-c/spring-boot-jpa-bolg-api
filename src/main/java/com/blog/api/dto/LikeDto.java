package com.blog.api.dto;

import com.blog.api.entity.Like;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LikeDto {

    private Long id;
    private Long userId;
    private boolean status;
    private LocalDateTime createdAt;

    public static LikeDto convertToLikeDto(Like like) {
        return LikeDto.builder()
                .id(like.getId())
                .userId(like.getUserId())
                .status(like.isStatus())
                .createdAt(like.getDate().getCreatedAt())
                .build();
    }

}
