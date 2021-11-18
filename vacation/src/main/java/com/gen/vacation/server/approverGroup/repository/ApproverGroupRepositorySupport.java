package com.gen.vacation.server.approverGroup.repository;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.ApproverDetail;
import com.gen.vacation.server.approverGroup.dto.ApproverGroupResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QApproverDetail.approverDetail;
import static com.gen.vacation.global.domain.entity.QOrganization.organization;
import static com.gen.vacation.global.domain.entity.QApproverGroup.approverGroup;


@RequiredArgsConstructor
@Repository
public class ApproverGroupRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public Map<String, Object> findAllBySearch(SearchRequestDto dto) {


        Map<String, Object> result = new HashMap<>();
        List<ApproverGroupResponseDto> list = new ArrayList<>();
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(organization.orgFullCode.contains(dto.getOrgCode()));
        builder.and(approverGroup.useYn.eq(true));

        List<Long> codes = jpaQueryFactory.select(approverGroup.approverGroupCode)
                .from(approverGroup)
                .innerJoin(organization)
                    .on(approverGroup.orgCode.eq(organization.orgCode))
                .where(builder)
                .orderBy(approverGroup.createdAt.desc())
                .limit(dto.getTotal())
                .offset(dto.getOffset())
                .fetch();
        int total = codes.size();
        total += dto.getOffset();

        if (!codes.isEmpty()) {
            list = jpaQueryFactory.select(Projections.fields(ApproverGroupResponseDto.class,
                    approverGroup.approverGroupCode
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(organization.orgName)
                                    .from(organization).where(organization.orgCode.eq(approverGroup.orgCode)), "orgName")
                    , approverGroup.createdAt
                    , approverGroup.modifiedAt
                    , approverGroup.jobName))
                    .from(approverGroup)
                    .where(approverGroup.approverGroupCode.in(codes.stream().limit(dto.getLimit()).collect(Collectors.toList()))).fetch();

            List<ApproverDetail> details = jpaQueryFactory.select(approverDetail).from(approverDetail)
                                                        .where(approverDetail.approverGroupCode
                                                                .in(list.stream()
                                                                        .map(ApproverGroupResponseDto::getApproverGroupCode)
                                                                        .collect(Collectors.toList()))).orderBy(approverDetail.order.asc())
                    .fetch();
            for(ApproverGroupResponseDto responseDto : list) {
                responseDto.setApprover(
                        details.stream()
                                .filter(detail ->
                                        (responseDto.getApproverGroupCode().equals(detail.getApproverGroupCode()) && (detail.getDivision()+"").equals("approver"))
                                )
                                .map(ApproverDetail::getUserName).collect(Collectors.joining(" > ")));
                responseDto.setCc(
                        details.stream()
                                .filter(detail ->
                                        (responseDto.getApproverGroupCode().equals(detail.getApproverGroupCode()) && (detail.getDivision()+"").equals("cc")))
                                .map(ApproverDetail::getUserName).collect(Collectors.joining(" , ")));
            }
        }

        result.put("total",total);
        result.put("approverGroupList",list);

        return result;
    }

}
