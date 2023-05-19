package com.blog.api.service;

import com.blog.api.dto.NotificationDto;
import com.blog.api.dto.PostDto;
import com.blog.api.dto.UserDto;
import com.blog.api.entity.Notification;
import com.blog.api.entity.Post;
import com.blog.api.entity.User;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.exception.NotFoundException;
import com.blog.api.repository.NotificationRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.util.resource.PageResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private User getUserInfo(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);

        return byEmail.orElseThrow(() -> new NotFoundException(404, "유저가 없습니다."));
    }

    @Transactional(readOnly = true)
    public PageResource<NotificationDto> findAllNotifications(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Notification> posts = notificationRepository.findAll(pageable);

        List<Notification> listOfPosts = posts.getContent();

        List<NotificationDto> content = listOfPosts.stream().map(NotificationDto::convertToNotificationDto).collect(Collectors.toList());

        PageResource<NotificationDto> pageResource = new PageResource<>();

        pageResource.setContent(content);
        pageResource.setPageNo(pageNo);
        pageResource.setPageSize(pageSize);
        pageResource.setTotalElements(posts.getTotalElements());
        pageResource.setTotalPages(posts.getTotalPages());
        pageResource.setLast(pageResource.isLast());

        return pageResource;
    }

    public NotificationDto createNotification(NotificationDto dto, String email) {
        User findUser = getUserInfo(email);

        Notification notification = Notification.builder()
                .user(findUser)
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();

        Notification createNotification = notificationRepository.save(notification);

        return NotificationDto.builder()
                .id(createNotification.getId())
                .user(UserDto.convertToUserDTO(createNotification.getUser()))
                .title(createNotification.getTitle())
                .content(createNotification.getContent())
                .createdAt(createNotification.getDate().getCreatedAt())
                .build();
    }

    public NotificationDto getNotification(Long notificationId) {
        Optional<Notification> findByNotificationId = notificationRepository.findById(notificationId);

        Notification findNotification = findByNotificationId.orElseThrow(() -> new NotFoundException(404, "해당 공지시항 존재하지 않습니다."));

        return NotificationDto.builder()
                .id(findNotification.getId())
                .title(findNotification.getTitle())
                .content(findNotification.getContent())
                .createdAt(findNotification.getDate().getCreatedAt())
                .user(UserDto.convertToUserDTO(findNotification.getUser()))
                .build();
    }

    public NotificationDto updateNotification(Long notificationId, NotificationDto dto) {
        Optional<Notification> findByNotificationId = notificationRepository.findById(notificationId);

        Notification notification = findByNotificationId.orElseThrow(() -> new NotFoundException(404, "해당 공지시항 존재하지 않습니다."));

        notification.changeTitle(dto.getTitle());
        notification.changeContent(dto.getContent());
        notification.getDate().changeUpdateAt(LocalDateTime.now());

        Notification updateNotification = notificationRepository.save(notification);

        return NotificationDto.builder()
                .id(updateNotification.getId())
                .title(updateNotification.getTitle())
                .content(updateNotification.getContent())
                .updatedAt(updateNotification.getDate().getUpdateAt())
                .user(UserDto.convertToUserDTO(updateNotification.getUser()))
                .build();
    }

    public void deleteNotification(Long notificationId) {
        Optional<Notification> findByNotificationId = notificationRepository.findById(notificationId);

        Notification notification = findByNotificationId.orElseThrow(() -> new NotFoundException(404, "해당 공지시항 존재하지 않습니다."));

        notificationRepository.delete(notification);
    }

}
