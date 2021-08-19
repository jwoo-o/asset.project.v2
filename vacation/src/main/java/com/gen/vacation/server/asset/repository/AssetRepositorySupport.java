package com.gen.vacation.server.asset.repository;

import com.gen.vacation.global.domain.entity.Asset;
import com.gen.vacation.server.asset.dto.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QOrganization.organization;
import static com.gen.vacation.global.domain.entity.QUser.user;
import static com.gen.vacation.global.domain.entity.QAsset.asset;
import static com.gen.vacation.global.domain.entity.QCommonCodeDetail.commonCodeDetail;

@Repository
public class AssetRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public AssetRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Asset.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Map<String, Object> findAllToExcelBySearch(AssetSearchRequestDto dto) throws Exception {

        int total;
        Map<String, Object> data = new HashMap<>();
        BooleanBuilder builder = new BooleanBuilder();
        List<AssetExcelListResponseDto> list;
        builder.and(asset.assetId.gt(""));
        if(!StringUtils.isEmpty(dto.getAssetId())) {
            builder.and(asset.assetId.eq(dto.getAssetId()));
        }
        if(!StringUtils.isEmpty(dto.getCategory())) {
            builder.and(asset.category.eq(dto.getCategory()));
        }
        if (!StringUtils.isEmpty(dto.getOrgCode())){
            builder.and(asset.organization.orgFullCode.contains(dto.getOrgCode()));
        }
        if(!StringUtils.isEmpty(dto.getRankCd())) {
            builder.and(asset.user.rankCd.eq(dto.getRankCd()));
        }
        if(!StringUtils.isEmpty(dto.getNote())) {
            builder.and(asset.note.contains(dto.getNote()));
        }
        if(!StringUtils.isEmpty(dto.getUserName())) {
            builder.and(asset.user.userName.contains(dto.getUserName()));
        }
        if(!StringUtils.isEmpty(dto.getStatus())) {
            builder.and(asset.status.eq(dto.getStatus()));
        }
        if(!StringUtils.isEmpty(dto.getStartDate())) {
            builder.and(asset.purchaseDate.goe(LocalDate.parse(dto.getStartDate())));
        }
        if(!StringUtils.isEmpty(dto.getEndDate())) {
            builder.and(asset.purchaseDate.loe(LocalDate.parse(dto.getEndDate())));
        }
        list = jpaQueryFactory.select(Projections.constructor(AssetExcelListResponseDto.class
                    , asset.assetId
                    , asset.category
                    , asset.modelName
                    , user.userName
                    , user.rankNm.as("rankName")
                    , organization.orgFullName
                    , asset.purchaseDate
                    , ExpressionUtils
                        .as(JPAExpressions
                                .select(commonCodeDetail.detailName)
                                .from(commonCodeDetail).where(commonCodeDetail.id.groupCode.eq("assetStatus").and(commonCodeDetail.id.detailCode.eq(asset.status))), "status")
                    , asset.note
                    , asset.createdAt
            )).from(asset)
                .join(organization).on(asset.orgCode.eq(organization.orgCode))
                .leftJoin(user).on(asset.userId.eq(user.userId))
                    .where(builder).limit(dto.getLimit()).offset(dto.getExcelOffset()).orderBy(asset.assetId.desc()).fetch();
        total = list.size();
        total += dto.getOffset();

        data.put("assetList", list);
        data.put("total", total);

