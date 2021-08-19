package com.gen.vacation.global.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-03-18
 * Time: 오후 3:34
 * 실행결과 공통 모델
 */
@Getter
@Setter
public class CommonResult {

    @ApiModelProperty(value = "응답 성공여부 : ")
    private boolean success;

    @ApiModelProperty(value = "응답 코드 번호 : ")
    private String code;

    @ApiModelProperty(value = "응답 메시지 :")
    private String result;


}
