package com.gen.vacation.server.asset.dto;

import com.gen.vacation.global.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;

import javax.websocket.server.ServerEndpoint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-07-06
 * Time: 오후 5:46
 */
@Getter
@Setter
public class AssetExcelListResponseDto {

    private String assetId;
    private String category;
    private String modelName;
    private String userName;
    private String rankName;
    private String orgName;
    private LocalDate purchaseDate;
    private String status;
    private String note;
    private String createdAt;


    public AssetExcelListResponseDto(String assetId, String category, String modelName, String userName, String rankName, String orgName, LocalDate purchaseDate, String status, String note, LocalDateTime createdAt) {
        this.assetId = assetId;
        this.category = category;
        this.modelName = modelName;
        this.userName = userName;
        this.rankName = rankName;
        this.orgName = orgName;
        this.purchaseDate = purchaseDate;
        this.status = status;
        this.note = note;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
