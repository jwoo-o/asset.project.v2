package com.gen.vacation.server.asset.dto;

import com.gen.vacation.global.domain.entity.Asset;
import com.gen.vacation.global.util.JsonUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-07-06
 * Time: 오전 11:08
 */
@Getter
@Setter
@NoArgsConstructor
public class AssetResponseDto {

    private String assetId;
    private String category;
    private String manufacture;
    private String modelName;
    private String userName;
    private String userId;
    private String serialNumber;
    private Long price;
    private LocalDate purchaseDate;
    private String orgCode;
    private String orgFullName;
    private String note;
    private String status;
    private Map<String, Object> assetInfo;
    private List<Map<String, Object>> fileList;
    private String imgSrc;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public AssetResponseDto(Asset asset) {
        this.assetId = asset.getAssetId();
        this.category = asset.getCategory();
        this.manufacture = asset.getManufacture();
        this.modelName = asset.getModelName();
        this.userId = asset.getUserId();
        this.serialNumber = asset.getSerialNumber();
        this.price = asset.getPrice();
        this.purchaseDate = asset.getPurchaseDate();
        this.orgCode = asset.getOrgCode();
        this.note = asset.getNote();
        this.status = asset.getStatus();
        this.assetInfo = (Map<String, Object>) JsonUtil.stringToJson(asset.getAssetInfo());
        this.createdAt = asset.getCreatedAt();
        this.modifiedAt = asset.getModifiedAt();
    }

    public AssetResponseDto(String assetId, String category, String manufacture, String modelName, String userName, String serialNumber, Long price, LocalDate purchaseDate, String orgFullName, String note, String status, String assetInfo, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.assetId = assetId;
        this.category = category;
        this.manufacture = manufacture;
        this.modelName = modelName;
        this.userName = userName;
        this.serialNumber = serialNumber;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.orgFullName = orgFullName;
        this.note = note;
        this.status = status;
        this.assetInfo = (Map<String, Object>) JsonUtil.stringToJson(assetInfo);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
