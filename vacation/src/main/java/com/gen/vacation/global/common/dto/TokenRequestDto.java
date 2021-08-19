package com.gen.vacation.global.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenRequestDto {

    private String refreshToken;

    private String id;

    private String type;
}
