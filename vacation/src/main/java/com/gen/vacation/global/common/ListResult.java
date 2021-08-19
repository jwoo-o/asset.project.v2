package com.gen.vacation.global.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-03-26
 * Time: 오전 11:32
 */
@Getter
@Setter
public class ListResult<T> extends CommonResult {

    private List<T> data;
}
