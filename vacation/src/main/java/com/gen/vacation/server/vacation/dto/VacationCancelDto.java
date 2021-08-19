package com.gen.vacation.server.vacation.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-05-04
 * Time: 오후 12:16
 */
@Getter
@Setter
public class VacationCancelDto {

    private Long vacationId;
    private String reason;

}
