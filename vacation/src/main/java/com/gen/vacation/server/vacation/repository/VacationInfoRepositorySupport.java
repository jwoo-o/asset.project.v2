package com.gen.vacation.server.vacation.repository;

import com.gen.vacation.global.domain.entity.VacationInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QUser.user;
import static com.gen.vacation.global.domain.entity.QVacationInfo.vacationInfo;

@Repository
public class VacationInfoRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public VacationInfoRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(VacationInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<String> findByOrgCodeGroupByYear(String orgCode) {

        List<String> userIds = jpaQueryFactory.select(user.userId)
                .from(user)
                .where(user.organization.orgFullCode.contains(orgCode)).fetch();

        List<String> years = jpaQueryFactory.select(vacationInfo.id.years)
                .from(vacationInfo)
                .where(vacationInfo.id.userId.in(userIds.stream().collect(Collectors.toList()))).groupBy(vacationInfo.id.years).orderBy(vacationInfo.id.years.desc()).fetch();

        return years;
    }
}
