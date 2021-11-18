package com.gen.vacation.server.user.repository;

import com.gen.vacation.global.enums.LoginHistoryEnum;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.util.QueryDslUtil;
import com.gen.vacation.server.user.dto.SeatResponseDto;
import com.gen.vacation.server.user.dto.UserListResponseDto;
import com.gen.vacation.server.user.dto.VacationInfoListResponseDto;
import com.gen.vacation.server.vacation.dto.VacationSearchDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QOrganization.organization;
import static com.gen.vacation.global.domain.entity.QSeat.seat;
import static com.gen.vacation.global.domain.entity.QUser.user;
import static com.gen.vacation.global.domain.entity.QVacationDeadline.vacationDeadline;
import static com.gen.vacation.global.domain.entity.QVacationInfo.vacationInfo;
import static com.gen.vacation.global.domain.entity.QVacation.vacation;
import static com.gen.vacation.global.domain.entity.QApprover.approver;

@RequiredArgsConstructor
@Repository
public class UserRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public Map<String, Object> findAllBySearch(String orgCode) throws Exception {

        Map<String, Object> data = new HashMap<>();

        List<UserListResponseDto> list = jpaQueryFactory.select(Projections.constructor(UserListResponseDto.class
                , user.userId
                , user.userName
                , user.email
                , user.orgCode
                , ExpressionUtils
                        .as(JPAExpressions
                                .select(organization.orgName)
                                .from(organization).where(organization.orgCode.eq(user.orgCode)), "orgName")
                , user.rankCd
                , user.rankNm
                , user.jobCd
                , user.jobNm
                , user.tel
                , user.hireDate
                , user.useYn
                , user.createdAt
                , user.modifiedAt
            )).from(user)
                .innerJoin(organization)
                    .on(user.orgCode.eq(organization.orgCode))
                    .where(
                            user.useYn.eq(true),
                            user.createdAt.loe(LocalDateTime.now()),
                            organization.orgFullCode.contains(orgCode)
                    )
                    .orderBy(user.createdAt.desc()).fetch();

        data.put("userList", list);

