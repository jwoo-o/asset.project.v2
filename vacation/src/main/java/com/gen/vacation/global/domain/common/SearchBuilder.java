package com.gen.vacation.global.domain.common;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;

import static com.gen.vacation.global.domain.entity.QOrganization.organization;
import static com.gen.vacation.global.domain.entity.QOrganizationLevel.organizationLevel;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-06
 * Time: 오후 12:54
 */
public class SearchBuilder {


    public static BooleanBuilder orgSearch(int order, String orgCode) {

        BooleanBuilder builder = new BooleanBuilder();
        BooleanBuilder builder1 = orgLevelSearch(order,orgCode);
        if (order > 1) {
            builder.and(organization.orgCode.in(
                    JPAExpressions.select(organizationLevel.orgCode)
                            .from(organizationLevel).where(builder1)));
        }
        return builder;
    }
    public static BooleanBuilder orgLevelSearch(int order, String orgCode) {

        BooleanBuilder builder = new BooleanBuilder();
        if (order > 1) {
            switch (order) {
                case 1:
                    builder.and(organizationLevel.orgCodeLv1.eq(orgCode));
                    break;
                case 2:
                    builder.and(organizationLevel.orgCodeLv2.eq(orgCode));
                    break;
                case 3:
                    builder.and(organizationLevel.orgCodeLv3.eq(orgCode));
                    break;
                case 4:
                    builder.and(organizationLevel.orgCodeLv4.eq(orgCode));
                    break;
                case 5:
                    builder.and(organizationLevel.orgCodeLv5.eq(orgCode));
                    break;
                case 6:
                    builder.and(organizationLevel.orgCodeLv6.eq(orgCode));
                    break;
                case 7:
                    builder.and(organizationLevel.orgCodeLv7.eq(orgCode));
                    break;
                case 8:
                    builder.and(organizationLevel.orgCodeLv8.eq(orgCode));
                    break;
                case 9:
                    builder.and(organizationLevel.orgCodeLv9.eq(orgCode));
                    break;
                case 10:
                    builder.and(organizationLevel.orgCodeLv10.eq(orgCode));
                    break;
            }

        }
        return builder;
    }

}
