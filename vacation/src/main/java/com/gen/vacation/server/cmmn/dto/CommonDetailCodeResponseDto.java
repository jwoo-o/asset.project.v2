package com.gen.vacation.server.cmmn.dto;

import com.gen.vacation.global.domain.entity.CommonCodeDetail;
import lombok.Getter;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-10
 * Time: 오전 9:34
 */
@Getter
public class CommonDetailCodeResponseDto {

    private String detailCode;
    private String detailName;
    private String detailDesc;
    private String groupCode;
    private int order;

    public CommonDetailCodeResponseDto(CommonCodeDetail detail) {

        this.detailCode = detail.getId().getDetailCode();
        this.detailName = detail.getDetailName();
        this.detailDesc = detail.getDetailDesc();
        this.groupCode = detail.getId().getGroupCode();
        this.order = detail.getOrder();
    }
}
