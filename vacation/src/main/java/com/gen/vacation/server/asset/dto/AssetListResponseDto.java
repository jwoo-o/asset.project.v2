package com.gen.vacation.server.asset.dto;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-07-05
 * Time: 오후 1:50
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetListResponseDto {

    private String assetId;
    private String userId;
    private String userName;
    private String orgCode;
    private String orgName;
    private String rankName;
    private String category;
    private String modelName;
    private LocalDate purchaseDate;
    private String note;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
