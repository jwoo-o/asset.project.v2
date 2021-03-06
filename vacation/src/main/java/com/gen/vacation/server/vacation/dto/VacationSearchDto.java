package com.gen.vacation.server.vacation.dto;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class VacationSearchDto extends SearchRequestDto {
    private String approveState;
    private String year;
    private String month;
}
