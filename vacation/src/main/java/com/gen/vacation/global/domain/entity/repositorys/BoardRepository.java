package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 1:40
 */
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM tb_board b WHERE b.id = :id")
    void deleteById(@Param("id") Long id);

}
