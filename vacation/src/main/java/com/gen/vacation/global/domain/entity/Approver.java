package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.Enum.ApprovalEnum;
import com.gen.vacation.global.Enum.ApproverFlagEnum;
import com.gen.vacation.global.domain.common.BaseEntity;
import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.domain.common.BooleanToYNConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Entity(name = "tb_approver")
@Table(name = "tb_approver")
public class Approver extends BaseEntity {

    @Column(name = "vacation_id")
    private Long vacationId;

    @Column(name = "approver_detail_code")
    private Long approverDetailCode;

    @Column(name = "approver_group_code")
    private Long approverGroupCode;

    @Column(name = "org_code")
    private String orgCode;

    @Column(name = "org_nm")
    private String orgName;

    @Column(name = "rank_cd")
    private String rankCd;

    @Column(name = "rank_nm")
    private String rankNm;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "division", length = 20, nullable = false)
    private String division;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "order")
    private int order;

    @Column(name = "email")
    private String email;

    @Column(name = "approval_flag")
    @Enumerated(EnumType.STRING)
    private ApproverFlagEnum approvalFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_id", updatable = false, insertable = false)
    private Vacation vacation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @Builder
    public Approver(Long vacationId, Long approverDetailCode, Long approverGroupCode, String orgCode, String orgName, String rankCd, String rankNm, String jobName, String division, String userId, String userName, int order, ApproverFlagEnum approvalFlag,String email) {
        this.vacationId = vacationId;
        this.approverDetailCode = approverDetailCode;
        this.approverGroupCode = approverGroupCode;
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.rankCd = rankCd;
        this.rankNm = rankNm;
        this.jobName = jobName;
        this.division = division;
        this.userId = userId;
        this.userName = userName;
        this.order = order;
        this.approvalFlag = approvalFlag;
        this.email = email;
    }


    public void update(ApproverFlagEnum approvalFlag) {
        this.approvalFlag = approvalFlag;
    }
}
