package com.gen.vacation.server.organization.repository;

import com.gen.vacation.global.domain.entity.OrganizationLevel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gen.vacation.global.domain.entity.QOrganizationLevel.organizationLevel;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 2:48
 */
@RequiredArgsConstructor
@Repository
public class OrganizationLevelRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public List<OrganizationLevel> findAllByOrgCode(String orgCode, int order) throws Exception {
        BooleanBuilder builder = new BooleanBuilder();
        if (order > 1) {
            switch (order) {
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
                default:
                    throw new IllegalArgumentException();
            }

        }

        return jpaQueryFactory.select(organizationLevel)
                .from(organizationLevel).where(builder).orderBy(organizationLevel.order.asc()).fetch();
    }

}
