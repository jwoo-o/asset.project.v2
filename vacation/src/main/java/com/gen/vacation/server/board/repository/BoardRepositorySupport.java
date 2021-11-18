package com.gen.vacation.server.board.repository;

import com.gen.vacation.global.enums.BoardEnum;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.server.board.dto.BoardListResponseDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QAdmin.admin;
import static com.gen.vacation.global.domain.entity.QBoard.board;
import static com.gen.vacation.global.domain.entity.QBoardFile.boardFile;
import static com.gen.vacation.global.domain.entity.QVacation.vacation;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 2:48
 */

@RequiredArgsConstructor
@Repository
public class BoardRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression eqSearch(String searchType, String searchWord) {

        if(StringUtils.isEmpty(searchType)){
            return null;
        }
        switch (searchType) {

            case "name":
                return vacation.userId.eq(searchWord);
            case "subject":
                return vacation.user.userName.eq(searchWord);
            case "content":
                return board.content.contains(searchWord);
            default:
                return null;
        }
    }

    public Map<String, Object> findAllBySearch(SearchRequestDto dto) throws Exception {


        Map<String, Object> data = new HashMap<>();
        List<BoardListResponseDto> list = new ArrayList<>();

        List<Long> ids = jpaQueryFactory.select(board.id)
                .from(board)
                .where(board.createdAt.loe(LocalDateTime.now()),
                        board.category.eq(BoardEnum.NOTICE),
                        eqSearch(dto.getSearchType(),dto.getSearchWord()))
                .orderBy(board.createdAt.desc())
                .limit(dto.getTotal())
                .offset(dto.getOffset()).fetch();

        int total = ids.size();
        total += dto.getOffset();

        if(!ids.isEmpty()) {
            list = jpaQueryFactory.select(Projections.constructor(BoardListResponseDto.class
                    , board.id
                    , board.subject
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(admin.name)
                                    .from(admin).where(admin.adminId.eq(board.adminId)), "writer")
                    , ExpressionUtils
                            .as(JPAExpressions
                                    .select(boardFile.id.count()).from(boardFile).where(boardFile.boardId.eq(board.id)), "isAttach")

                    , board.createdAt
                    , board.modifiedAt
                    , board.count
            )).from(board)
                    .where(board.id.in(ids.stream().limit(dto.getLimit()).collect(Collectors.toList()))).orderBy(board.id.desc()).fetch();

        }

        data.put("noticeList", list);
        data.put("total", total);

        return data;
    }
}
