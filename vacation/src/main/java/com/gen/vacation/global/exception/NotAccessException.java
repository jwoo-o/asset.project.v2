package com.gen.vacation.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-06
 * Time: 오후 6:29
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbidden")
public class NotAccessException {
}
