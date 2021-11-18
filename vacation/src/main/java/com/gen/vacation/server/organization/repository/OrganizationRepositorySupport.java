package com.gen.vacation.server.organization.repository;

import com.gen.vacation.global.domain.common.SearchBuilder;
import com.gen.vacation.server.organization.dto.OrganizationChartDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gen.vacation.global.domain.entity.QOrganization.organization;
import static com.gen.vacation.global.domain.entity.QOrganizationLevel.organizationLevel;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 2:48
 */
@RequiredArgsConstructor
@Repository
public class OrganizationRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public List<OrganizationChartDto> findAllByOrgCode(String orgCode) throws Exception {

        BooleanBuilder builder = new BooleanBuilder();
        if (!orgCode.equals("00000000")) {
            builder.and(organization.orgFullCode.contains(orgCode));
        }
        return jpaQueryFactory.select(Projections.constructor(OrganizationChartDto.class
                , organization.orgCode
                , organization.orgPaCode
                , organization.orgName
                , organization.orgFullName
                , organization.orgFullCode
                , organization.order
                , organization.color
                ))
                .from(organization)
                .where(builder).fetch();
    }


    public List<String> findOrgCodeAllByOrgCode(String orgCode, int order) throws Exception {

        return jpaQueryFactory.select(organizationLevel.orgCode)
                .from(organizationLevel)
                .where(SearchBuilder.orgLevelSearch(order, orgCode)).fetch();


    }

    public List<OrganizationChartDto> findAllByOrder(int order) {

        return jpaQueryFactory.select(Projections.constructor(OrganizationChartDto.class
                , organization.orgCode
                , organization.orgPaCode
                , organization.orgName
                , organization.orgFullName
                , organization.orgFullCode
                , organization.order
                , organization.color
        ))
                .from(organization)
                .where(organization.order.loe(order)).orderBy(organization.order.asc()).fetch();
    }
}
