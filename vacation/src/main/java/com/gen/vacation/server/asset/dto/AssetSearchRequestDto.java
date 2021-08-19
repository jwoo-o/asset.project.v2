package com.gen.vacation.server.asset.dto;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-07-05
 * Time: 오후 1:46
 */
@Getter
@Setter
@ToString
public class AssetSearchRequestDto extends SearchRequestDto {

    private String userName;
    private String category;
    private String status;
    private String note;
    private String rankCd;
    private String assetId;
    private String startDate;
    private String endDate;
}
