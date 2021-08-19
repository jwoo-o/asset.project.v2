package com.gen.vacation.server.login.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 2:17
 */
@Getter
@Setter
public class UserLoginResponseDto {


    private boolean pwdChange;

    private String token;

    private String refreshToken;

    private String errCode;

}
