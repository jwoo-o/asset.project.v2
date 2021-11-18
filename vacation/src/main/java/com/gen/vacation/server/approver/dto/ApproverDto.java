package com.gen.vacation.server.approver.dto;

import com.gen.vacation.global.enums.ApproverFlagEnum;
import com.gen.vacation.global.enums.DivisionEnum;
import com.gen.vacation.global.domain.entity.Approver;
import com.gen.vacation.global.domain.entity.ApproverDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApproverDto {

    private Long vacationId;

    private Long approverDetailCode;

    private Long approverGroupCode;

    private String division;

    private int order;

    private String orgCode;

    private String orgName;

    private String rankCd;

    private String rankNm;

    private String jobName;

    private String userId;

    private String userName;

    private String email;

    public ApproverDto(ApproverDetail approverDetail, Long vacationId) {
        this.approverDetailCode = approverDetail.getApproverDetailCode();
        this.approverGroupCode = approverDetail.getApproverGroupCode();
        this.division = approverDetail.getDivision().name();
        this.order = approverDetail.getOrder();
        this.orgCode = approverDetail.getOrgCode();
        this.orgName = approverDetail.getOrgName();
        this.rankCd = approverDetail.getRankCd();
        this.rankNm = approverDetail.getRankNm();
        this.jobName = approverDetail.getJobName();
        this.userId = approverDetail.getUserId();
        this.userName = approverDetail.getUserName();
        this.email = approverDetail.getEmail();
        this.vacationId = vacationId;
    }


    public Approver toEntity() {
        return Approver.builder()
                .approvalFlag(ApproverFlagEnum.WAIT)
                .approverDetailCode(approverDetailCode)
                .approverGroupCode(approverGroupCode)
                .division(DivisionEnum.valueOf(division))
                .jobName(jobName)
                .order(order)
                .orgCode(orgCode)
                .orgName(orgName)
                .rankCd(rankCd)
                .rankNm(rankNm)
                .userId(userId)
                .userName(userName)
                .vacationId(vacationId)
                .email(email)
                .build();
    }

}
