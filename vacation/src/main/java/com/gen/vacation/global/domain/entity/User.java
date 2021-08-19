package com.gen.vacation.global.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.domain.common.BooleanToYNConverter;
import com.gen.vacation.server.user.dto.UserRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity(name = "tb_user")
@Table(name = "tb_user")
public class User extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(length = 20, name = "user_id")
    private String userId;

    @Column(name = "user_name", length = 30, nullable = false)
    private String userName;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "org_code", nullable = false,length = 200)
    private String orgCode;

    @Column(name = "rank_cd")
    private String rankCd;

    @Column(name = "rank_nm")
    private String rankNm;

    @Column(name = "job_cd")
    private String jobCd;

    @Column(name = "job_nm")
    private String jobNm;


    @Column(name = "tel")
    private String tel;

    @Column(name = "seat_id")
    private Long seatId;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "use_yn", length = 2, nullable = false)
    private boolean useYn;

    @Column(length = 2)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean salt;

    @Column
    private LocalDateTime lastLoginTime;

    @Column
    private LocalDate hireDate;

    @Column
    private LocalDate leaveDate;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "asset_admin",length = 2)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean assetAdmin;

    @Column(name = "mgr_org_code",length = 200)
    private String mgrOrgCode;

    @Column(name = "admin",length = 2)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean admin;

    /** 내선 번호 */
    @Column(name = "ex")
    private String ex;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_code", updatable = false, insertable = false)
    private Organization organization;

    @OneToMany(mappedBy = "user")
    private List<VacationInfo> vacationInfos = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Asset> assets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<VacationCountHistory> vacationCountHistories = new ArrayList<>();


    @Builder
    public User(String userId, String password, String name, String orgCode, String rankCd, String rankNm, String email, String tel,boolean salt,LocalDate hireDate,String jobCd, String jobNm,Long seatId,boolean admin,String ex,boolean assetAdmin,String mgrOrgCode) {
        this.userId = userId;
        this.password = password;
        this.userName = name;
        this.orgCode = orgCode;
        this.rankCd = rankCd;
        this.rankNm = rankNm;
        this.jobCd = jobCd;
        this.jobNm = jobNm;
        this.email = email;
        this.tel = tel;
        this.salt = salt;
        this.useYn = true;
        this.hireDate = hireDate;
        this.seatId = seatId;
        this.admin = admin;
        this.ex = ex;
        this.assetAdmin = assetAdmin;
        this.mgrOrgCode = mgrOrgCode;
    }


    @Override
    public String getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public void login() {

        this.lastLoginTime = LocalDateTime.now();
    }
    public void update(UserRequestDto dto) {

        this.userName = dto.getName();
        this.orgCode = dto.getOrgCode();
        this.rankCd = dto.getRankCd();
        this.rankNm = dto.getRankNm();
        this.jobCd = dto.getJobCd();
        this.jobNm = dto.getJobNm();
        this.tel = dto.getTel();
        this.seatId = dto.getSeatId();
        this.admin = dto.isAdmin();
        this.ex = dto.getEx();
        this.assetAdmin = dto.isAssetAdmin();
        this.mgrOrgCode = dto.getMgrOrgCode();
        this.hireDate = dto.getHireDate().toLocalDateTime().toLocalDate();
    }

    public void imageUpload(String profileImage) {
        this.profileImage = profileImage;
    }
    public void changePassword(String pwd, boolean salt) {

        this.password = pwd;
        this.salt = salt;
    }

    public void delete(LocalDate leaveDate) {

        this.useYn = false;
        this.leaveDate = leaveDate;
    }


}
