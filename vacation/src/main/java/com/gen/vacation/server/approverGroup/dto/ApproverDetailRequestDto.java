package com.gen.vacation.server.approverGroup.dto;

import com.gen.vacation.global.enums.DivisionEnum;
import com.gen.vacation.global.domain.entity.ApproverDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-09
 * Time: 오후 5:53
 */
@Getter
@Setter
@ToString
public class ApproverDetailRequestDto {

    private Long approverDetailCode;

    private Long approverGroupCode;

    private String division;

    private int order;

    private String orgCode;

    private String orgName;

    private String rankCd;

    private String rankNm;

    private String jobCode;

    private String jobName;

    private String userId;

    private String userName;

    private String email;

    public ApproverDetail toEntity() {
        return ApproverDetail.builder()
                .approverDetailCode(approverDetailCode)
                .approverGroupCode(approverGroupCode)
                .division(DivisionEnum.valueOf(division))
                .order(order)
                .orgCode(orgCode)
                .orgName(orgName)
                .rankCd(rankCd)
                .rankNm(rankNm)
                .jobCode(jobCode)
                .jobName(jobName)
                .userId(userId)
                .userName(userName)
                .email(email)
                .build();
    }
}
