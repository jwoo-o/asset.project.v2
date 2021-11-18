package com.gen.vacation.global.domain.entity;


import com.gen.vacation.global.domain.common.BooleanToYNConverter;
import com.gen.vacation.global.enums.LoginHistoryEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-08-25
 * Time: 오전 10:13
 */
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "tb_login_history")
@Table(name = "tb_login_history", indexes = {@Index(name = "i_tb_login_history_login_id_login_type_login_dt", columnList = "login_id,login_type,login_dt")})
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 로그인 아이디
     */
    @Column(name = "login_id")
    private String loginId;

    /**
     * 로그인 로그 등록 시간
     */
    @Column(name = "login_dt")
    @CreatedDate
    private LocalDateTime loginDt;

    /**
     * 로그인 아이피
     */
    @Column(name = "pc_ip", length = 25)
    private String pcIp;

    /**
     * 로그인 분류(사용자[agent],관리자)
     */
    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginHistoryEnum loginType;

    /**
     * 로그인 성공 분류
     */
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "login_yn", length = 2)
    private boolean loginYn;

    /**
     * 로그인 실패 메시지
     */
    @Column(name = "err_msg")
    private String errMsg;

    @Builder
    public LoginHistory(String loginId, String pcIp, LoginHistoryEnum loginType, boolean loginYn, String errMsg) {
        this.loginId = loginId;
        this.pcIp = pcIp;
        this.loginType = loginType;
        this.loginYn = loginYn;
        this.errMsg = errMsg;
    }

    public void success(boolean loginYn, String errMsg) {
        this.loginYn = loginYn;
        this.errMsg = errMsg;
    }

    public void fail(String errMsg) {
        this.errMsg = errMsg;
    }
}
