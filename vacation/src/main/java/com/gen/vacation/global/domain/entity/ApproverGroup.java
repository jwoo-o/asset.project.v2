package com.gen.vacation.global.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.domain.common.BooleanToYNConverter;
import com.gen.vacation.server.approverGroup.dto.ApproverGroupRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity(name = "tb_approver_group")
@Table
public class ApproverGroup extends BaseTimeEntity {

    @Id
    @Column(name = "approver_group_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approverGroupCode;

    @Column(name = "org_code")
    private String orgCode;


    @Column(name = "job_code")
    private String jobCode;

    @Column(name = "job_name")
    private String jobName;


    @Column(name = "use_yn", length = 2)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean useYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_code", updatable = false, insertable = false)
    private Organization organization;

    @JsonIgnore
    @OneToMany(mappedBy = "approverGroup",cascade = CascadeType.REMOVE)
    @OrderBy("order asc")
    private List<ApproverDetail> approverDetails = new ArrayList<>();


    @Builder
    public ApproverGroup(String orgCode, String jobCode, String jobName) {
        this.orgCode = orgCode;
        this.jobCode = jobCode;
        this.jobName = jobName;
        this.useYn = true;
    }


    public void update(ApproverGroupRequestDto dto) {

        this.orgCode = dto.getOrgCode();
        this.jobCode = dto.getJobCode();
        this.jobName = dto.getJobName();
        this.useYn = true;
    }

    public void delete(boolean useYn) {

        this.useYn = useYn;
    }
}
