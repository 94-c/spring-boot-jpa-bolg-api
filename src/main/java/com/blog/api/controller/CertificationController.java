package com.blog.api.controller;

import com.blog.api.dto.CertificationDto;
import com.blog.api.service.CertificationService;
import com.blog.api.service.EmailTokenService;
import com.blog.api.util.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CertificationController {

    private final CertificationService certificationService;

    @GetMapping("confirm-email")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<Boolean> viewConfirmEmail(@RequestParam String token) {

        CertificationDto result = certificationService.verifyEmail(token);

        return SuccessResponse.success(result);
    }
}
