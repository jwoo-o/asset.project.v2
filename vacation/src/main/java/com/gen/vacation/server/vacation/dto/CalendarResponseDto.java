package com.gen.vacation.server.vacation.dto;

import com.gen.vacation.global.enums.VacationKind;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-04-29
 * Time: 오전 11:20
 */
@Getter
@Setter
public class CalendarResponseDto {

    private Long id;
    private String category;
    private String title;

    /** vacationKind */
    private String calendarId;

    private LocalDateTime start;
    private LocalDateTime end;

    private String userId;

    public CalendarResponseDto(Long id, VacationKind vacationKind, String userName, LocalDate start, LocalDate end,String userId) {
        this.id = id;
        this.calendarId = vacationKind.getKey();
        this.title = userName;
        if(start.equals(end)){
            this.category = "time";
        }else{

            this.category = "allday";
        }
        this.start = start.atTime(9,0,0);
        this.end = end.atTime(18,0,0);
        if(vacationKind.equals(VacationKind.MORNING)) {
            this.start = start.atTime(9,0,0);
            this.end = end.atTime(14,0,0);
        }
        if(vacationKind.equals(VacationKind.AFTERNOON)) {
            this.start = start.atTime(14,0,0);
            this.end = end.atTime(18,0,0);
        }

        this.userId = userId;


    }
}
