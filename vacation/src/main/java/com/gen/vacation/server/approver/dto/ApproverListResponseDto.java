package com.gen.vacation.server.approver.dto;

import com.gen.vacation.global.domain.entity.Approver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApproverListResponseDto {
    private Long id;
    private Long vacationId;
    private Long approverDetailCode;
    private Long approverGroupCode;
    private String orgCode;
    private String orgName;
    private String rankCd;
    private String rankNm;
    private String jobName;
    private String division;
    private String userId;
    private String userName;
    private int order;

   /* public ApproverListResponseDto( Map<String, Object> data) {
        this.vacationId = (Long) data.get("vacationId");
        this.approverDetailCode = (Long) data.get("approverDetailCode");
        this.approverGroupCode = (Long) data.get("approverGroupCode");
        this.orgCode = (String) data.get("orgCode");
        this.orgName = (String) data.get("orgName");
        this.rankCd = (String) data.get("rankCd");
        this.rankNm = (String) data.get("rankNm");
        this.userJob = (String) data.get("userJob");
        this.division = (String) data.get("division");
        this.userId = (String) data.get("userId");
        this.userName = (String) data.get("userName");
        this.order = (int) data.get("order");
    }*/

    public Approver toEntity() {
        return Approver.builder()
                .vacationId(vacationId)
                .approverDetailCode(approverDetailCode)
                .approverGroupCode(approverGroupCode)
                .division(division)
                .order(order)
                .orgCode(orgCode)
                .orgName(orgName)
                .rankCd(rankCd)
                .rankNm(rankNm)
                .jobName(jobName)
                .userId(userId)
                .userName(userName).build();
    }
}
