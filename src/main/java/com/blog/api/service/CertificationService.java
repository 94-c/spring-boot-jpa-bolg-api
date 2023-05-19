package com.blog.api.service;

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
    public boolean verifyEmail(String token) {
        Certification findByEmailToken = emailTokenService.findByIdAndExpirationDateAfterAndExpired(token);

        Optional<User> findByUser = userRepository.findById(findByEmailToken.getUserId());
        findByEmailToken.setTokenToUsed();

        if (findByUser.isPresent()) {
            User user = findByUser.get();
            user.isEnabled();
            return true;
        } else {
            throw new RuntimeException("토큰 에러");
        }
    }
}
