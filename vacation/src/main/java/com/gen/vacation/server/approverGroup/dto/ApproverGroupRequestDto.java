package com.gen.vacation.server.approverGroup.dto;

import com.gen.vacation.global.domain.entity.ApproverGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-10
 * Time: 오후 1:06
 */
@Getter
@Setter
@ToString
public class ApproverGroupRequestDto {

    private String orgCode;

    private String orgName;

    private String jobCode;

    private String jobName;

    private List<ApproverDetailRequestDto> detailCodes;

    public ApproverGroup toEntity() {
        return ApproverGroup.builder()
                .orgCode(orgCode)
                .jobCode(jobCode)
                .jobName(jobName)
                .build();

    }
}
