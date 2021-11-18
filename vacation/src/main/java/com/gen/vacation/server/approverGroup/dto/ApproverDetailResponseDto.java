package com.gen.vacation.server.approverGroup.dto;

import com.gen.vacation.global.domain.entity.ApproverDetail;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-10
 * Time: 오전 9:34
 */
@Getter
@Setter
public class ApproverDetailResponseDto {

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


    public ApproverDetailResponseDto(ApproverDetail approverDetail) {
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
    }


}
