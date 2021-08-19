package com.gen.vacation.global.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-05-20
 * Time: 오후 4:42
 */
@Getter
@Setter
public class ErrorResult {

    private String message;

    private String code;

    private String error;
}
