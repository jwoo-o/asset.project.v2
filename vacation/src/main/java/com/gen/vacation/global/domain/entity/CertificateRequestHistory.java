package com.gen.vacation.global.domain.entity;


import com.gen.vacation.global.enums.ApproverFlagEnum;
import com.gen.vacation.global.enums.CertTypeEnum;
import com.gen.vacation.global.domain.common.BaseEntity;
import com.gen.vacation.global.domain.common.BooleanToYNConverter;
import com.gen.vacation.server.user.dto.CertificateStateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity(name = "tb_certificate_request_history")
@Table(name = "tb_certificate_request_history",indexes =
        {@Index(name = "i_tb_certificate_request_history_user_id_created_at", columnList = "user_id,created_at")})
@ToString
public class CertificateRequestHistory extends BaseEntity {

    @Column(length = 20, name = "user_id")
    private String userId;

    @Column(length = 20, name = "admin_id")
    private String adminId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", updatable = false, insertable = false)
    private Admin admin;

    private String purpose;

    private String submit;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "seal",length = 2,nullable = false)
    private boolean seal;

    @Column(name = "type",length = 30)
    @Enumerated(EnumType.STRING)
    private CertTypeEnum type;

    @Column(name = "approval_flag")
    @Enumerated(EnumType.STRING)
    private ApproverFlagEnum approvalFlag;

    @Builder
    public CertificateRequestHistory(String userId, String adminId, String purpose, String submit, CertTypeEnum type, ApproverFlagEnum approvalFlag, boolean seal) {
        this.userId = userId;
        this.adminId = adminId;
        this.purpose = purpose;
        this.submit = submit;
        this.seal = seal;
        this.type = type;
        this.approvalFlag = approvalFlag;
    }

    public void success() {

        this.approvalFlag = ApproverFlagEnum.SUCCESS;
    }

    public void approve(String adminId) {

        this.adminId = adminId;
        this.approvalFlag = ApproverFlagEnum.SUCCESS;
    }
    public void state(CertificateStateRequestDto dto) {

        this.adminId = dto.getAdminId();
        this.approvalFlag = ApproverFlagEnum.valueOf(dto.getApprovalFlag());
    }
}
