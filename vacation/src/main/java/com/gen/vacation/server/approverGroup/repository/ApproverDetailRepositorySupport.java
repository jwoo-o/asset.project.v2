package com.gen.vacation.server.approverGroup.repository;

import com.gen.vacation.global.domain.entity.ApproverDetail;
import com.gen.vacation.global.domain.entity.ApproverGroup;
import com.gen.vacation.server.user.dto.UserListResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static com.gen.vacation.global.domain.entity.QApproverDetail.approverDetail;

@Repository
public class ApproverDetailRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;


    public ApproverDetailRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(ApproverDetail.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Long> getApproverDetailCode(Long approverGroupCode) {
        List<Long> list = new ArrayList<>();

        list = jpaQueryFactory.select(
                approverDetail.approverDetailCode
        ).from(approverDetail).where(approverDetail.approverGroupCode.eq(approverGroupCode)).fetch();

        return list;
    }
}
