package com.gen.vacation.server.login.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserLoginDto {
    @NotBlank(message = "")
    private String userId;
    @NotBlank(message = "")
    private String password;
}
