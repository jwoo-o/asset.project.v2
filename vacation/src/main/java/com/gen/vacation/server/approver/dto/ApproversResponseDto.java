package com.gen.vacation.server.approver.dto;

import com.gen.vacation.global.Enum.ApproverFlagEnum;
import com.gen.vacation.global.domain.entity.Approver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ApproversResponseDto {
    private String userName;
    private int order;
    private ApproverFlagEnum approverFlag;
    private LocalDateTime modifiedAt;

    public ApproversResponseDto(Approver approver) {
        this.userName = approver.getUserName();
        this.order = approver.getOrder();
        this.modifiedAt = approver.getModifiedAt();
        approverFlag = approver.getApprovalFlag();
    }
}
