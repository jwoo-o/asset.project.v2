package com.gen.vacation.server.asset.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gen.vacation.global.domain.entity.Asset;
import com.gen.vacation.global.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-07-05
 * Time: 오전 9:43
 */
@ToString
@Getter
@Setter
public class AssetRequestDto {

    private String assetId;
    private String category;
    private Long price;
    private String status;
    private String note;
    private String modelName;
    private String manufacture;
    private LocalDate purchaseDate;
    private String serialNumber;
    private Map<String,Object> assetInfo;
    private String orgCode;
    private String userId;



    public Asset toEntity() throws JsonProcessingException {

        return Asset.builder()
                .assetId(assetId)
                .category(category)
                .price(price)
                .modelName(modelName)
                .purchaseDate(purchaseDate)
                .manufacture(manufacture)
                .note(note)
                .status(status)
                .assetInfo(JsonUtil.dtoToString(assetInfo))
                .orgCode(orgCode)
                .userId(userId).build();
    }
}
