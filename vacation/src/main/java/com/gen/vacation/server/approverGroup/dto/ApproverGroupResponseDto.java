package com.gen.vacation.server.approverGroup.dto;

import com.gen.vacation.global.domain.entity.ApproverGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 6:49
 */
@Getter
@Setter
@NoArgsConstructor
public class ApproverGroupResponseDto {

    private Long approverGroupCode;

    private String orgName;

    private String orgCode;

    private String jobName;
    private String jobCode;

    private String approver;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private List<ApproverDetailResponseDto> detailCodes;

    private String cc;

    private boolean useYn;

    private String orgPaCode;

    public ApproverGroupResponseDto(ApproverGroup group) {
        this.approverGroupCode = group.getApproverGroupCode();
        this.orgName = group.getOrganization().getOrgName();
        this.orgCode = group.getOrgCode();
        this.jobName = group.getJobName();
        this.jobCode = group.getJobCode();
        this.createdAt = group.getCreatedAt();
        this.modifiedAt = group.getModifiedAt();
        this.detailCodes = group.getApproverDetails().stream().map(ApproverDetailResponseDto::new).collect(Collectors.toList());
        this.useYn = group.isUseYn();
    }



}
