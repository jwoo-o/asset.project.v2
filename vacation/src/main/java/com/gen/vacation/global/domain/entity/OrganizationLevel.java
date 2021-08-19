package com.gen.vacation.global.domain.entity;

import com.gen.vacation.server.organization.dto.OrganizationChartUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Arrays;


@NoArgsConstructor
@Getter
@Entity(name = "tb_organization_level")
@Table(
        name = "tb_organization_level",
        indexes = {
                @Index(name = "idx_tb_organization_level_1", columnList = "org_code_lv1"),
                @Index(name = "idx_tb_organization_level_2", columnList = "org_code_lv2"),
                @Index(name = "idx_tb_organization_level_3", columnList = "org_code_lv3"),
                @Index(name = "idx_tb_organization_level_4", columnList = "org_code_lv4"),
                @Index(name = "idx_tb_organization_level_5", columnList = "org_code_lv5"),
                @Index(name = "idx_tb_organization_level_6", columnList = "org_code_lv6"),
                @Index(name = "idx_tb_organization_level_7", columnList = "org_code_lv7"),
                @Index(name = "idx_tb_organization_level_8", columnList = "org_code_lv8"),
                @Index(name = "idx_tb_organization_level_9", columnList = "org_code_lv9"),
                @Index(name = "idx_tb_organization_level_10", columnList = "org_code_lv10")
        })
public class OrganizationLevel implements Persistable<String> {

    /**
     * 부서코드
     */
    @Id
    @Column(name = "org_code", length = 200)
    private String orgCode;

    @Column(name = "order")
    private int order;

    @Column(name = "org_code_lv1", length = 200)
    private String orgCodeLv1;

    @Column(name = "org_code_lv2", length = 200)
    private String orgCodeLv2;

    @Column(name = "org_code_lv3", length = 200)
    private String orgCodeLv3;

    @Column(name = "org_code_lv4", length = 200)
    private String orgCodeLv4;

    @Column(name = "org_code_lv5", length = 200)
    private String orgCodeLv5;

    @Column(name = "org_code_lv6", length = 200)
    private String orgCodeLv6;

    @Column(name = "org_code_lv7", length = 200)
    private String orgCodeLv7;

    @Column(name = "org_code_lv8", length = 200)
    private String orgCodeLv8;

    @Column(name = "org_code_lv9", length = 200)
    private String orgCodeLv9;

    @Column(name = "org_code_lv10", length = 200)
    private String orgCodeLv10;


    @Builder
    public OrganizationLevel(String orgCode, int order, String orgCodeLv1, String orgCodeLv2, String orgCodeLv3, String orgCodeLv4, String orgCodeLv5, String orgCodeLv6, String orgCodeLv7, String orgCodeLv8, String orgCodeLv9, String orgCodeLv10) {
        this.orgCode = orgCode;
        this.order = order;
        this.orgCodeLv1 = orgCodeLv1;
        this.orgCodeLv2 = orgCodeLv2;
        this.orgCodeLv3 = orgCodeLv3;
        this.orgCodeLv4 = orgCodeLv4;
        this.orgCodeLv5 = orgCodeLv5;
        this.orgCodeLv6 = orgCodeLv6;
        this.orgCodeLv7 = orgCodeLv7;
        this.orgCodeLv8 = orgCodeLv8;
        this.orgCodeLv9 = orgCodeLv9;
        this.orgCodeLv10 = orgCodeLv10;
    }

    public void update(OrganizationChartUpdateRequestDto dto) {


        String[] fullCode = dto.getOrgFullCode().split(",");
        String[] codes = Arrays.copyOf(fullCode,10);

        this.order = dto.getOrder();
        this.orgCodeLv1 = codes[0];
        this.orgCodeLv2 = codes[1];
        this.orgCodeLv3 = codes[2];
        this.orgCodeLv4 = codes[3];
        this.orgCodeLv5 = codes[4];
        this.orgCodeLv6 = codes[5];
        this.orgCodeLv7 = codes[6];
        this.orgCodeLv8 = codes[7];
        this.orgCodeLv9 = codes[8];
        this.orgCodeLv10 = codes[9];

    }

    @Override
    public String getId() {
        return orgCode;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
