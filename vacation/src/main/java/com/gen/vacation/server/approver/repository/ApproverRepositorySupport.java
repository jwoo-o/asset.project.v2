package com.gen.vacation.server.approver.repository;

import com.gen.vacation.global.enums.ApprovalEnum;
import com.gen.vacation.server.approver.dto.ApproverVacationListResponseDto;
import com.gen.vacation.server.vacation.dto.VacationSearchDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QVacation.vacation;
import static com.gen.vacation.global.domain.entity.QApprover.approver;
import static com.gen.vacation.global.domain.entity.QVacationFile.vacationFile;

@RequiredArgsConstructor
@Repository
public class ApproverRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public Map<String, Object> findAllBySearch(VacationSearchDto dto) {

        Map<String, Object> data = new HashMap<>();
        List<ApproverVacationListResponseDto> list = new ArrayList<>();

        List<Long> ids = jpaQueryFactory.select(vacation.vacationId)
                .from(vacation)
                .innerJoin(approver)
                    .on(vacation.vacationId.eq(approver.vacationId))
                .where(
                        eqUserId(dto.getUserId()),
                        eqSearch(dto.getSearchType(),dto.getSearchWord()),
                        eqApproveState(dto.getApproveState())
                )
                .orderBy(vacation.vacationId.desc())
                .limit(dto.getTotal())
                .offset(dto.getOffset())
                .fetch();


        int total = ids.size();
        total += dto.getOffset();

        if (!ids.isEmpty()) {
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
                                    .select(vacationFile.id.count().gt(0)).from(vacationFile).where(vacationFile.vacationId.eq(vacation.vacationId)).limit(1), "isAttach")
            ))
                    .from(vacation)
                    .innerJoin(approver)
                        .on(vacation.vacationId.eq(approver.vacationId))
                    .where(vacation.vacationId.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList()))).orderBy(vacation.vacationId.desc()).fetch();
        }

        data.put("approverVacationList", list);
        data.put("total", total);

        return data;
    }
    public Long countByUserId(String userId) {
        return jpaQueryFactory.select(approver.id.count())
                .from(approver)
                .innerJoin(vacation).on(approver.vacationId.eq(vacation.vacationId).and(approver.order.eq(vacation.orderPosition)))
                .where(approver.userId.eq(userId).and(vacation.approveState.in(ApprovalEnum.WAIT, ApprovalEnum.ING))).fetchCount();

    }

    private BooleanExpression eqSearch(String searchType, String searchWord) {

        if(StringUtils.isEmpty(searchType)){
            return null;
        }
        switch (searchType) {

            case "userId":
                return vacation.userId.eq(searchWord);
            case "name":
                return vacation.user.userName.eq(searchWord);
            default:
                return null;
        }
    }
    private BooleanExpression eqApproveState(String approveState) {
        if(StringUtils.isEmpty(approveState)) {
            return null;
        }
        return vacation.approveState.eq(ApprovalEnum.valueOf(approveState));
    }
    private BooleanExpression eqUserId(String userId) {
        if(StringUtils.isEmpty(userId)) {
            return null;
        }
        return approver.userId.eq(userId);
    }

}
