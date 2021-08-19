package com.gen.vacation.server.approver.web;

import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.server.approver.service.ApproverService;
import com.gen.vacation.server.vacation.dto.VacationSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApproverApiController {

    private final ResponseService responseService;

    private final ApproverService approverService;

    @GetMapping("/approverVacationList")
    public SingleResult<Map<String, Object>> getApproverList(@Valid VacationSearchDto dto) throws Exception {

        return responseService.getSingleResult(approverService.selApproverVacationList(dto));

    }

    @GetMapping("/approver/order-check/{userId}/{vacationId}/{orderPosition}")
    public SingleResult<Boolean> getApproverList(@PathVariable String userId, @PathVariable Long vacationId, @PathVariable int orderPosition) throws Exception {

        return responseService.getSingleResult(approverService.selApproverListAndOrderCheck(userId, vacationId, orderPosition));

    }

    @GetMapping("/approver/approvalCount/{userId}")
    public SingleResult<Long> getUserApprovalCountByUserId(@PathVariable String userId) throws Exception {


        return responseService.getSingleResult(approverService.selUserApprovalCountByUserId(userId));

    }

    @GetMapping("approver/list")
    public SingleResult<Long> getUserApprovalList(@PathVariable String userId) throws Exception {


        return responseService.getSingleResult(approverService.selUserApprovalCountByUserId(userId));

    }


}
