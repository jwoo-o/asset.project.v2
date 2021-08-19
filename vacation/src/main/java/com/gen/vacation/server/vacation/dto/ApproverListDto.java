package com.gen.vacation.server.vacation.dto;

import com.gen.vacation.global.Enum.ApproverFlagEnum;
import com.gen.vacation.global.domain.common.BooleanToYNConverter;
import com.gen.vacation.global.domain.entity.Approver;
import com.gen.vacation.global.domain.entity.ApproverDetail;
import com.gen.vacation.global.domain.entity.Vacation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ApproverListDto {

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
    private ApproverFlagEnum approvalFlag;

    /*public ApproverListDto(Approver approver) {
        this.id = approver.getId();
        this.vacationId = approver.getVacationId();
        this.approverDetailCode = approver.getApproverDetailCode();
        this.approverGroupCode = approver.getApproverGroupCode();
        this.orgCode = approver.getOrgCode();
        this.orgName = approver.getOrgName();
        this.rankCd = approver.getRankCd();
        this.rankNm = approver.getRankNm();
        this.jobName = approver.getJobName();
        this.division = approver.getDivision();
        this.userId = approver.getUserId();
        this.userName = approver.getUserName();
        this.order = approver.getOrder();
        this.approvalFlag = approver.isApprovalFlag();
    }

    public ApproverListDto( Map<String, Object> data) {
        this.vacationId = (Long) data.get("vacationId");
        this.approverDetailCode = (Long) data.get("approverDetailCode");
        this.approverGroupCode = (Long) data.get("approverGroupCode");
        this.orgCode = (String) data.get("orgCode");
        this.orgName = (String) data.get("orgName");
        this.rankCd = (String) data.get("rankCd");
        this.rankNm = (String) data.get("rankNm");
        this.jobName = (String) data.get("jobName");
        this.division = (String) data.get("division");
        this.userId = (String) data.get("userId");
        this.userName = (String) data.get("userName");
        this.order = (int) data.get("order");
        this.approvalFlag = (boolean) data.get("approvalFlag");
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
                .userName(userName)
                .approvalFlag(approvalFlag)
                .build();
    }

}
