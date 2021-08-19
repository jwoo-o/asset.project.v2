package com.gen.vacation.global.common.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-18
 * Time: 오후 2:23
 */
@Getter
@Setter
public class LogSearchRequestDto extends SearchRequestDto {


    @NotNull(message = "")
    private Timestamp sDay;
    @NotNull(message = "")
    private Timestamp eDay;

    private String loginType;

}
