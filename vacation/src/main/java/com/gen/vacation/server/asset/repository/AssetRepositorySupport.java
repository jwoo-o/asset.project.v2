package com.gen.vacation.server.asset.repository;

import com.gen.vacation.global.domain.entity.Asset;
import com.gen.vacation.server.asset.dto.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Repository
public class AssetRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public Map<String, Object> findAllToExcelBySearch(AssetSearchRequestDto dto) throws Exception {

        Map<String, Object> data = new HashMap<>();

        List<AssetExcelListResponseDto> list = jpaQueryFactory.select(Projections.constructor(AssetExcelListResponseDto.class
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
            ))
                .from(asset)
                .join(organization)
                    .on(asset.orgCode.eq(organization.orgCode))
                .leftJoin(user)
                    .on(asset.userId.eq(user.userId))
                .where(asset.assetId.gt(""),
                        eqAssetId(dto.getAssetId()),
                        eqCategory(dto.getCategory()),
                        eqRankCd(dto.getRankCd()),
                        eqStatus(dto.getStatus()),
                        ctNote(dto.getNote()),
                        ctOrgFullCode(dto.getOrgCode()),
                        ctUserName(dto.getUserName()),
                        goeStartDate(dto.getStartDate()),
                        loeEndDate(dto.getEndDate())
                        )
                .limit(dto.getLimit())
                .offset(dto.getExcelOffset())
                .orderBy(asset.assetId.desc()).fetch();

        int total = list.size();
        total += dto.getOffset();

        data.put("assetList", list);
        data.put("total", total);

        return data;
    }

    public Map<String, Object> findAllBySearch(AssetSearchRequestDto dto) throws Exception {
        Map<String, Object> data = new HashMap<>();

        List<String> ids = jpaQueryFactory.select(asset.assetId)
                .from(asset)
                .join(organization)
                    .on(asset.orgCode.eq(organization.orgCode))
                .leftJoin(user)
                    .on(asset.userId.eq(user.userId))
                .where(asset.assetId.gt(""),
                        eqAssetId(dto.getAssetId()),
                        eqCategory(dto.getCategory()),
                        eqRankCd(dto.getRankCd()),
                        eqStatus(dto.getStatus()),
                        ctNote(dto.getNote()),
                        ctOrgFullCode(dto.getOrgCode()),
                        ctUserName(dto.getUserName()),
                        goeStartDate(dto.getStartDate()),
                        loeEndDate(dto.getEndDate())
                )
                .limit(dto.getTotal())
                .offset(dto.getOffset())
                .orderBy(asset.assetId.desc()).fetch();

        List<AssetListResponseDto> list = new ArrayList<>();
        int total = ids.size();
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
            ))
                    .from(asset)
                    .where(asset.assetId.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList())))
                    .orderBy(asset.assetId.desc())
                    .fetch();

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
                ))
                .from(asset)
                .where(eqAssetId(assetId))
                .fetchOne();
    }

    public List<Asset> findAll(String fullCode) {
        return jpaQueryFactory.select(asset)
                .from(asset)
                .innerJoin(organization)
                    .on(asset.orgCode.eq(organization.orgCode))
                .where(organization.orgFullCode.like(fullCode+"%"))
                .orderBy(asset.modifiedAt.desc())
                .fetch();
    }



    private BooleanExpression eqAssetId(String assetId) {
        if(StringUtils.isEmpty(assetId)) {
            return null;
        }
        return asset.assetId.eq(assetId);
    }
    private BooleanExpression eqCategory(String category) {
        if(StringUtils.isEmpty(category)) {
            return null;
        }
        return asset.category.eq(category);
    }
    private BooleanExpression ctOrgFullCode(String orgCode) {
        if(StringUtils.isEmpty(orgCode)) {
            return null;
        }
        return organization.orgFullCode.contains(orgCode);
    }
    private BooleanExpression eqRankCd(String rankCd) {
        if(StringUtils.isEmpty(rankCd)) {
            return null;
        }
        return user.rankCd.eq(rankCd);
    }
    private BooleanExpression ctNote(String note) {
        if(StringUtils.isEmpty(note)) {
            return null;
        }
        return asset.note.contains(note);
    }
    private BooleanExpression ctUserName(String userName) {
        if(StringUtils.isEmpty(userName)) {
            return null;
        }
        return user.userName.contains(userName);
    }
    private BooleanExpression eqStatus(String status) {
        if(StringUtils.isEmpty(status)) {
            return asset.status.ne("D");
        }
        return asset.status.eq(status);
    }
    private BooleanExpression goeStartDate(String startDate) {
        if(StringUtils.isEmpty(startDate)) {
            return null;
        }
        return asset.purchaseDate.goe(LocalDate.parse(startDate));
    }
    private BooleanExpression loeEndDate(String endDate) {
        if(StringUtils.isEmpty(endDate)) {
            return null;
        }
        return asset.purchaseDate.loe(LocalDate.parse(endDate));
    }

}
