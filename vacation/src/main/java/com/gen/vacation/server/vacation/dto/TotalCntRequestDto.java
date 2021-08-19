package com.gen.vacation.server.vacation.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TotalCntRequestDto {

    private String year;
    private String totalCnt;
    private String changeReason;
}
