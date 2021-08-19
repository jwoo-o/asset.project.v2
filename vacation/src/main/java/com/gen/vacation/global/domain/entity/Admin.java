package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.domain.common.BooleanToYNConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Entity(name = "tb_admin")
@Table(name = "tb_admin")
public class Admin extends BaseTimeEntity implements Persistable<String> {

    /**
     * 관리자계정
     */
    @Id
    @Column(length = 20, name = "admin_id")
    private String adminId;

    /**
     * 패스워드
     */
    @Column(length = 100)
    private String password;

    /**
     * 암호화 유무
     */
    @Convert(converter = BooleanToYNConverter.class)
    @Column(length = 2)
    private boolean salt;

    /**
     * 이름
     */
    @Column(length = 30)
    private String name;

    /**
     * 부서코드
     */
    @Column(name = "org_code", nullable = false,length = 200)
    private String orgCode;

    /**
     * 관리부서 코드
     */
    @Column(name = "mgr_org_code", nullable = false,length = 200)
    private String mgrOrgCode;

    /**
     * 접속 IP
     */
    @Column(name = "con_ip")
    private String conIp;

    /**
     * 이메일
     */
    @Column(name = "email")
    private String email;

    /**
     * 전화번호
     */
    @Column(name = "tel")
    private String tel;

    /**
     * 계정 사용 여부
     */
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "use_yn", length = 2, nullable = false)
    private boolean useYn;


    @Column
    private LocalDateTime lastLoginTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_code", updatable = false, insertable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mgr_org_code", updatable = false, insertable = false)
    private Organization mgrOrganization;

    @OneToMany(mappedBy = "admin")
    private List<VacationCountHistory> vacationCountHistories = new ArrayList<>();

    public void login() {

        this.lastLoginTime = LocalDateTime.now();
    }

    @Builder
    public Admin(String adminId, String password, boolean salt, String name, String orgCode, String mgrOrgCode, String conIp, String email, String tel) {
        this.adminId = adminId;
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.orgCode = orgCode;
        this.mgrOrgCode = mgrOrgCode;
        this.conIp = conIp;
        this.email = email;
        this.tel = tel;
        this.useYn = true;
    }

    public void changePassword(String pwd, boolean salt) {

        this.password = pwd;
        this.salt = salt;
    }


    public void use(boolean useYn) {
            this.useYn = useYn;
    }

    @Override
    public String getId() {
        return adminId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
