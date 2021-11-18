package com.gen.vacation.server.certificate.repository;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.server.user.dto.CertificateResponseDto;
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

import static com.gen.vacation.global.domain.entity.QCertificateRequestHistory.certificateRequestHistory;
import static com.gen.vacation.global.domain.entity.QOrganization.organization;
import static com.gen.vacation.global.domain.entity.QUser.user;


@RequiredArgsConstructor
@Repository
public class CertificateRequestHistoryRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;


    public Map<String,Object> findAll(SearchRequestDto dto) {

        Map<String, Object> data = new HashMap<>();
        List<CertificateResponseDto> list = new ArrayList<>();

        List<Long> ids = jpaQueryFactory.select(certificateRequestHistory.id)
                .from(certificateRequestHistory)
                .innerJoin(user)
                    .on(certificateRequestHistory.userId.eq(user.userId))
                .innerJoin(organization)
                    .on(user.orgCode.eq(organization.orgCode))
                .where(organization.orgFullCode.contains(dto.getOrgCode()),
                        certificateRequestHistory.id.loe(999999999999999999L),
                        eqSearch(dto.getSearchType(),dto.getSearchWord()))
                .orderBy(certificateRequestHistory.id.desc())
                .limit(dto.getTotal())
                .offset(dto.getOffset())
                .fetch();

        int total = ids.size();
        total += dto.getOffset();

        if(!ids.isEmpty()) {
            list = jpaQueryFactory.select(Projections.constructor(CertificateResponseDto.class
                    ,certificateRequestHistory.id
                    ,certificateRequestHistory.purpose
                    ,certificateRequestHistory.submit
                    ,certificateRequestHistory.type
                    ,certificateRequestHistory.approvalFlag
                    ,certificateRequestHistory.seal
                    ,certificateRequestHistory.createdAt
                    ,certificateRequestHistory.modifiedAt
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(organization.orgName)
                                    .from(organization).innerJoin(user).on(organization.orgCode.eq(user.orgCode))
                                    .where(user.userId.eq(certificateRequestHistory.userId)), "orgName")
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(user.userName)
                                    .from(user).where(user.userId.eq(certificateRequestHistory.userId)), "userName")
            ))
                    .from(certificateRequestHistory)
                    .where(certificateRequestHistory.id.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList())))
                    .orderBy(certificateRequestHistory.id.desc())
                    .fetch();
        }
        data.put("certificateList",list);
        data.put("total",total);

        return data;

    }

    public Map<String,Object> findById(String userId,SearchRequestDto dto) {

        Map<String, Object> data = new HashMap<>();
        List<CertificateResponseDto> list = new ArrayList<>();

        List<Long> ids = jpaQueryFactory.select(certificateRequestHistory.id)
                    .from(certificateRequestHistory)
                    .where(certificateRequestHistory.userId.eq(userId))
                    .orderBy(certificateRequestHistory.createdAt.desc())
                    .limit(dto.getTotal())
                    .offset(dto.getOffset())
                    .fetch();


        int total = ids.size();
        total += dto.getOffset();

        if(!ids.isEmpty()) {
            list = jpaQueryFactory.select(Projections.constructor(CertificateResponseDto.class
                                        ,certificateRequestHistory.id
                                        ,certificateRequestHistory.purpose
                                        ,certificateRequestHistory.submit
                                        ,certificateRequestHistory.type
                                        ,certificateRequestHistory.approvalFlag
                                        ,certificateRequestHistory.seal
                                        ,certificateRequestHistory.createdAt
                                        ,certificateRequestHistory.modifiedAt
                                        , ExpressionUtils
                                                .as(JPAExpressions
                                                        .select(organization.orgName)
                                                        .from(organization).innerJoin(user).on(organization.orgCode.eq(user.orgCode))
                                                        .where(user.userId.eq(certificateRequestHistory.userId)), "orgName")
                                        , ExpressionUtils
                                                .as(JPAExpressions
                                                        .select(user.userName)
                                                        .from(user).where(user.userId.eq(certificateRequestHistory.userId)), "userName")
            ))
                    .from(certificateRequestHistory)
                    .where(certificateRequestHistory.id.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList())))
                    .orderBy(certificateRequestHistory.id.desc())
                    .fetch();
        }
        data.put("certificateList",list);
        data.put("total",total);

        return data;
    }

    private BooleanExpression eqSearch(String searchType, String searchWord) {

        if(StringUtils.isEmpty(searchType)){
            return null;
        }
        switch (searchType) {

            case "userId":
                return user.userId.eq(searchWord);
            case "name":
                return user.userName.eq(searchWord);
            default:
                return null;
        }
    }
}
