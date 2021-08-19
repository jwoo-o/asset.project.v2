package com.gen.vacation.server.organization.web;

import com.gen.vacation.global.common.CommonResult;
import com.gen.vacation.global.common.ListResult;
import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.server.organization.dto.OrganizationChartDto;
import com.gen.vacation.server.organization.dto.OrganizationChartRequestDto;
import com.gen.vacation.server.organization.dto.OrganizationChartUpdateRequestDto;
import com.gen.vacation.server.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-04
 * Time: 오후 12:59
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class OrganizationApiController {

    private final ResponseService responseService;

    private final OrganizationService organizationService;


    @GetMapping("/organization_charts/{orgCode}")
    public ListResult<Map<String, Object>> getOrganizationChartByLevel(@PathVariable String orgCode, @RequestParam("type") String type) throws Exception {


        return responseService.getListResult(organizationService.selOrganizationChartByLevel(orgCode, type));
    }

    @PostMapping("/organization_charts")
    public CommonResult postOrganizationChart(@RequestBody OrganizationChartRequestDto dto) throws Exception {

        organizationService.insOrganizationChart(dto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/organization_charts")
    public CommonResult putOrganizationChart(@RequestBody OrganizationChartUpdateRequestDto dto) throws Exception {

        organizationService.updOrganizationChart(dto);
        return responseService.getSuccessResult();
    }

    @GetMapping("/organization_charts/{orgCode}/check")
    public SingleResult<Boolean> getCheckOrganizationByOrgCode(@PathVariable String orgCode) throws Exception {


        return responseService.getSingleResult(organizationService.selOrganizationCheckOrgCode(orgCode));

    }

    @DeleteMapping("/organization_charts/{orgCode}")
    public CommonResult deleteOrganizationChart(@PathVariable String orgCode) throws Exception {

        organizationService.delOrganizationChart(orgCode);
        return responseService.getSuccessResult();
    }

    @GetMapping("/organization_chart/batch")
    public CommonResult batch() throws Exception  {

        organizationService.updOrganizationBatch();
        return responseService.getSuccessResult();
    }
    @GetMapping("/organization_charts/code")
    public SingleResult<String> getCode() throws Exception  {

        return responseService.getSingleResult(organizationService.selOrganizationByMaxCode());
    }

    @GetMapping("/organization_charts/order/{order}")
    public ListResult<OrganizationChartDto> getCode(@PathVariable int order) throws Exception  {

        return responseService.getListResult(organizationService.selOrganizationByOrder(order));
    }


}
