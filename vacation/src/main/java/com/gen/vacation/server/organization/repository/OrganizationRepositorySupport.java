package com.gen.vacation.server.organization.repository;

import com.gen.vacation.global.domain.common.SearchBuilder;
import com.gen.vacation.global.domain.entity.Organization;
import com.gen.vacation.server.organization.dto.OrganizationChartDto;
import com.gen.vacation.server.organization.dto.OrganizationChartLineDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.gen.vacation.global.domain.entity.QOrganization.organization;
import static com.gen.vacation.global.domain.entity.QOrganizationLevel.organizationLevel;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 2:48
 */
@Repository
public class OrganizationRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    private EntityManager em;

    public OrganizationRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Organization.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<OrganizationChartDto> findAllByOrgCode(String orgCode) throws Exception {

       /* BooleanBuilder builder = SearchBuilder.orgSearch(order, orgCode);*/
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

        BooleanBuilder builder = SearchBuilder.orgLevelSearch(order, orgCode);

        return jpaQueryFactory.select(organizationLevel.orgCode)
                .from(organizationLevel)
                .where(builder).fetch();


    }

    public List<OrganizationChartDto> findAllByOrder(int order) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(organization.order.loe(order));

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
                .where(builder).orderBy(organization.order.asc()).fetch();
    }
}
