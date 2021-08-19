package com.gen.vacation.server.vacation.dto;

import com.gen.vacation.global.Enum.LoginHistoryEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-08-03
 * Time: 오전 10:03
 */
@Getter
@Setter
public class VacationCountHistoryRequestDto {

    private Long id;
    private String changeCnt;
    private String changeReason;
    private String writeType;
    private String adminName;
    private String pcIp;
    private LocalDateTime modifiedAt;


    public VacationCountHistoryRequestDto(Long id, String changeCnt,String changeReason, LoginHistoryEnum writeType, String adminName, String pcIp, LocalDateTime modifiedAt) {
        this.id = id;
        this.changeCnt = changeCnt;
        this.changeReason = changeReason;
        this.writeType = writeType.getKey();
        this.adminName = adminName;
        this.pcIp = pcIp;
        this.modifiedAt = modifiedAt;
    }
}
