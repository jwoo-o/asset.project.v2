package com.gen.vacation.server.user.repository;

import com.gen.vacation.global.Enum.LoginHistoryEnum;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.User;
import com.gen.vacation.global.util.QueryDslUtil;
import com.gen.vacation.server.user.dto.SeatResponseDto;
import com.gen.vacation.server.user.dto.UserListResponseDto;
import com.gen.vacation.server.user.dto.VacationInfoListResponseDto;
import com.gen.vacation.server.vacation.dto.VacationSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
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

import static com.gen.vacation.global.domain.entity.QOrganization.organization;
import static com.gen.vacation.global.domain.entity.QSeat.seat;
import static com.gen.vacation.global.domain.entity.QUser.user;
import static com.gen.vacation.global.domain.entity.QVacationDeadline.vacationDeadline;
import static com.gen.vacation.global.domain.entity.QVacationInfo.vacationInfo;
import static com.gen.vacation.global.domain.entity.QVacation.vacation;
import static com.gen.vacation.global.domain.entity.QApprover.approver;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public UserRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(User.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
    public Map<String, Object> findAllBySearch(String orgCode) throws Exception {

        Map<String, Object> data = new HashMap<>();
        BooleanBuilder builder = new BooleanBuilder();
        List<UserListResponseDto> list;
        builder.and(user.useYn.eq(true));
        builder.and(user.createdAt.loe(LocalDateTime.now()));
        builder.and(user.organization.orgFullCode.contains(orgCode));
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
                , user.useYn
                , user.createdAt
                , user.modifiedAt
            )).from(user)
                    .where(builder)
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

        int total = 0;
        List<UserListResponseDto> list = new ArrayList<>();
        List<OrderSpecifier> orders = new ArrayList<>();
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(user.createdAt.loe(LocalDateTime.now()));
        if (!StringUtils.isEmpty(dto.getSearchType())) {

            switch (dto.getSearchType()) {

                case "userId":
                    builder.and(user.userId.eq(dto.getSearchWord()));
                    break;
                case "name":
                    builder.and(user.userName.eq(dto.getSearchWord()));
                    break;
                default:
                    break;
            }
            builder.and(user.useYn.eq(!dto.getSearchType().equals("use")));
        } else {
            builder.and(user.useYn.eq(true));
        }


        if(!dto.getSort().isEmpty()) {
            Order direction = dto.getSeq().equals("desc") ? Order.DESC : Order.ASC;

            switch (dto.getSort()) {
                case "orgName":
                    orders.add(QueryDslUtil.getSortedColumn(direction,organization,dto.getSort()));
                    break;
                default:
                    orders.add(QueryDslUtil.getSortedColumn(direction,user,dto.getSort()));
                    break;
            }
        }
        orders.add(QueryDslUtil.getSortedColumn(Order.ASC,user,"createdAt"));


        if (!StringUtils.isEmpty(dto.getOrgCode())){
            builder.and(user.organization.orgFullCode.contains(dto.getOrgCode()));
        }

        List<String> ids = jpaQueryFactory.select(user.userId)
                .from(user).join(organization).on(user.orgCode.eq(organization.orgCode))
                .where(builder).limit(dto.getTotal()).offset(dto.getOffset())
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .fetch();

        total = ids.size();
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
            )).from(user).where(user.userId.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList())))
                    .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                    .fetch();
        }

        data.put("userList", list);
        data.put("total", total);

        return data;
    }

    public Map<String, Object> findVacationInfos(VacationSearchDto dto) {
        Map<String, Object> data = new HashMap<>();

        int total = 0;
        List<VacationInfoListResponseDto> list = new ArrayList<>();
        List<OrderSpecifier> orders = new ArrayList<>();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(user.createdAt.loe(LocalDateTime.now()));
        if (!StringUtils.isEmpty(dto.getSearchType())) {

            switch (dto.getSearchType()) {
                case "userId":
                    builder.and(user.userId.eq(dto.getSearchWord()));
                    break;
                case "name":
                    builder.and(user.userName.eq(dto.getSearchWord()));
                    break;
                default:
                    break;
            }
            builder.and(user.useYn.eq(!dto.getSearchType().equals("use")));
        } else {
            builder.and(user.useYn.eq(true));
        }


        if (!StringUtils.isEmpty(dto.getYear())){
            builder.and(vacationInfo.id.years.eq(dto.getYear()));
        }
        if (!StringUtils.isEmpty(dto.getOrgCode())){
            builder.and(user.organization.orgFullCode.contains(dto.getOrgCode()));
        }

        if(!dto.getSort().isEmpty()) {
            Order direction = dto.getSeq().equals("desc") ? Order.DESC : Order.ASC;

            switch (dto.getSort()) {
                case "orgName":
                    orders.add(QueryDslUtil.getSortedColumn(direction,organization,dto.getSort()));
                    break;
                default:
                    orders.add(QueryDslUtil.getSortedColumn(direction,user,dto.getSort()));
                    break;
            }
        }
        orders.add(QueryDslUtil.getSortedColumn(Order.ASC,user,"createdAt"));



        List<String> ids = jpaQueryFactory.select(user.userId)
                .from(user).innerJoin(vacationInfo).on(user.userId.eq(vacationInfo.id.userId))
                .innerJoin(organization).on(user.orgCode.eq(organization.orgCode))
                .where(builder).limit(dto.getTotal()).offset(dto.getOffset())
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .fetch();
        total = ids.size();
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
                                    .select(new CaseBuilder().when(vacationDeadline.deadlines.isNotNull()).then(true).otherwise(false))
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
                                    ), "sender")
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(new CaseBuilder().when(vacationDeadline.deadlines.isNotNull()).then(true).otherwise(false))
                                    .from(vacationDeadline)
                                    .where(
                                            vacationDeadline.id.userId.eq(user.userId)
                                                    .and(vacationDeadline.id.years.eq(dto.getYear())
                                                            .and(vacationDeadline.id.writeType.eq(LoginHistoryEnum.ADMIN)))
                                    ), "adminDeadline")
                    , user.rankNm
                    , user.jobNm
                    , user.hireDate
                    , user.leaveDate
                    , user.useYn
                    , vacationInfo.totalCnt.as("vacationTotalCnt")
                    , vacationInfo.useCnt.as("vacationUseCnt")
            )).from(user).innerJoin(vacationInfo).on(user.userId.eq(vacationInfo.id.userId).and(vacationInfo.id.years.eq(dto.getYear())))
                    .where(user.userId.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList())))
                    .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
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
            )).from(user).innerJoin(vacationInfo).on(user.userId.eq(vacationInfo.id.userId).and(vacationInfo.id.years.eq(year)))
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
        )).from(user).innerJoin(vacationInfo).on(user.userId.eq(vacationInfo.id.userId).and(vacationInfo.id.years.eq(year)))
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
        )).from(seat)
                .leftJoin(user).on(seat.id.eq(user.seatId).and(user.useYn.eq(true)))
                .leftJoin(organization).on(user.orgCode.eq(organization.orgCode))
                .fetch();
    }

    public List<String> findEmailByVacationId(Long vacationId) {

        return jpaQueryFactory.select(user.email)
                .from(user)
                .innerJoin(approver).on(user.userId.eq(approver.userId))
                .innerJoin(vacation).on(approver.vacationId.eq(vacation.vacationId))
                .where(vacation.vacationId.eq(vacationId)).fetch();
    }
}