        return data;
    }

    public Map<String, Object> findAllBySearch(AssetSearchRequestDto dto) throws Exception {
        int total;
        Map<String, Object> data = new HashMap<>();
        BooleanBuilder builder = new BooleanBuilder();
        List<AssetListResponseDto> list = new ArrayList<>();
        builder.and(asset.assetId.gt(""));
        if(!StringUtils.isEmpty(dto.getAssetId())) {
            builder.and(asset.assetId.eq(dto.getAssetId()));
        }
        if(!StringUtils.isEmpty(dto.getCategory())) {
            builder.and(asset.category.eq(dto.getCategory()));
        }
        if (!StringUtils.isEmpty(dto.getOrgCode())){
            builder.and(organization.orgFullCode.contains(dto.getOrgCode()));
        }
        if(!StringUtils.isEmpty(dto.getRankCd())) {
            builder.and(asset.user.rankCd.eq(dto.getRankCd()));
        }
        if(!StringUtils.isEmpty(dto.getNote())) {
            builder.and(asset.note.contains(dto.getNote()));
        }
        if(!StringUtils.isEmpty(dto.getUserName())) {
            builder.and(user.userName.contains(dto.getUserName()));
        }
        if(!StringUtils.isEmpty(dto.getStatus())) {
            builder.and(asset.status.eq(dto.getStatus()));
        } else {
            builder.and(asset.status.ne("D"));
        }
        if(!StringUtils.isEmpty(dto.getStartDate())) {
            builder.and(asset.purchaseDate.goe(LocalDate.parse(dto.getStartDate())));
        }
        if(!StringUtils.isEmpty(dto.getEndDate())) {
            builder.and(asset.purchaseDate.loe(LocalDate.parse(dto.getEndDate() )));
        }
        List<String> ids = jpaQueryFactory.select(asset.assetId)
                .from(asset)
                .join(organization).on(asset.orgCode.eq(organization.orgCode))
                .leftJoin(user).on(asset.userId.eq(user.userId))
                .where(builder).limit(dto.getTotal()).offset(dto.getOffset()).orderBy(asset.assetId.desc()).fetch();

        total = ids.size();
        total += dto.getOffset();

        if (!ids.isEmpty()) {
            list = jpaQueryFactory.select(Projections.constructor(AssetListResponseDto.class
                    , asset.assetId
                    , asset.userId
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(user.userName)
                                    .from(user).where(user.userId.eq(asset.userId)), "userName")
                    , asset.orgCode
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(organization.orgName)
                                    .from(organization).where(organization.orgCode.eq(asset.orgCode)), "orgName")
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(user.rankNm)
                                    .from(user).where(user.userId.eq(asset.userId)), "rankName")
                    , asset.category
                    , asset.modelName
                    , asset.purchaseDate
                    , asset.note
                    , asset.status
                    , asset.createdAt
                    , asset.modifiedAt
            )).from(asset).where(asset.assetId.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList()))).fetch();

        }
        data.put("assetList", list);
        data.put("total", total);

        return data;
    }

    public AssetResponseDto findById(String assetId)  throws Exception {

        return jpaQueryFactory.select(Projections.constructor(AssetResponseDto.class
                , asset.assetId
                , asset.category
                , asset.manufacture
                , asset.modelName
                , ExpressionUtils
                        .as(JPAExpressions
                                .select(user.userName)
                                .from(user).where(user.userId.eq(asset.userId)), "userName")
                , asset.serialNumber
                , asset.price
                , asset.purchaseDate
                , ExpressionUtils
                        .as(JPAExpressions
                                .select(organization.orgFullName)
                                .from(organization).where(organization.orgCode.eq(asset.orgCode)), "orgFullName")
                , asset.note
                , ExpressionUtils
                        .as(JPAExpressions
                                .select(commonCodeDetail.detailName)
                                .from(commonCodeDetail).where(commonCodeDetail.id.groupCode.eq("assetStatus").and(commonCodeDetail.id.detailCode.eq(asset.status))), "status")
                , asset.assetInfo
                , asset.createdAt
                , asset.modifiedAt
                )).from(asset).where(asset.assetId.eq(assetId)).fetchOne();
    }

    public List<AssetTotalDto> findCountByOrg(List<String> orgCode)  throws Exception {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(asset.status.ne("D"));
        builder.and(asset.orgCode.in(orgCode));
        return jpaQueryFactory.select(Projections.constructor(AssetTotalDto.class
                , ExpressionUtils
                        .as(JPAExpressions
                                .select(organization.orgName)
                                .from(organization).where(organization.orgCode.eq(asset.orgCode)), "name")
                , asset.count().as("count")
                )).from(asset).where(builder).groupBy(asset.orgCode).fetch();


    }

    public List<Asset> findAll(List<String> orgCode) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(asset.orgCode.in(orgCode));
        return jpaQueryFactory.select(asset).from(asset).where(builder).orderBy(asset.modifiedAt.desc()).fetch();
    }
}
