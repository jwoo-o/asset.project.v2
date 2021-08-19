package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-08-26
 * Time: 오후 1:20
 */
@NoArgsConstructor
@Getter
@Entity(name = "tb_asset_file")
public class AssetFile extends BaseEntity {

    /**
     * 파일이름
     */
    @Column
    private String fileName;

    /**
     * 업로드된  파일이름
     */
    @Column
    private String key;

    /**
     * 파일 사이즈
     */
    @Column
    private Long fileSize;

    @Column(name = "asset_id")
    private String assetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", updatable = false, insertable = false)
    private Asset asset;

    @Builder
    public AssetFile(String fileName, String key, Long fileSize, String assetId) {
        this.fileName = fileName;
        this.key = key;
        this.assetId = assetId;
        this.fileSize = fileSize;
    }

    public void update(String assetId){
        this.assetId = assetId;
    }
}
