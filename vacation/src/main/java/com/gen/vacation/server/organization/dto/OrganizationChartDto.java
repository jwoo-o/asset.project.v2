package com.gen.vacation.server.organization.dto;

import com.gen.vacation.global.domain.entity.Organization;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-04-16
 * Time: 오후 8:44
 */
@Getter
@Setter
public class OrganizationChartDto {

    /**
     * 부서코드
     */
    private String orgCode;

    /**
     * 상위 부서코드
     */
    private String orgPaCode;

    /**
     * 부서이름
     */
    private String orgName;

    /**
     * 전체 부서이름
     */
    private String orgFullName;

    /**
     * 전체 부서코드
     */
    private String orgFullCode;

    /**
     * 부서순서
     */
    private int order;
    /**
     * 배치도 부서 색상 코드
     */
    private String color;
    public OrganizationChartDto(String orgCode, String orgPaCode, String orgName, String orgFullName, String orgFullCode, int order,String color) {
        this.orgCode = orgCode;
        this.orgPaCode = orgPaCode;
        this.orgName = orgName;
        this.orgFullName = orgFullName;
        this.orgFullCode = orgFullCode;
        this.order = order;
        this.color = color;
    }
    public OrganizationChartDto(Organization organization) {
        this.orgCode = organization.getOrgCode();
        this.orgPaCode = organization.getOrgPaCode();
        this.orgName = organization.getOrgName();
        this.orgFullName = organization.getOrgFullName();
        this.orgFullCode = organization.getOrgFullCode();
        this.order = organization.getOrder();
        this.color = organization.getColor();
    }
}
