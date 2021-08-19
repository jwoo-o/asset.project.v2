package com.gen.vacation.global.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-03-26
 * Time: 오전 11:31
 */
@Getter
@Setter
public class SingleResult<T> extends CommonResult {

    private T data;
}
