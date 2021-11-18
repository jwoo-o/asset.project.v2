package com.gen.vacation.server.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;



import java.util.List;


import static com.gen.vacation.global.domain.entity.QAdmin.admin;


@RequiredArgsConstructor
@Repository
public class AdminRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public List<String> findEmailById(String adminId) {

        return jpaQueryFactory.select(admin.email).from(admin)
                .where(admin.adminId.ne(adminId).and(admin.useYn.eq(true))).fetch();
    }
}
