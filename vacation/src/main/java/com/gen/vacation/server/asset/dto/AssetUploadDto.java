package com.gen.vacation.server.asset.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-04-26
 * Time: 오후 3:49
 */
@Getter
@Setter
public class AssetUploadDto {

    private String assetId;
    private MultipartFile files;

}
