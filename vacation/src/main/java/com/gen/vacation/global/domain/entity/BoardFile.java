package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-07-03
 * Time: 오후 4:25
 */
@NoArgsConstructor
@Getter
@Entity(name = "tb_board_file")
public class BoardFile extends BaseEntity {


    /**
     * 파일이름
     */
    @Column
    private String fileName;

    /**
     * 업로드된  파일이름
     */
    @Column
    private String key;

    /**
     * 파일 사이즈
     */
    @Column
    private Long fileSize;

    /**
     * 게시판 키
     */
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", updatable = false, insertable = false)
    private Board board;

    @Builder
    public BoardFile(String fileName, String key, Long fileSize) {
        this.fileName = fileName;
        this.key = key;
        this.fileSize = fileSize;
    }

    public void update(Long boardId) {

        this.boardId = boardId;
    }

}
