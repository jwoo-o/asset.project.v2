package com.gen.vacation.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-04-30
 * Time: 오전 9:16
 */
@Getter
@Setter
@AllArgsConstructor
public class SeatResponseDto {

    private Long id;
    private String className;
    private String left;
    private String top;
    private String userName;
    private String userId;
    private String imgSrc;
    private String tel;
    private String email;
    private String orgCode;
    private String orgName;
    private String fullCode;
    private String fullName;
    private String color;
    private String rankNm;
    private String jobNm;
    private String ex;


}
