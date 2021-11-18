package com.gen.vacation.global.domain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.util.JsonUtil;
import com.gen.vacation.server.asset.dto.AssetRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-06-30
 * Time: 오전 11:03
 */
@Getter
@NoArgsConstructor
@Entity(name = "tb_asset")
@Table(name = "tb_asset",indexes = {
        @Index(columnList = "modified_at")
})
public class Asset extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(length = 20, name = "asset_id")
    private String assetId;
    @Column(length = 10, name = "category", nullable = false)
    private String category;
    @Column(length = 10, name = "status", nullable = false)
    private String status;
    @Column(name = "model_name")
    private String modelName;
    @Column(name = "manufacture")
    private String manufacture;
    @Column(name = "serialNumber")
    private String serialNumber;
    @Column(length = 20, name = "user_id",nullable = false)
    private String userId;
    @Column(name = "org_code", nullable = false,length = 200)
    private String orgCode;
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    @Column(name = "price")
    private Long price;
    @Column(name = "note",length = 2048)
    private String note;
    @Column(length = 2048, name = "asset_info")
    private String assetInfo;

    @Column(name = "pc_id", length = 200,unique = true)
    private String pcId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_code", updatable = false, insertable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @OneToMany(mappedBy = "asset")
    private List<AssetFile> assetFiles = new ArrayList<>();

    @Builder
    public Asset(String assetId, String category, String status, String modelName, String manufacture, String serialNumber, String userId, String orgCode, LocalDate purchaseDate, Long price, String note, String assetInfo, String pcId) {
        this.assetId = assetId;
        this.category = category;
        this.status = status;
        this.modelName = modelName;
        this.manufacture = manufacture;
        this.serialNumber = serialNumber;
        this.userId = userId;
        this.orgCode = orgCode;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.note = note;
        this.assetInfo = assetInfo;
        this.pcId = pcId;
    }

    public void update(AssetRequestDto dto) throws JsonProcessingException {

        this.manufacture = dto.getManufacture();
        this.modelName = dto.getModelName();
        this.serialNumber = dto.getSerialNumber();
        this.orgCode = dto.getOrgCode();
        this.userId = dto.getUserId();
        this.assetInfo = JsonUtil.dtoToString(dto.getAssetInfo());
        this.note = dto.getNote();
        this.price = dto.getPrice();
        this.purchaseDate = dto.getPurchaseDate();
        this.status = dto.getStatus();
    }

    public void userUpdate(User user) {

        this.userId = user.getUserId();
        this.orgCode = user.getOrgCode();
    }

    @Override
    public String getId() {
        return assetId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
