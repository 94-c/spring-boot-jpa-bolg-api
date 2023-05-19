package com.blog.api.service;

import com.blog.api.dto.CertificationDto;
import com.blog.api.dto.UserDto;
import com.blog.api.entity.Certification;
import com.blog.api.entity.User;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificationService {

    private final EmailTokenService emailTokenService;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CertificationDto verifyEmail(String token) {
        Certification findByEmailToken = emailTokenService.findByIdAndExpirationDateAfterAndExpired(token);

        Optional<User> findByUser = userRepository.findById(findByEmailToken.getUserId());
        findByEmailToken.setTokenToUsed();

        return CertificationDto.builder()
                .id(findByEmailToken.getId())
                .userEmail(findByUser.get().getEmail())
                .expirationDate(findByEmailToken.getExpirationDate())
                .createdAt(findByEmailToken.getDate().getCreatedAt())
                .build();
    }
}
