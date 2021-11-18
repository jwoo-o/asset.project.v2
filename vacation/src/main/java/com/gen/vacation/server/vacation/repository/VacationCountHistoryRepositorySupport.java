package com.gen.vacation.server.vacation.repository;


import com.gen.vacation.server.vacation.dto.VacationCountHistoryRequestDto;
import com.gen.vacation.server.vacation.dto.VacationSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QAdmin.admin;
import static com.gen.vacation.global.domain.entity.QVacationCountHistory.vacationCountHistory;

@RequiredArgsConstructor
@Repository
public class VacationCountHistoryRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public Map<String, Object> findAllBySearch(String userId,VacationSearchDto dto) throws Exception {


        Map<String, Object> data = new HashMap<>();
        List<VacationCountHistoryRequestDto> list = new ArrayList<>();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(vacationCountHistory.userId.eq(userId));
        builder.and(vacationCountHistory.years.eq(dto.getYear()));

        List<Long> ids = jpaQueryFactory.select(vacationCountHistory.id)
                .from(vacationCountHistory)
                .where(builder)
                .orderBy(vacationCountHistory.modifiedAt.desc())
                .limit(dto.getTotal()).offset(dto.getOffset())
                .fetch();


        int total = ids.size();
        total += dto.getOffset();

        if (!ids.isEmpty()) {
            list = jpaQueryFactory.select(Projections.constructor(VacationCountHistoryRequestDto.class
                    , vacationCountHistory.id
                    , vacationCountHistory.changeCnt
                    , vacationCountHistory.changeReason
                    , vacationCountHistory.writeType
                    , admin.name.as("adminName")
                    , vacationCountHistory.pcIp
                    , vacationCountHistory.modifiedAt
            ))
                    .from(vacationCountHistory)
                    .leftJoin(admin)
                        .on(vacationCountHistory.adminId.eq(admin.adminId))
                    .where(vacationCountHistory.id.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList())))
                    .orderBy(vacationCountHistory.id.desc()).fetch();

        }
        data.put("vacationChangeList", list);
        data.put("total", total);


        return data;
    }
}
