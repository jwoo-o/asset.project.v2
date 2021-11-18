package com.gen.vacation.server.user.dto;

import com.gen.vacation.global.enums.ApproverFlagEnum;
import com.gen.vacation.global.enums.CertTypeEnum;
import com.gen.vacation.global.domain.entity.CertificateRequestHistory;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CertificateRequestDto {

    @NotBlank(message = "")
    private String userId;
    @NotBlank(message = "")
    private String purpose;
    @NotBlank(message = "")
    private String submit;
    @NotBlank(message = "")
    private String type;
    private boolean seal;


    public CertificateRequestHistory toEntity() {

        return CertificateRequestHistory.builder()
                                    .userId(userId)
                                    .approvalFlag(ApproverFlagEnum.WAIT)
                                    .purpose(purpose)
                                    .seal(seal)
                                    .submit(submit)
                                    .type(CertTypeEnum.valueOf(type)).build();
    }
}
