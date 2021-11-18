package com.gen.vacation.server.certificate.dto;

import com.gen.vacation.global.enums.ApproverFlagEnum;
import com.gen.vacation.global.enums.CertTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CertificateResponseDto {

    private Long id;
    private String purpose;
    private String submit;
    private String type;
    private String approvalFlag;
    private String approvalDesc;
    private boolean seal;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String orgName;
    private String userName;

    public CertificateResponseDto(Long id, String purpose, String submit, CertTypeEnum type, ApproverFlagEnum approvalFlag, boolean seal, LocalDateTime createdAt, LocalDateTime modifiedAt, String orgName, String userName) {
        this.id = id;
        this.purpose = purpose;
        this.submit = submit;
        this.type = type.getDesc();
        this.approvalFlag = approvalFlag.getKey();
        this.approvalDesc = approvalFlag.getDesc();
        this.seal = seal;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.orgName = orgName;
        this.userName = userName;
    }
}
