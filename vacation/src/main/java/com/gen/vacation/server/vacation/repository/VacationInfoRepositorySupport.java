package com.gen.vacation.server.vacation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gen.vacation.global.domain.entity.QUser.user;
import static com.gen.vacation.global.domain.entity.QVacationInfo.vacationInfo;

@RequiredArgsConstructor
@Repository
public class VacationInfoRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public List<String> findByOrgCodeGroupByYear(String orgCode) {

        List<String> userIds = jpaQueryFactory.select(user.userId)
                .from(user)
                .where(user.organization.orgFullCode.contains(orgCode)).fetch();

        return jpaQueryFactory.select(vacationInfo.id.years)
                .from(vacationInfo)
                .where(vacationInfo.id.userId.in(userIds)).groupBy(vacationInfo.id.years).orderBy(vacationInfo.id.years.desc()).fetch();
    }
}
