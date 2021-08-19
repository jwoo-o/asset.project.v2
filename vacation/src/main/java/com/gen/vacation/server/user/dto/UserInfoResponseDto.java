package com.gen.vacation.server.user.dto;

import com.gen.vacation.global.domain.entity.User;
import com.gen.vacation.global.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-13
 * Time: 오후 1:32
 */
@Getter
@Setter
public class UserInfoResponseDto {

    private String userId;
    private String name;
    private String orgCode;
    private String orgName;
    private String rankCd;
    private String rankNm;
    private String jobCd;
    private String jobNm;
    private String tel;
    private String email;
    private LocalDate hireDate;
    private String imgSrc;
    private String fullCode;
    private String fullName;
    private Long seatId;
    private boolean admin;
    private boolean assetAdmin;
    private String mgrOrgCode;
    private String ex;

    public UserInfoResponseDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getUserName();
        this.orgCode = user.getOrgCode();
        this.orgName = user.getOrganization().getOrgName();
        this.rankCd = user.getRankCd();
        this.rankNm = user.getRankNm();
        this.jobCd = user.getJobCd();
        this.jobNm = user.getJobNm();
        this.tel = user.getTel();
        this.email = user.getEmail();
        this.hireDate = user.getHireDate();
        this.imgSrc = CommonUtil.checkDefaultNull(user.getProfileImage(),"");
        this.fullCode = user.getOrganization().getOrgFullCode();
        this.fullName = user.getOrganization().getOrgFullName();
        this.seatId = user.getSeatId();
        this.admin = user.isAdmin();
        this.assetAdmin = user.isAssetAdmin();
        this.mgrOrgCode = user.getMgrOrgCode();
        this.ex = user.getEx();

    }
}
