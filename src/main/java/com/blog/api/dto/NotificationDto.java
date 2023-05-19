package com.blog.api.dto;

import com.blog.api.entity.Notification;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class NotificationDto {

    private Long id;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private UserDto user;

    public static NotificationDto convertToNotificationDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .createdAt(notification.getDate().getCreatedAt())
                .updatedAt(notification.getDate().getUpdateAt())
                .user(UserDto.convertToUserDTO(notification.getUser()))
                .build();
    }
}
