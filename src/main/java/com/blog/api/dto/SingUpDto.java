package com.blog.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingUpDto {
    @Email(message = "이메일 양식이 아닙니다.")
    @NotBlank(message = "이메일은 필수값 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수값 입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수값 입니다.")
    private String name;

}
