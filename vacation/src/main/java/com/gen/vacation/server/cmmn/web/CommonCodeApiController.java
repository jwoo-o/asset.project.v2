package com.gen.vacation.server.cmmn.web;

import com.gen.vacation.global.common.CommonResult;
import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.server.cmmn.dto.CommonCodeRequestDto;
import com.gen.vacation.server.cmmn.dto.CommonGroupCodeResponseDto;
import com.gen.vacation.server.cmmn.service.CommonCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오전 10:57
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class CommonCodeApiController {

    private final ResponseService responseService;

    private final CommonCodeService commonCodeService;

    @GetMapping("/common-code")
    public SingleResult<Map<String, Object>> getCommonCode(@RequestParam("query") String query) throws Exception {

        return responseService.getSingleResult(commonCodeService.selCommonCodeFind(query));
    }

    @GetMapping("/common")
    public SingleResult<Map<String, Object>> getCommonList(SearchRequestDto dto) throws Exception {

        return responseService.getSingleResult(commonCodeService.selCommonGroupCode(dto));
    }

    @GetMapping("/common/{groupCode}")
    public SingleResult<CommonGroupCodeResponseDto> getCommonList(@PathVariable String groupCode) throws Exception {

        return responseService.getSingleResult(commonCodeService.selCommonDetailCode(groupCode));
    }

    @GetMapping("/common/{groupCode}/check")
    public SingleResult<Boolean> getCommonCheck(@PathVariable String groupCode) throws Exception {

        return responseService.getSingleResult(commonCodeService.selCommonGroupCodeCheck(groupCode));
    }

    @PostMapping("/common")
    public CommonResult postCommonCode(@RequestBody CommonCodeRequestDto dto) throws Exception {

        commonCodeService.insCommonGroupCode(dto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/common")
    public CommonResult putCommonCode(@RequestBody CommonCodeRequestDto dto) throws Exception {

        commonCodeService.updCommonGroupCode(dto);
        return responseService.getSuccessResult();
    }


}
