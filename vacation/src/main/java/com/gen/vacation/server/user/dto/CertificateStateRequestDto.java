package com.gen.vacation.server.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CertificateStateRequestDto {

    private String adminId;
    private String approvalFlag;
}
