package com.gen.vacation.server.vacation.dto;

import com.gen.vacation.global.Enum.ApprovalEnum;
import com.gen.vacation.global.Enum.VacationKind;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.Vacation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class VacationListResponseDto {

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
    private List<Map<String, Object>> fileList;
    private Long isAttach;
    private String rejectReason;

    public VacationListResponseDto(Long vacationId, ApprovalEnum approveState, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDate endDay, LocalDate startDay, VacationKind vacationKind, String vacationType, String vacationReason, String vacationTel, String takeOver, String userId, String userName, String orgCode, String countDay, int orderPosition,Long isAttach, String rejectReason) {
        this.vacationId = vacationId;
        this.approveState = approveState.getDesc();
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
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
        this.isAttach = isAttach;
        this.rejectReason = rejectReason;
    }

    public VacationListResponseDto(Vacation vacation, List<Map<String, Object>> fileList) {
        this.vacationId = vacation.getVacationId();
        this.approveState = vacation.getApproveState().getDesc();
        this.createdAt = vacation.getCreatedAt();
        this.modifiedAt = vacation.getModifiedAt();
        this.endDay = vacation.getEndDay();
        this.startDay = vacation.getStartDay();
        this.vacationKind = vacation.getVacationKind().getDesc();
        this.vacationType = vacation.getVacationType();
        this.vacationReason = vacation.getVacationReason();
        this.vacationTel = vacation.getVacationTel();
        this.takeOver = vacation.getTakeOver();
        this.userId = vacation.getUserId();
        this.userName = vacation.getUserName();
        this.orgCode = vacation.getOrgCode();
        this.countDay = vacation.getCountDay();
        this.orderPosition = vacation.getOrderPosition();
        this.fileList = fileList;
        this.rejectReason = vacation.getRejectReason();
    }
}
