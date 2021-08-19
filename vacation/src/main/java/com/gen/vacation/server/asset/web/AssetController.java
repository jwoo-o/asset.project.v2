package com.gen.vacation.server.asset.web;

import com.gen.vacation.global.common.CommonResult;
import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.config.JwtTokenConfig;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.server.asset.dto.*;
import com.gen.vacation.server.asset.service.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-07-02
 * Time: 오전 10:14
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class AssetController {


    private final ResponseService responseService;

    private final AssetService assetService;

    private final JwtTokenConfig jwtTokenConfig;



    @GetMapping("/asset/next-id")
    public SingleResult<String> getNextId(@RequestParam(name = "category") String category) throws Exception {

        return responseService.getSingleResult(assetService.selNextId(category));

    }

    @PostMapping("/asset")
    public CommonResult postAsset(@RequestBody AssetRequestDto dto) throws Exception {

        assetService.insAsset(dto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/asset")
    public CommonResult putAsset(@RequestBody AssetRequestDto dto) throws Exception {

        assetService.updAsset(dto);
        return responseService.getSuccessResult();
    }

    @PostMapping("/asset/image")
    public CommonResult postAssetImage(AssetUploadDto dto) throws Exception {

        assetService.insAssetImage(dto);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/asset/image/{assetId}")
    public CommonResult deleteAssetImage(@PathVariable String assetId) throws Exception {

        assetService.delAssetImage(assetId);
        return responseService.getSuccessResult();
    }

    @GetMapping("/asset")
    public SingleResult<Map<String, Object>> getAsset(AssetSearchRequestDto dto) throws Exception {

        return responseService.getSingleResult(assetService.selAsset(dto));
    }

    @GetMapping("/asset/excel")
    public SingleResult<Map<String, Object>> getAssetToExcel(AssetSearchRequestDto dto) throws Exception {

        return responseService.getSingleResult(assetService.selAssetToExcel(dto));
    }

    @GetMapping("/asset/{assetId}")
    public SingleResult<AssetResponseDto> getAsset(@PathVariable String assetId) throws Exception {

        return responseService.getSingleResult(assetService.selAssetById(assetId));
    }

    @GetMapping("/asset/token/{assetId}")
    public SingleResult<AssetResponseDto> getAssetToToken(@PathVariable String assetId) throws Exception {

        return responseService.getSingleResult(assetService.selAssetTokenById(assetId));
    }

    @GetMapping("/asset/total")
    public SingleResult<Map<String, Object>> getAssetTotal(AssetSearchRequestDto dto) throws Exception {

        return responseService.getSingleResult(assetService.selAssetTotal(dto));
    }
}
