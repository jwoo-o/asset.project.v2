package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.Enum.BoardEnum;
import com.gen.vacation.global.domain.common.BaseEntity;
import com.gen.vacation.server.board.dto.NoticeInfoRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-07-03
 * Time: 오후 4:04
 */
@NoArgsConstructor
@Getter
@Entity(name = "tb_board")
@Table(name = "tb_board",
        indexes = {@Index(name = "i_tb_board_category_created_at", columnList = "category,created_at")})
public class Board extends BaseEntity {


    /**
     * 제목
     */
    @Column(nullable = false)
    private String subject;

    /**
     * 패스워드
     */
    @Column
    private String pwd;

    /**
     * 내용
     */
    @Column(length = 8192)
    private String content;

    /**
     * 등록자 ip
     */
    @Column(length = 100)
    private String ip;

    /**
     * 연락처
     */
    @Column(length = 100)
    private String tel;


    /**
     * 카테고리 분류()
     */
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private BoardEnum category;

    /**
     * 조회수
     */
    @Column
    private int count;

    /**
     * 사용자 아이디
     */
    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    /**
     * 관리자 아이디
     */
    @Column(name = "admin_id")
    private String adminId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", updatable = false, insertable = false)
    private Admin admin;

    @OneToMany(mappedBy = "board")
    private List<BoardFile> boardFiles = new ArrayList<BoardFile>();


    @Builder
    public Board(String subject, String pwd, String content, String ip, String tel, BoardEnum category, int count, String userId, String adminId) {
        this.subject = subject;
        this.pwd = pwd;
        this.content = content;
        this.ip = ip;
        this.tel = tel;
        this.category = category;
        this.count = count;
        this.userId = userId;
        this.adminId = adminId;
    }

    public void updateCount() {
        this.count++;
    }


    public void update(NoticeInfoRequestDto dto) {
        this.subject = dto.getSubject();
        this.content = dto.getContent();
    }

}
