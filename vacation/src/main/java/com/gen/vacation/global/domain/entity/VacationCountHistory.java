package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.enums.LoginHistoryEnum;
import com.gen.vacation.global.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-08-03
 * Time: 오전 8:53
 */
@NoArgsConstructor
@Getter
@Entity(name = "tb_vacation_count_history")
@Table(name = "tb_vacation_count_history")
@ToString
public class VacationCountHistory extends BaseEntity {

    /**
     * 관리자계정
     */
    @Column(length = 20, name = "admin_id")
    private String adminId;

    @Column(name = "years")
    private String years;

    @Column(name = "user_id", length = 20)
    private String userId;

    /**
     * 작성 아이피
     */
    @Column(name = "pc_ip", length = 25)
    private String pcIp;

    @Column(name = "change_cnt")
    private String changeCnt;

    @Column(name = "change_reason")
    private String changeReason;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", updatable = false, insertable = false)
    private Admin admin;

    /**
     * (사용자,관리자,SYSTEM)
     */
    @Column(name = "write_type")
    @Enumerated(EnumType.STRING)
    private LoginHistoryEnum writeType;


    @Builder
    public VacationCountHistory(String adminId, String years, String userId, String pcIp, String changeCnt, String changeReason, LoginHistoryEnum writeType) {
        this.adminId = adminId;
        this.years = years;
        this.userId = userId;
        this.pcIp = pcIp;
        this.changeCnt = changeCnt;
        this.changeReason = changeReason;
        this.writeType = writeType;
    }
}
