package com.gen.vacation.server.cmmn.dto;

import com.gen.vacation.global.domain.entity.CommonCodeDetail;
import com.gen.vacation.global.domain.entity.id.CommonCodeDetailId;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-09
 * Time: 오후 5:53
 */
@Getter
@Setter
public class CommonDetailCodeRequestDto {

    private String detailCode;
    private String detailName;
    private String detailDesc;
    private String groupCode;
    private CommonCodeDetailId id;
    private int order;

    public CommonCodeDetail toEntity() {

        return CommonCodeDetail.builder()
                .id(new CommonCodeDetailId(detailCode, groupCode))
                .detailName(detailName)
                .detailDesc(detailDesc)
                .order(order).build();
    }
}
