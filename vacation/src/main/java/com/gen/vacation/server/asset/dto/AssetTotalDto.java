package com.gen.vacation.server.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-07-12
 * Time: 오전 10:10
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetTotalDto {

    private String name;
    private Long value;

    public AssetTotalDto(Map.Entry<String, Long> map) {

        this.name = map.getKey();
        this.value = map.getValue();
    }
}
