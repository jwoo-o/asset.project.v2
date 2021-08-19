package com.gen.vacation.server.cmmn.repository;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.CommonCodeGroup;
import com.gen.vacation.server.cmmn.dto.CommonGroupCodeResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@Repository
public class CommonCodeGroupRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    private EntityManager em;

    public CommonCodeGroupRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(CommonCodeGroup.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public Map<String, Object> findAllBySearch(SearchRequestDto dto) {
        Map<String, Object> result = new HashMap<String, Object>();
        BooleanBuilder builder = new BooleanBuilder();
        List<CommonGroupCodeResponseDto> list = new ArrayList<>();
        if (!StringUtils.isEmpty(dto.getSearchType())) {

            switch (dto.getSearchType()) {

                case "groupCode":
                    builder.and(commonCodeGroup.groupCode.eq(dto.getSearchWord()));
                    break;
                case "groupName":
                    builder.and(commonCodeGroup.groupName.eq(dto.getSearchWord()));
                    break;
            }
        }

        List<String> codes = jpaQueryFactory.select(commonCodeGroup.groupCode)
                .from(commonCodeGroup).where(builder).orderBy(commonCodeGroup.createdAt.desc()).limit(dto.getTotal()).offset(dto.getOffset()).fetch();

        int total = codes.size();
        total += dto.getOffset();

        if(codes.size() > 0 ){
            list = jpaQueryFactory.select(Projections.constructor(CommonGroupCodeResponseDto.class,
                    commonCodeGroup.groupCode, commonCodeGroup.groupDesc, commonCodeGroup.groupName, commonCodeGroup.modifiedAt))
                    .from(commonCodeGroup).where(commonCodeGroup.groupCode.in(codes.stream().limit(dto.getLimit()).collect(Collectors.toList()))).fetch();

        }

        result.put("commonGroupList", list);
        result.put("total", total);

        return result;
    }
}
