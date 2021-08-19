package com.gen.vacation.server.approver.repository;

import com.gen.vacation.global.Enum.ApprovalEnum;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.Approver;
import com.gen.vacation.server.approver.dto.ApproverVacationListResponseDto;
import com.gen.vacation.server.vacation.dto.VacationSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QUser.user;
import static com.gen.vacation.global.domain.entity.QVacation.vacation;
import static com.gen.vacation.global.domain.entity.QApprover.approver;
import static com.gen.vacation.global.domain.entity.QVacationFile.vacationFile;

@Repository
public class ApproverRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public ApproverRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Approver.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
    public Map<String, Object> findAllBySearch(VacationSearchDto dto) {
        Map<String, Object> data = new HashMap<String, Object>();

        int total = 0;
        List<ApproverVacationListResponseDto> list = new ArrayList<>();

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
        if (!StringUtils.isEmpty(dto.getApproveState())){
            builder.and(vacation.approveState.eq(ApprovalEnum.valueOf(dto.getApproveState())));
        }

        builder.and(approver.userId.eq(dto.getUserId()));

        List<Long> ids = jpaQueryFactory.select(vacation.vacationId)
                .from(vacation).innerJoin(approver).on(vacation.vacationId.eq(approver.vacationId))
                .where(builder).orderBy(vacation.vacationId.desc()).limit(dto.getTotal()).offset(dto.getOffset()).fetch();


        total = ids.size();
        total += dto.getOffset();

        if (ids.size() > 0) {
            list = jpaQueryFactory.select(Projections.constructor(ApproverVacationListResponseDto.class
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
            )).from(vacation).innerJoin(approver).on(vacation.vacationId.eq(approver.vacationId))
                    .where(vacation.vacationId.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList())).and(approver.userId.eq(dto.getUserId()))).orderBy(vacation.vacationId.desc()).fetch();

        }

        data.put("approverVacationList", list);
        data.put("total", total);

        return data;
    }

    public Long countByUserId(String userId) {

        Long count = jpaQueryFactory.select(approver.id.count())
                .from(approver)
                .innerJoin(vacation).on(approver.vacationId.eq(vacation.vacationId).and(approver.order.eq(vacation.orderPosition)))
                .where(approver.userId.eq(userId).and(vacation.approveState.in(ApprovalEnum.WAIT, ApprovalEnum.ING))).fetchCount();

        return count;

    }
}
