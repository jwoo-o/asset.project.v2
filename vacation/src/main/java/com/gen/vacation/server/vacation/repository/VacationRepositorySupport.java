package com.gen.vacation.server.vacation.repository;

import com.gen.vacation.global.Enum.ApprovalEnum;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.QVacation;
import com.gen.vacation.global.domain.entity.Vacation;
import com.gen.vacation.server.vacation.dto.CalendarResponseDto;
import com.gen.vacation.server.vacation.dto.VacationListRequestDto;
import com.gen.vacation.server.vacation.dto.VacationListResponseDto;
import com.gen.vacation.server.vacation.dto.VacationSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static com.gen.vacation.global.domain.entity.QVacation.vacation;
import static com.gen.vacation.global.domain.entity.QOrganization.organization;
import static com.gen.vacation.global.domain.entity.QUser.user;
import static com.gen.vacation.global.domain.entity.QVacationFile.vacationFile;

@Repository
public class VacationRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public VacationRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Vacation.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Map<String, Object> findAllBySearch(VacationSearchDto dto) throws Exception {


        Map<String, Object> data = new HashMap<>();

        int total = 0;
        List<VacationListResponseDto> list = new ArrayList<>();

        BooleanBuilder builder = new BooleanBuilder();
        if (!StringUtils.isEmpty(dto.getSearchType())) {
            switch (dto.getSearchType()) {

                case "userId":
                    builder.and(vacation.userId.eq(dto.getSearchWord()));
                    break;
                case "name":
                    builder.and(vacation.user.userName.eq(dto.getSearchWord()));
                    break;
            }
        }
        if (!StringUtils.isEmpty(dto.getYear())) {
            builder.and(vacation.createdAt.goe(LocalDateTime.of(Integer.parseInt(dto.getYear()),1,1,0,0,0)));
            builder.and(vacation.createdAt.loe(LocalDateTime.of(Integer.parseInt(dto.getYear()),12,31,23,59,59)));
        }
        if (!StringUtils.isEmpty(dto.getApproveState())) {
            builder.and(vacation.approveState.eq(ApprovalEnum.valueOf(dto.getApproveState())));
        }

        List<Long> ids = jpaQueryFactory.select(vacation.vacationId)
                .from(vacation).innerJoin(user).on(vacation.userId.eq(user.userId))
                .where(builder).orderBy(vacation.createdAt.desc()).limit(dto.getTotal()).offset(dto.getOffset()).fetch();

        total = ids.size();
        total += dto.getOffset();

        if (!ids.isEmpty()) {
            list = jpaQueryFactory.select(Projections.constructor(VacationListResponseDto.class
                    , vacation.vacationId
                    , vacation.approveState
                    , vacation.createdAt
                    , vacation.modifiedAt
                    , vacation.endDay
                    , vacation.startDay
                    , vacation.vacationKind
                    , vacation.vacationType
                    , vacation.vacationReason
                    , vacation.vacationTel
                    , vacation.takeOver
                    , vacation.userId
                    , vacation.userName
                    , vacation.orgCode
                    , vacation.countDay
                    , vacation.orderPosition
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(vacationFile.id.count()).from(vacationFile).where(vacationFile.vacationId.eq(vacation.vacationId)), "isAttach")
                    ,vacation.rejectReason
            )).from(vacation).innerJoin(user).on(vacation.userId.eq(user.userId))
                    .where(vacation.vacationId.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList()))).orderBy(vacation.vacationId.desc()).fetch();

        }
        data.put("vacationList", list);
        data.put("total", total);


        return data;
    }

    public List<VacationListResponseDto> findByUserId(String userId) {
        List<VacationListResponseDto> list = new ArrayList<>();

        Map<String, Object> data = new HashMap<>();

        list = jpaQueryFactory.select(Projections.constructor(VacationListResponseDto.class
                , vacation.vacationId
                , vacation.approveState
                , vacation.createdAt
                , vacation.modifiedAt
                , vacation.endDay
                , vacation.startDay
                , vacation.vacationKind
                , vacation.vacationType
                , vacation.vacationReason
                , vacation.vacationTel
                , vacation.takeOver
                , vacation.userId
                , vacation.userName
                , vacation.orgCode
                , vacation.countDay
                , vacation.orderPosition
                , vacation.rejectReason
        )).from(vacation).where(vacation.userId.eq(userId)).orderBy(vacation.modifiedAt.desc()).limit(1).fetch();

        return list;
    }

    public List<CalendarResponseDto> findCalendar(VacationSearchDto dto) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(organization.orgFullCode.contains(dto.getOrgCode()));
        builder.and(vacation.startDay.goe(LocalDate.parse(dto.getMonth()).minusMonths(1L)).and(vacation.endDay.loe(LocalDate.parse(dto.getMonth()).plusMonths(2L))));
        builder.and(vacation.approveState.in(ApprovalEnum.WAIT, ApprovalEnum.ING, ApprovalEnum.APPROVE, ApprovalEnum.CANCEL));


        return jpaQueryFactory.select(Projections.constructor(CalendarResponseDto.class,
                vacation.vacationId.as("id")
                ,vacation.vacationKind
                ,vacation.userName
                ,vacation.startDay.as("start")
                ,vacation.endDay.as("end")
                ,vacation.userId))
                .from(vacation).innerJoin(user).on(vacation.userId.eq(user.userId))
                .innerJoin(organization).on(user.orgCode.eq(organization.orgCode))
                .where(builder).fetch();
    }

    public int countByUserIdAndStartDayAndEndDay(String userId, LocalDate startDay, LocalDate endDay, String vacationId) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(vacation.userId.eq(userId));
        if (!vacationId.equals("undefined")){
            Long longVacationId = Long.parseLong(vacationId);
            builder.and(vacation.vacationId.ne(longVacationId));
        }
        builder.and(vacation.startDay.loe(endDay).and(vacation.endDay.goe(startDay)));
        builder.and(vacation.approveState.ne(ApprovalEnum.REJECT));
        builder.and(vacation.approveState.ne(ApprovalEnum.CANCELSUCCESS));

        List<Long> vacationDateCount = jpaQueryFactory.select(vacation.vacationId)
                .from(vacation).where(builder).fetch();


        return vacationDateCount.size();

    }
}
