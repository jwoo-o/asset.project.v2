package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 1:40
 */
public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {


    @Override
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM tb_board_file b WHERE b.id = :id")
    void deleteById(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE tb_board_file b set b.boardId= :boardId WHERE b.id in :ids")
    void updateBoardIdByIds(@Param("ids") Collection<Long> ids, @Param("boardId") Long boardId);
}
