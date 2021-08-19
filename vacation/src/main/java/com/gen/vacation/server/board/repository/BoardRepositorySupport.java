package com.gen.vacation.server.board.repository;

import com.gen.vacation.global.Enum.BoardEnum;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.Board;
import com.gen.vacation.server.board.dto.BoardListResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gen.vacation.global.domain.entity.QAdmin.admin;
import static com.gen.vacation.global.domain.entity.QBoard.board;
import static com.gen.vacation.global.domain.entity.QBoardFile.boardFile;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 2:48
 */
@Repository
public class BoardRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    private EntityManager em;

    public BoardRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Board.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Map<String, Object> findAllBySearch(SearchRequestDto dto) throws Exception {


        Map<String, Object> data = new HashMap<String, Object>();
        List<BoardListResponseDto> list = new ArrayList<>();
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(board.createdAt.loe(LocalDateTime.now()));
        builder.and(board.category.eq(BoardEnum.NOTICE));
        if (!StringUtils.isEmpty(dto.getSearchType())) {

            switch (dto.getSearchType()) {

                case "name":
                    builder.and(board.adminId.in(JPAExpressions.select(admin.adminId).from(admin).from(admin).where(admin.name.eq(dto.getSearchWord()))));
                    break;
                case "subject":
                    builder.and(board.subject.eq(dto.getSearchWord()));
                    break;
                case "content":
                    builder.and(board.content.contains(dto.getSearchWord()));
                    break;
            }
        }
        List<Long> ids = jpaQueryFactory.select(board.id)
                .from(board)
                .where(builder).orderBy(board.createdAt.desc()).limit(dto.getTotal()).offset(dto.getOffset()).fetch();

        int total = ids.size();
        total += dto.getOffset();

        if(ids.size() > 0) {
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
