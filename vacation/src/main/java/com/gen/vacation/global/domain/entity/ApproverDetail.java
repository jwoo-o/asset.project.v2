package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.enums.DivisionEnum;
import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.server.approverGroup.dto.ApproverDetailRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity(name = "tb_approver_detail")
@Table
@ToString
public class ApproverDetail extends BaseTimeEntity{

    @Id
    @Column(name = "approver_detail_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "job_code")
    private String jobCode;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "division", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private DivisionEnum division;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_group_code", updatable = false, insertable = false)
    private ApproverGroup approverGroup;


    @Builder
    public ApproverDetail(Long approverDetailCode, Long approverGroupCode, String orgCode, String orgName, String rankCd, String rankNm, String jobCode, String jobName, DivisionEnum division, String userId, String userName, int order,String email) {
        this.approverDetailCode = approverDetailCode;
        this.approverGroupCode = approverGroupCode;
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.rankCd = rankCd;
        this.rankNm = rankNm;
        this.jobCode = jobCode;
        this.jobName = jobName;
        this.division = division;
        this.userId = userId;
        this.userName = userName;
        this.order = order;
        this.email = email;
    }

    public void update(ApproverDetailRequestDto requestDto) {

        this.approverGroupCode = requestDto.getApproverGroupCode();
        this.orgCode = requestDto.getOrgCode();
        this.orgName = requestDto.getOrgName();
        this.rankCd = requestDto.getRankCd();
        this.rankNm = requestDto.getRankNm();
        this.division = DivisionEnum.valueOf(requestDto.getDivision());
        this.userId = requestDto.getUserId();
        this.userName = requestDto.getUserName();
        this.jobCode = requestDto.getJobCode();
        this.jobName = requestDto.getJobName();
        this.order = requestDto.getOrder();
        this.email = requestDto.getEmail();
    }


}
