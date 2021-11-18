package com.gen.vacation.server.cmmn.repository;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.server.cmmn.dto.CommonGroupCodeResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QCommonCodeGroup.commonCodeGroup;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 2:48
 */
@RequiredArgsConstructor
@Repository
public class CommonCodeGroupRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public Map<String, Object> findAllBySearch(SearchRequestDto dto) {

        Map<String, Object> result = new HashMap<>();
        List<CommonGroupCodeResponseDto> list = new ArrayList<>();

        List<String> codes = jpaQueryFactory.select(commonCodeGroup.groupCode)
                .from(commonCodeGroup)
                .where(eqSearch(dto.getSearchType(),dto.getSearchWord()))
                .orderBy(commonCodeGroup.createdAt.desc())
                .limit(dto.getTotal())
                .offset(dto.getOffset())
                .fetch();

        int total = codes.size();
        total += dto.getOffset();

        if(!codes.isEmpty() ){
            list = jpaQueryFactory.select(Projections.constructor(CommonGroupCodeResponseDto.class,
                    commonCodeGroup.groupCode, commonCodeGroup.groupDesc, commonCodeGroup.groupName, commonCodeGroup.modifiedAt))
                    .from(commonCodeGroup)
                    .where(commonCodeGroup.groupCode.in(codes.stream().limit(dto.getLimit()).collect(Collectors.toList())))
                    .fetch();

        }

        result.put("commonGroupList", list);
        result.put("total", total);

        return result;
    }

    private BooleanExpression eqSearch(String searchType, String searchWord) {

        if(StringUtils.isEmpty(searchType)){
            return null;
        }
        switch (searchType) {

            case "groupCode":
                return commonCodeGroup.groupCode.eq(searchWord);
            case "groupName":
                return commonCodeGroup.groupName.eq(searchWord);
            default:
                return null;
        }
    }
}
