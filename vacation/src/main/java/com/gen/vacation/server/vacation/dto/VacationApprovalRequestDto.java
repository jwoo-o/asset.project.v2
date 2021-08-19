package com.gen.vacation.server.vacation.dto;

import com.gen.vacation.global.Enum.ApprovalEnum;
import com.gen.vacation.global.Enum.VacationKind;
import com.gen.vacation.global.domain.entity.Vacation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class VacationApprovalRequestDto {

    private Long vacationId;
    private String approveState;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
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
    private String rejectReason;

    public VacationApprovalRequestDto(Long vacationId, String approveState, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDate endDay, LocalDate startDay, String vacationKind, String vacationType, String vacationReason, String vacationTel, String takeOver, String userId, String userName, String orgCode, String countDay, int orderPosition) {
        this.vacationId = vacationId;
        this.approveState = approveState;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.endDay = endDay;
        this.startDay = startDay;
        this.vacationKind = vacationKind;
        this.vacationType = vacationType;
        this.vacationReason = vacationReason;
        this.vacationTel = vacationTel;
        this.takeOver = takeOver;
        this.userId = userId;
        this.userName = userName;
        this.orgCode = orgCode;
        this.countDay = countDay;
        this.orderPosition = orderPosition;
    }

}
