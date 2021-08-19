package com.gen.vacation.server.approver.dto;

import com.gen.vacation.global.Enum.ApprovalEnum;
import com.gen.vacation.global.Enum.VacationKind;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ApproverVacationListResponseDto {
    private Long vacationId;
    private String approveState;
    private String createdAt;
    private String modifiedAt;
    private LocalDate endDay;
    private LocalDate startDay;
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
    private boolean isAttach;

    public ApproverVacationListResponseDto(Long vacationId, ApprovalEnum approveState, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDate endDay, LocalDate startDay, VacationKind vacationKind, String vacationType, String vacationReason, String vacationTel, String takeOver, String userId, String userName, String orgCode, String countDay, int orderPosition, Long isAttach) {
        this.vacationId = vacationId;
        this.approveState = approveState.getDesc();
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.modifiedAt = modifiedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.endDay = endDay;
        this.startDay = startDay;
        this.vacationKind = vacationKind.getDesc();
        this.vacationType = vacationType;
        this.vacationReason = vacationReason;
        this.vacationTel = vacationTel;
        this.takeOver = takeOver;
        this.userId = userId;
        this.userName = userName;
        this.orgCode = orgCode;
        this.countDay = countDay;
        this.orderPosition = orderPosition;
        if(isAttach>0){
            this.isAttach = true;
        }
    }

}
