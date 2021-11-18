package com.gen.vacation.server.user.dto;

import com.gen.vacation.global.domain.entity.Admin;
import com.gen.vacation.global.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-12
 * Time: 오후 5:44
 */
@Getter
@Setter
@ToString
public class UserRequestDto {

    @NotBlank(message = "")
    private String userId;
    @NotBlank(message = "")
    private String name;
    private String password;
    @NotBlank(message = "")
    private String rankCd;
    @NotBlank(message = "")
    private String rankNm;
    @NotBlank(message = "")
    private String jobCd;
    @NotBlank(message = "")
    private String jobNm;
    @NotBlank(message = "")
    private String orgCode;
    @NotNull(message = "")
    private String tel;
    @NotNull(message = "")
    private String email;
    private boolean salt;
    @NotNull(message = "")
    private Timestamp hireDate;
    @NotNull(message = "")
    private Timestamp birthDate;
    @NotNull(message = "")
    private String address;
    private String address1;
    private String responsibilities;
    private Long seatId;
    private boolean admin;
    private boolean assetAdmin;
    private String mgrOrgCode;
    private String ex;


    public User toEntity() {
        return User.builder()
                .userId(userId)
                .name(name)
                .orgCode(orgCode)
                .password(password)
                .email(email)
                .rankCd(rankCd)
                .rankNm(rankNm)
                .jobCd(jobCd)
                .jobNm(jobNm)
                .salt(salt)
                .hireDate(hireDate.toLocalDateTime().toLocalDate())
                .birthDate(birthDate.toLocalDateTime().toLocalDate())
                .responsibilities(responsibilities)
                .address(address)
                .address1(address1)
                .tel(tel)
                .seatId(seatId)
                .admin(admin)
                .assetAdmin(assetAdmin)
                .mgrOrgCode(mgrOrgCode)
                .ex(ex)
                .build();
    }

    public Admin adminToEntity(){
        return Admin.builder()
                        .adminId(userId)
                        .email(email)
                        .mgrOrgCode("00000000")
                        .name(name)
                        .orgCode(orgCode)
                        .password(password)
                        .salt(salt)
                        .tel(tel).build();
    }


}
