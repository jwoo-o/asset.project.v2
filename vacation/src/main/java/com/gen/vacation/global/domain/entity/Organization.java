package com.gen.vacation.global.domain.entity;


import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.server.organization.dto.OrganizationChartUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity(name = "tb_organization")
@Table(name = "tb_organization")
public class Organization extends BaseTimeEntity {

    /**
     * 부서코드
     */
    @Id
    @Column(name = "org_code", length = 200)
    private String orgCode;

    /**
     * 상위 부서코드
     */
    @Column(name = "org_pa_code", length = 200)
    private String orgPaCode;

    /**
     * 부서이름
     */
    @Column(name = "org_name", length = 30)
    private String orgName;

    /**
     * 전체 부서이름
     */
    @Column(name = "org_full_name", length = 300)
    private String orgFullName;

    /**
     * 전체 부서이름
     */
    @Column(name = "org_full_code", length = 2048)
    private String orgFullCode;

    /**
     * 부서순서
     */
    @Column(name = "order")
    private int order;

    /**
     * 좌석 색상
     *
     * */

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "organization")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "mgrOrganization")
    private List<Admin> admins = new ArrayList<>();

    @OneToMany(mappedBy = "organization")
    private List<Asset> assets = new ArrayList<>();


    @Builder
    public Organization(String orgCode, String orgPaCode, String orgName, String orgFullName, int order, String orgFullCode) {
        this.orgCode = orgCode;
        this.orgPaCode = orgPaCode;
        this.orgName = orgName;
        this.orgFullName = orgFullName;
        this.order = order;
        this.orgFullCode = orgFullCode;
    }

    public void update(OrganizationChartUpdateRequestDto dto) {

        this.orgFullCode = dto.getOrgFullCode();
        this.orgFullName = dto.getOrgFullName();
        this.orgName = dto.getOrgName();
        this.color = dto.getColor();
        this.order = dto.getOrder();
        this.orgPaCode = dto.getOrgPaCode();
    }

    public void update(String orgFullName, String orgFullCode, OrganizationChartUpdateRequestDto dto) {

        this.orgFullCode = this.orgFullCode.replace(orgFullCode,dto.getOrgFullCode());
        this.orgFullName = this.orgFullName.replace(orgFullName,dto.getOrgFullName());
        this.order = dto.getOrgFullCode().split(",").length;
        this.color = dto.getColor();
    }
}
