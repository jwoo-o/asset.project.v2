package com.gen.vacation.server.cmmn.dto;

import com.gen.vacation.global.domain.entity.CommonCodeGroup;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 6:49
 */
@Getter
@Setter
public class CommonGroupCodeResponseDto {

    private String groupCode;
    private String groupDesc;
    private String groupName;
    private LocalDateTime modifiedAt;
    private List<CommonDetailCodeResponseDto> detailCodes;
    private boolean useYn;


    public CommonGroupCodeResponseDto(String groupCode, String groupDesc, String groupName, LocalDateTime modifiedAt) {
        this.groupCode = groupCode;
        this.groupDesc = groupDesc;
        this.groupName = groupName;
        this.modifiedAt = modifiedAt;
    }

    public CommonGroupCodeResponseDto(CommonCodeGroup group) {
        this.groupCode = group.getGroupCode();
        this.groupDesc = group.getGroupDesc();
        this.groupName = group.getGroupName();
        this.detailCodes = group.getCommonCodeDetails().stream().map(CommonDetailCodeResponseDto::new).collect(Collectors.toList());
        this.useYn = group.isUseYn();
    }
}
