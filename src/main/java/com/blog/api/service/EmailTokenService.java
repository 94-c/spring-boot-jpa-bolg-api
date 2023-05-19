package com.blog.api.service;

import com.blog.api.entity.Certification;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.repository.CertificationRepository;
import com.blog.api.util.Md5Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailTokenService {

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;
    private final EmailSenderService emailSenderService;
    private final CertificationRepository certificationRepository;


    //이메일 인증 토큰 생성
    public static Certification createEmailToken(Long userId) {
        return Certification.builder()
                .expirationDate(LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE))
                .expired(false)
                .userId(userId)
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();
    }

    //이메일 인증 토큰 생성
    public void createEmailToken(Long userId, String receiverEmail) {

        //이메일 토큰 저장
        Certification certification = createEmailToken(userId);
        certificationRepository.save(certification);

        //이메일 전송
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
        mailMessage.setSubject("회원가입 이메일 인증");
        mailMessage.setText("http://localhost:8080/confirm-email?token=" + certification.getId());

        emailSenderService.sendEmail(mailMessage);

    }

    //유효한 토큰 가져오기
    public Certification findByIdAndExpirationDateAfterAndExpired(String token) {
        Optional<Certification> emailToke = certificationRepository.findByIdAndExpirationDateAfterAndExpired(token, LocalDateTime.now(), false);

        return emailToke.orElseThrow(() -> new RuntimeException("인증 토큰이 존재하지 않습니다."));
    }


}

