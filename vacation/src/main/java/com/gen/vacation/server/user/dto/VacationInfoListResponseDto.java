package com.gen.vacation.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-04-27
 * Time: 오전 11:06
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class VacationInfoListResponseDto {

    private String userId;
    private String userName;
    private String orgName;
    private boolean userDeadline;
    private boolean sender;
    private boolean adminDeadline;
    private String rankNm;
    private String jobNm;
    private LocalDate hireDate;
    private LocalDate leaveDate;
    private boolean useYn;
    private String email;
    private String vacationTotalCnt;
    private String vacationUseCnt;

    public VacationInfoListResponseDto(String userId, String userName, String orgName, boolean userDeadline, boolean sender, boolean adminDeadline, String rankNm, String jobNm, LocalDate hireDate, LocalDate leaveDate, boolean useYn, String vacationTotalCnt, String vacationUseCnt) {
        this.userId = userId;
        this.userName = userName;
        this.orgName = orgName;
        this.userDeadline = userDeadline;
        this.sender = sender;
        this.adminDeadline = adminDeadline;
        this.rankNm = rankNm;
        this.jobNm = jobNm;
        this.hireDate = hireDate;
        this.leaveDate = leaveDate;
        this.useYn = useYn;
        this.vacationTotalCnt = vacationTotalCnt;
        this.vacationUseCnt = vacationUseCnt;
    }

    public VacationInfoListResponseDto(String userId, String userName, String orgName, String rankNm, String jobNm, LocalDate hireDate, String email,boolean useYn, String vacationTotalCnt, String vacationUseCnt) {
        this.userId = userId;
        this.userName = userName;
        this.orgName = orgName;
        this.rankNm = rankNm;
        this.jobNm = jobNm;
        this.hireDate = hireDate;
        this.email = email;
        this.useYn = useYn;
        this.vacationTotalCnt = vacationTotalCnt;
        this.vacationUseCnt = vacationUseCnt;
    }
}
