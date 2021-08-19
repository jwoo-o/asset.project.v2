package com.gen.vacation.global.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseToken {

    private String token;

    private String refreshToken;


    public ResponseToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
