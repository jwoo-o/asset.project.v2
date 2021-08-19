package com.gen.vacation.server.vacation.dto;

import com.gen.vacation.global.domain.entity.VacationDeadline;
import com.gen.vacation.global.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-05-26
 * Time: 오후 5:47
 */
@Getter
@Setter
public class VacationDeadlineResponseDto {

    private String year;
    private String userId;
    private String writer;
    private JSONArray deadlines;

    public VacationDeadlineResponseDto(VacationDeadline vacationDeadline) {
        this.year = vacationDeadline.getId().getYears();
        this.userId = vacationDeadline.getId().getYears();;
        this.writer = vacationDeadline.getId().getWriteType().getValue();
        this.deadlines = (JSONArray) JsonUtil.stringToJson(vacationDeadline.getDeadlines());
    }
}
