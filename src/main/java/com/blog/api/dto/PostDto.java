package com.blog.api.dto;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostDto {

    private Long id;
    private String title;

    private String content;

    private Long viewCount;

    private String createdAt;

}
