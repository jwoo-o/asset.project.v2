package com.gen.vacation.server.vacation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VacationAdminDeleteRequestDto {
    private Long vacationId;
    private String userId;
    private String countDay;
    private String year;
    private boolean check;
}
