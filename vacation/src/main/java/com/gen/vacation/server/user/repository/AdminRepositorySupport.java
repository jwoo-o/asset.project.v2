package com.gen.vacation.server.user.repository;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.Admin;
import com.gen.vacation.global.domain.entity.User;
import com.gen.vacation.server.user.dto.SeatResponseDto;
import com.gen.vacation.server.user.dto.UserListResponseDto;
import com.gen.vacation.server.user.dto.VacationInfoListResponseDto;
import com.gen.vacation.server.vacation.dto.VacationSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QAdmin.admin;


@Repository
public class AdminRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public AdminRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Admin.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<String> findEmailById(String adminId) {

        return jpaQueryFactory.select(admin.email).from(admin)
                .where(admin.adminId.ne(adminId).and(admin.useYn.eq(true))).fetch();
    }
}