        return data;
    }


    public Map<String, Object> findByUserId(SearchRequestDto dto) {
        Map<String, Object> data = new HashMap<>();
        UserListResponseDto userResponseDto = jpaQueryFactory.select(Projections.fields(UserListResponseDto.class
                , user.userId
                , user.userName
                , user.email
                , user.orgCode
                , ExpressionUtils
                        .as(JPAExpressions
                                .select(organization.orgName)
                                .from(organization).where(organization.orgCode.eq(user.orgCode)), "orgName")
                , user.rankCd
                , user.rankNm
                , user.tel
                , user.useYn
                , user.salt
                , user.jobCd
        )).from(user).where(user.userId.eq(dto.getUserId())).fetchFirst();

        data.put("userList", userResponseDto);
        return data;
    }

    public Map<String, Object> findBySearch(SearchRequestDto dto) {
        Map<String, Object> data = new HashMap<>();

        List<UserListResponseDto> list = new ArrayList<>();
        OrderSpecifier[] orders = getOrders(dto.getSort(),dto.getSeq()).toArray(new OrderSpecifier[0]);

        List<String> ids = jpaQueryFactory.select(user.userId)
                .from(user).join(organization).on(user.orgCode.eq(organization.orgCode))
                .where(
                        user.createdAt.loe(LocalDateTime.now()),
                        eqUse(dto.getSearchType()),
                        eqSearch(dto.getSearchType(),dto.getSearchWord()),
                        ctOrgFullCode(dto.getOrgCode())
                        )
                .limit(dto.getTotal()).offset(dto.getOffset())
                .orderBy(orders)
                .fetch();

        int total = ids.size();
        total += dto.getOffset();

        if (!ids.isEmpty()) {
            list = jpaQueryFactory.select(Projections.constructor(UserListResponseDto.class
                    , user.userId
                    , user.userName
                    , user.email
                    , user.orgCode
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(organization.orgName)
                                    .from(organization).where(organization.orgCode.eq(user.orgCode)), "orgName")
                    , user.rankCd
                    , user.rankNm
                    , user.jobCd
                    , user.jobNm
                    , user.tel
                    , user.hireDate
                    , user.leaveDate
                    , user.useYn
                    , user.createdAt
                    , user.modifiedAt
            ))
                    .from(user)
                    .where(user.userId.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList())))
                    .orderBy(orders)
                    .fetch();
        }

        data.put("userList", list);
        data.put("total", total);

        return data;
    }

    public Map<String, Object> findVacationInfos(VacationSearchDto dto) {
        Map<String, Object> data = new HashMap<>();

        List<VacationInfoListResponseDto> list = new ArrayList<>();
        OrderSpecifier[] orders = getOrders(dto.getSort(),dto.getSeq()).toArray(new OrderSpecifier[0]);

        List<String> ids = jpaQueryFactory.select(user.userId)
                .from(user).innerJoin(vacationInfo)
                    .on(user.userId.eq(vacationInfo.id.userId))
                .innerJoin(organization)
                    .on(user.orgCode.eq(organization.orgCode))
                .where(
                        user.createdAt.loe(LocalDateTime.now()),
                        eqUse(dto.getSearchType()),
                        eqSearch(dto.getSearchType(),dto.getSearchWord()),
                        ctOrgFullCode(dto.getOrgCode()),
                        eqYear(dto.getYear())
                )
                .limit(dto.getTotal())
                .offset(dto.getOffset())
                .orderBy(orders)
                .fetch();
        int total = ids.size();
        total += dto.getOffset();

        if (!ids.isEmpty()) {
            list = jpaQueryFactory.select(Projections.constructor(VacationInfoListResponseDto.class
                    , user.userId
                    , user.userName
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(organization.orgName)
                                    .from(organization).where(organization.orgCode.eq(user.orgCode)), "orgName")
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(vacationDeadline.confirm)
                                    .from(vacationDeadline)
                                    .where(
                                            vacationDeadline.id.userId.eq(user.userId)
                                            .and(vacationDeadline.id.years.eq(dto.getYear())
                                            .and(vacationDeadline.id.writeType.eq(LoginHistoryEnum.USER)))
                                    ), "userDeadline")
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(new CaseBuilder().when(vacationDeadline.createdAt.isNotNull()).then(true).otherwise(false))
                                    .from(vacationDeadline)
                                    .where(
                                            vacationDeadline.id.userId.eq(user.userId)
                                                    .and(vacationDeadline.id.years.eq(dto.getYear())
                                                            .and(vacationDeadline.id.writeType.eq(LoginHistoryEnum.USER)))
                                    ), "userSender")
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(vacationDeadline.confirm)
                                    .from(vacationDeadline)
                                    .where(
                                            vacationDeadline.id.userId.eq(user.userId)
                                                    .and(vacationDeadline.id.years.eq(dto.getYear())
                                                            .and(vacationDeadline.id.writeType.eq(LoginHistoryEnum.ADMIN)))
                                    ), "adminDeadline")
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(new CaseBuilder().when(vacationDeadline.createdAt.isNotNull()).then(true).otherwise(false))
                                    .from(vacationDeadline)
                                    .where(
                                            vacationDeadline.id.userId.eq(user.userId)
                                                    .and(vacationDeadline.id.years.eq(dto.getYear())
                                                            .and(vacationDeadline.id.writeType.eq(LoginHistoryEnum.ADMIN)))
                                    ), "adminSender")
                    , user.rankNm
                    , user.jobNm
                    , user.hireDate
                    , user.leaveDate
                    , user.useYn
                    , vacationInfo.totalCnt.as("vacationTotalCnt")
                    , vacationInfo.useCnt.as("vacationUseCnt")
            ))
                    .from(user)
                    .innerJoin(vacationInfo)
                        .on(user.userId.eq(vacationInfo.id.userId).and(vacationInfo.id.years.eq(dto.getYear())))
                    .where(user.userId.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList())))
                    .fetch();
        }
        data.put("userList", list);
        data.put("total", total);

        return data;
    }
    public VacationInfoListResponseDto findVacationInfo(String userId,String year) {

        return jpaQueryFactory.select(Projections.constructor(VacationInfoListResponseDto.class
                    , user.userId
                    , user.userName
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(organization.orgName)
                                    .from(organization).where(organization.orgCode.eq(user.orgCode)), "orgName")
                    , user.rankNm
                    , user.jobNm
                    , user.hireDate
                    , user.email
                    , user.useYn
                    , vacationInfo.totalCnt.as("vacationTotalCnt")
                    , vacationInfo.useCnt.as("vacationUseCnt")
            ))
                .from(user)
                .innerJoin(vacationInfo)
                    .on(user.userId.eq(vacationInfo.id.userId).and(vacationInfo.id.years.eq(year)))
                .where(user.userId.eq(userId)).fetchOne();
    }

    public List<VacationInfoListResponseDto> findVacationInfos(String year) {

        return jpaQueryFactory.select(Projections.constructor(VacationInfoListResponseDto.class
                , user.userId
                , user.userName
                , ExpressionUtils
                        .as(JPAExpressions
                                .select(organization.orgName)
                                .from(organization).where(organization.orgCode.eq(user.orgCode)), "orgName")
                , user.rankNm
                , user.jobNm
                , user.hireDate
                , user.email
                , user.useYn
                , vacationInfo.totalCnt.as("vacationTotalCnt")
                , vacationInfo.useCnt.as("vacationUseCnt")
        ))
                .from(user)
                .innerJoin(vacationInfo)
                    .on(user.userId.eq(vacationInfo.id.userId).and(vacationInfo.id.years.eq(year)))
                .where(user.useYn.eq(true)).fetch();
    }


    public List<SeatResponseDto> findSeatByUser() {

        return jpaQueryFactory.select(Projections.constructor(SeatResponseDto.class
                    ,seat.id
                    ,seat.className
                    ,seat.left
                    ,seat.top
                    ,user.userName
                    ,user.userId
                    ,user.profileImage.as("imgSrc")
                    ,user.tel
                    ,user.email
                    ,user.orgCode
                    ,organization.orgName
                    ,organization.orgFullCode.as("fullCode")
                    ,organization.orgFullName.as("fullName")
                    ,organization.color
                    ,user.rankNm
                    ,user.jobNm
                    ,user.ex
        ))
                .from(seat)
                .leftJoin(user)
                    .on(seat.id.eq(user.seatId).and(user.useYn.eq(true)))
                .leftJoin(organization)
                    .on(user.orgCode.eq(organization.orgCode))
                .fetch();
    }

    public List<String> findEmailByVacationId(Long vacationId) {

        return jpaQueryFactory.select(user.email)
                .from(user)
                .innerJoin(approver)
                    .on(user.userId.eq(approver.userId))
                .innerJoin(vacation)
                    .on(approver.vacationId.eq(vacation.vacationId))
                .where(vacation.vacationId.eq(vacationId))
                .fetch();
    }

    private BooleanExpression eqSearch(String searchType, String searchWord) {

        if (StringUtils.isEmpty(searchType)) {
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
    private BooleanExpression eqUse(String searchType) {

        return user.useYn.eq(!Optional.ofNullable(searchType).orElse("").equals("use"));

    }

    private BooleanExpression ctOrgFullCode(String orgCode) {
        if (StringUtils.isEmpty(orgCode)) {
            return null;
        }
        return organization.orgFullCode.contains(orgCode);
    }
    private BooleanExpression eqYear(String year) {
        if (StringUtils.isEmpty(year)) {
            return null;
        }
        return vacationInfo.id.years.eq(year);
    }

    private List<OrderSpecifier> getOrders(String sort, String seq) {

        List<OrderSpecifier> orders = new ArrayList<>();

        if(!sort.isEmpty()) {
            Order direction = seq.equals("desc") ? Order.DESC : Order.ASC;
            orders.add(QueryDslUtil.getSortedColumn(direction,sort.equals("orgName") ? organization : user ,sort));

        }
        orders.add(QueryDslUtil.getSortedColumn(Order.ASC,user,"createdAt"));
        return orders;
    }
}
