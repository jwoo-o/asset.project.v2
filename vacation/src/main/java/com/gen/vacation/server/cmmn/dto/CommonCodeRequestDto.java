package com.gen.vacation.server.cmmn.dto;

import com.gen.vacation.global.domain.entity.CommonCodeGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-10
 * Time: 오후 1:06
 */
@Getter
@Setter
@ToString
public class CommonCodeRequestDto {

    @NotBlank(message = "")
    private String groupCode;
    private String groupDesc;
    @NotBlank(message = "")
    private String groupName;
    @NotNull(message = "")
    private Boolean useYn;
    private List<CommonDetailCodeRequestDto> detailCodes;


    public CommonCodeGroup toEntity() {

        return CommonCodeGroup.builder()
                .groupCode(groupCode)
                .groupDesc(groupDesc)
                .groupName(groupName)
                .useYn(useYn).build();

    }
}
