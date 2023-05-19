package com.blog.api.controller;

import com.blog.api.dto.NotificationDto;
import com.blog.api.util.response.SuccessResponse;
import com.blog.api.service.NotificationService;
import com.blog.api.util.PagingUtil;
import com.blog.api.util.resource.PageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    /**
     * TODO 등록, 수정, 삭제는 ROLE = ADMIN 계정만 가능 하도록
     * 전체 조회 및 상세 조회는 ADMIN, USER 상관 없이 확인 할 수 있도록
     * 파일업로드 구현
     */
    private final NotificationService notificationService;

    @GetMapping("notifications")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResource<NotificationDto> getAllNotifications (
            @RequestParam(value = "pageNo", defaultValue = PagingUtil.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PagingUtil.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PagingUtil.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PagingUtil.DEFAULT_SORT_DIREACTION, required = false) String sortDir) {

        return notificationService.findAllNotifications(pageNo, pageSize, sortBy, sortDir);
    }

    @PostMapping("notifications")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessResponse<NotificationDto> createNotification(@Valid @RequestBody NotificationDto notificationDto, Principal principal) {

        NotificationDto notification = notificationService.createNotification(notificationDto, principal.getName());

        return SuccessResponse.success(notification);
    }

    @GetMapping("notifications/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<NotificationDto> getNotification(@PathVariable(name = "id") Long id) {
        NotificationDto notification = notificationService.getNotification(id);

        return SuccessResponse.success(notification);
    }

    @PutMapping("notifications/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<NotificationDto> updateNotification(@Valid @RequestBody NotificationDto notificationDto, @PathVariable(name = "id") Long notificationId) {
        NotificationDto notification = notificationService.updateNotification(notificationId, notificationDto);

        return SuccessResponse.success(notification);
    }

    @DeleteMapping("notifications/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<String> deleteNotification(@PathVariable(name = "id") Long notificationId) {
        notificationService.deleteNotification(notificationId);

        return SuccessResponse.success(null);
    }

 }
