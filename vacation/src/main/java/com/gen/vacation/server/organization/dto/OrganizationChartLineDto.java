package com.gen.vacation.server.organization.dto;

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
public class OrganizationChartLineDto {

    /**
     * 부서코드
     */
    private String orgCode;


    /**
     * 부서이름
     */
    private String orgName;

    public OrganizationChartLineDto(String orgCode, String orgName) {
        this.orgCode = orgCode;
        this.orgName = orgName;

    }
}
