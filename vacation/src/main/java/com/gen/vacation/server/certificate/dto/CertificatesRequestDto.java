package com.gen.vacation.server.certificate.dto;

import com.gen.vacation.global.enums.ApproverFlagEnum;
import com.gen.vacation.global.enums.CertTypeEnum;
import com.gen.vacation.global.domain.entity.CertificateRequestHistory;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class CertificatesRequestDto {

    @NotBlank(message = "")
    private List<String> userId;
    @NotBlank(message = "")
    private String purpose;
    @NotBlank(message = "")
    private String submit;
    @NotBlank(message = "")
    private String type;
    private boolean seal;
    private String adminId;

    public CertificateRequestHistory toEntity(String userId) {

        return CertificateRequestHistory.builder()
                .userId(userId)
                .approvalFlag(ApproverFlagEnum.SUCCESS)
                .purpose(purpose)
                .seal(seal)
                .submit(submit)
                .adminId(adminId)
                .type(CertTypeEnum.valueOf(type)).build();
    }

}
