package com.gen.vacation.server.login.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 1:30
 */
@Getter
@Setter
public class AdminLoginDto {

    @NotBlank(message = "")
    private String adminId;
    @NotBlank(message = "")
    private String password;
}
