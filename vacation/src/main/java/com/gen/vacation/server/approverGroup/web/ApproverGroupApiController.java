package com.gen.vacation.server.approverGroup.web;

import com.gen.vacation.global.common.CommonResult;
import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.server.approverGroup.dto.ApproverGroupRequestDto;
import com.gen.vacation.server.approverGroup.dto.ApproverGroupResponseDto;
import com.gen.vacation.server.approverGroup.service.ApproverGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ApproverGroupApiController {

    private final ResponseService responseService;

    private final ApproverGroupService approverGroupService;


    @GetMapping("/approverGroup")
    public SingleResult<Map<String, Object>> getApproverList(SearchRequestDto dto) throws Exception {

        return responseService.getSingleResult(approverGroupService.selApproverGroup(dto));
    }
    @DeleteMapping("/approverGroup/{id}")
    public CommonResult deleteApprover(@PathVariable Long id) throws Exception {

        approverGroupService.delApproverGroup(id);
        return responseService.getSuccessResult();
    }

    @GetMapping("/approver/{approverGroupCode}")
    public SingleResult<ApproverGroupResponseDto> getApproverList(@PathVariable Long approverGroupCode) throws Exception {

        return responseService.getSingleResult(approverGroupService.selApproverDetail(approverGroupCode));
    }

    @GetMapping("/approver/user/{orgCode}/{jobCode}")
    public SingleResult<ApproverGroupResponseDto> getApproverListByOrgCode(@PathVariable String orgCode, @PathVariable String jobCode) throws Exception {

        return responseService.getSingleResult(approverGroupService.selApproverDetailByOrgCode(orgCode, jobCode));
    }

    @GetMapping("/approver/{orgCode}/{jobCode}/check")
    public SingleResult<Boolean> getApproverCheck(@PathVariable String orgCode, @PathVariable String jobCode) throws Exception {

        return responseService.getSingleResult(approverGroupService.selApproverGroupCheck(orgCode, jobCode));
    }

    @PostMapping("/approver")
    public CommonResult postApprover(@RequestBody ApproverGroupRequestDto dto) throws Exception {

        approverGroupService.insApproverGroup(dto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/approver")
    public CommonResult putApprover(@RequestBody ApproverGroupRequestDto dto) throws Exception {

        /*approverGroupService.insApproverGroup(dto);*/
        approverGroupService.updApproverGroup(dto);
        return responseService.getSuccessResult();
    }

    @GetMapping("/approver/approver-check/{userId}")
    public SingleResult<Boolean> getApproverCheck(@PathVariable String userId) throws Exception {

        return responseService.getSingleResult(approverGroupService.selApproverCheck(userId));

    }
}
