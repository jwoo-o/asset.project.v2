package com.gen.vacation.server.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-13
 * Time: 오후 1:30
 */
@Getter
@Setter
public class UserPasswordRequestDto {

    private String password = "";
    @NotBlank
    private String userId = "";
}
