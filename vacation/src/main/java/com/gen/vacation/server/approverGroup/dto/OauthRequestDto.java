package com.gen.vacation.server.approverGroup.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-03
 * Time: 오후 8:08
 */
@Getter
@Setter
@ToString
public class OauthRequestDto {

    @NotBlank(message = "")
    private String adminId;
    @NotBlank(message = "")
    private String refreshToken;
}
