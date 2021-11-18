package com.gen.vacation.server.organization.dto;

import com.gen.vacation.global.domain.entity.Organization;
import com.gen.vacation.global.domain.entity.OrganizationLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-12
 * Time: 오전 9:25
 */
@Getter
@Setter
public class OrganizationChartRequestDto {


    /**
     * 등록 부서 코드
     */
    @NotBlank(message = "")
    private String orgCode;
    /**
     * 등록 부서 이름
     */
    @NotBlank(message = "")
    private String orgName;
    /**
     * 부서 전체 이름
     */
    @NotBlank(message = "")
    private String orgFullName;
    /**
     * 부서 전체 코드
     */
    @NotBlank(message = "")
    private String orgFullCode;
    /**
     * 상위 부서 코드
     */
    @NotBlank(message = "")
    private String orgPaCode;
    /**
     * 등록 부서 레벨
     */
    @NotNull
    private int order;


    public Organization toEntity() {
        return Organization.builder()
                .orgCode(orgCode)
                .orgName(orgName)
                .orgPaCode(orgPaCode)
                .orgFullName(orgFullName)
                .orgFullCode(orgFullCode)
                .order(order).build();
    }

    public OrganizationLevel levelToEntity() {
        String[] fullCode = orgFullCode.split(",");
        String[] codes = Arrays.copyOf(fullCode,10);
        return OrganizationLevel.builder()
                .orgCode(orgCode)
                .order(order)
                .orgCodeLv1(codes[0])
                .orgCodeLv2(codes[1])
                .orgCodeLv3(codes[2])
                .orgCodeLv4(codes[3])
                .orgCodeLv5(codes[4])
                .orgCodeLv6(codes[5])
                .orgCodeLv7(codes[6])
                .orgCodeLv8(codes[7])
                .orgCodeLv9(codes[8])
                .orgCodeLv10(codes[9]).build();

    }


}
