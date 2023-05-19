package com.blog.api.controller;

import com.blog.api.dto.LoginDto;
import com.blog.api.dto.SingUpDto;
import com.blog.api.dto.TokenDto;
import com.blog.api.dto.UserDto;
import com.blog.api.entity.User;
import com.blog.api.jwt.provider.TokenProvider;
import com.blog.api.util.response.SuccessResponse;
import com.blog.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signup")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessResponse<UserDto> createUser(@Valid @RequestBody SingUpDto signupDTO) {
        User user = userService.createUser(signupDTO);

        return SuccessResponse.success(UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getDate().getCreatedAt())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<TokenDto>> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);

        Cookie myCookie = new Cookie("cookie", token);

        myCookie.setHttpOnly(true);
        myCookie.setMaxAge(300);
        response.addCookie(myCookie);

        HttpHeaders httpHeaders = new HttpHeaders();

        SuccessResponse<TokenDto> successResponse = SuccessResponse.success(TokenDto.builder().token(token).build());

        return new ResponseEntity<>(successResponse, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/logout")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<SuccessResponse<String>> logout() {
        HttpHeaders httpHeaders = new HttpHeaders();

        SuccessResponse<String> successResponse = SuccessResponse.success(null);

        return new ResponseEntity<>(successResponse, httpHeaders, HttpStatus.OK);
    }
}
