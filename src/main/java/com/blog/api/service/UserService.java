package com.blog.api.service;

import com.blog.api.dto.SingUpDto;
import com.blog.api.entity.User;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.exception.NotFoundException;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public User createUser(SingUpDto signupDTO) {
        User user = User.builder()
                .email(signupDTO.getEmail())
                .name(signupDTO.getName())
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .enabled(true)
                .authority("ROLE_USER")
                .build();

        user.encryptPassword(signupDTO.getPassword());

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> findByEmail = userRepository.findByEmail(email);

        User user = findByEmail.orElseThrow(() -> new NotFoundException(403, "이메일이나 비밀번호가 틀립니다."));

        log.debug(String.valueOf(user.isEnabled()));

        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .authority(user.getAuthority())
                .enabled(user.isEnabled())
                .build();

    }

}
