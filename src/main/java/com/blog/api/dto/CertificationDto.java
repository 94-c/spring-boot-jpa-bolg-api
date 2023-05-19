package com.blog.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CertificationDto {

    private String id;

    private LocalDateTime expirationDate;

    private LocalDateTime createdAt;

    private String userEmail;


}
