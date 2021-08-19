package com.gen.vacation.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-05-20
 * Time: 오전 10:37
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
public class UnauthorizedException extends RuntimeException {


}
