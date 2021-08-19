package com.gen.vacation.server.vacation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gen.vacation.global.Enum.ApprovalEnum;
import com.gen.vacation.global.Enum.VacationKind;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.ApproverDetail;
import com.gen.vacation.global.domain.entity.Vacation;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class VacationListRequestDto {

    private Long vacationId;
    private String approveState;
    private Timestamp endDay;
    private Timestamp startDay;
    private String vacationKind;
    private String vacationType;
    private String vacationReason;
    private String vacationTel;
    private String takeOver;
    private String userId;
    private String userName;
    private String orgCode;
    private String countDay;
    private int orderPosition;
    private Long[] fileIds;

    public Vacation toEntity() {
        return Vacation.builder()
                .approveState(ApprovalEnum.valueOf(approveState))
                .orgCode(orgCode)
                .endDay(endDay.toLocalDateTime().toLocalDate())
                .startDay(startDay.toLocalDateTime().toLocalDate())
                .vacationKind(VacationKind.valueOf(vacationKind))
                .vacationType(vacationType)
                .vacationReason(vacationReason)
                .vacationTel(vacationTel)
                .takeOver(takeOver)
                .userId(userId)
                .userName(userName)
                .orgCode(orgCode)
                .countDay(countDay)
                .orderPosition(orderPosition)
                .build();
    }

}
