package com.gen.vacation.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponseDto {
    private String userId;

    private String userName;

    private String email;

    private String orgCode;

    private String orgName;

    private String rankCd;

    private String rankNm;

    private String jobCd;

    private String jobNm;

    private String tel;

    private LocalDate hireDate;

    private LocalDate leaveDate;

    private boolean useYn;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public UserListResponseDto(String userId, String userName, String email, String orgCode, String orgName, String rankCd, String rankNm, String jobCd, String jobNm, String tel, LocalDate hireDate, boolean useYn, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.rankCd = rankCd;
        this.rankNm = rankNm;
        this.jobCd = jobCd;
        this.jobNm = jobNm;
        this.tel = tel;
        this.hireDate = hireDate;
        this.useYn = useYn;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
