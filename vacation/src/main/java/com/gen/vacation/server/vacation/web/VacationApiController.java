package com.gen.vacation.server.vacation.web;

import com.gen.vacation.global.common.CommonResult;
import com.gen.vacation.global.common.ListResult;
import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.common.dto.MailSenderDto;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.global.util.CustomMailSenderUtil;
import com.gen.vacation.server.vacation.dto.*;
import com.gen.vacation.server.vacation.service.VacationDeadLineService;
import com.gen.vacation.server.vacation.service.VacationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class VacationApiController {

    private final ResponseService responseService;

    private final VacationService vacationService;

    private final VacationDeadLineService vacationDeadLineService;

    private final CustomMailSenderUtil mailSenderUtil;

    @GetMapping("/vacation/vacation-list")
    public SingleResult<Map<String, Object>> getVacationListAll(VacationSearchDto dto) throws Exception {

        return responseService.getSingleResult(vacationService.selVacationList(dto));

    }

    @GetMapping("/vacation/vacation-detail/{vacationId}")
    public SingleResult<Map<String, Object>> getVacationByVacationId(@PathVariable Long vacationId) throws Exception {

        return responseService.getSingleResult(vacationService.selVacationDetailByVacationId(vacationId));

    }

    @PostMapping("/vacation")
    public CommonResult postVacation(@RequestBody VacationListRequestDto dto) throws Exception {

        MailSenderDto senderDto = vacationService.insVacation(dto);
        mailSenderUtil.sendMail(senderDto);
        return responseService.getSuccessResult();
    }

    @PostMapping("/vacation/file")
    public ListResult<Long> postVacationFile(List<MultipartFile> files) throws Exception {

        return responseService.getListResult(vacationService.insVacationFiles(files));
    }

    @DeleteMapping("/vacation/file/{id}")
    public CommonResult deleteVacationFile(@PathVariable Long id) throws Exception {

        vacationService.delVacationFile(id);
        return responseService.getSuccessResult();
    }

    @PutMapping("/vacationApproval")
    public CommonResult putApprovalAllById(@RequestBody VacationApprovalRequestDto dto) throws Exception {

        MailSenderDto senderDto = vacationService.updApproval(dto);
        mailSenderUtil.sendMails(senderDto);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/vacation/vacation-delete/{vacationId}")
    public CommonResult deleteRoles(@PathVariable Long vacationId) throws Exception {

        vacationService.delVacationId(vacationId);
        return responseService.getSuccessResult();
    }

    @GetMapping("/vacation/years")
    public ListResult<String> getYears() throws Exception {

        return responseService.getListResult(vacationService.selYears());
    }

    @GetMapping("/vacation/years/org/{orgCode}")
    public ListResult<String> getYearsByOrgCode(@PathVariable String orgCode) throws Exception {

        return responseService.getListResult(vacationService.selYearsByOrgCode(orgCode));
    }

    @GetMapping("/vacation/years/{userId}")
    public ListResult<String> getYearsByUserId(@PathVariable String userId) throws Exception {

        return responseService.getListResult(vacationService.selYearsByUserId(userId));
    }

    @GetMapping("/vacation")
    public SingleResult<Map<String,Object>> getVacationInfoList(VacationSearchDto dto) throws Exception {

        return responseService.getSingleResult(vacationService.selUserList(dto));
    }
    @GetMapping("/vacation/calendar")
    public ListResult<CalendarResponseDto> getVacationCalendar(VacationSearchDto dto) throws Exception {

        return responseService.getListResult(vacationService.selVacationCalendar(dto));
    }
    @GetMapping("/vacation/{userId}")
    public SingleResult<Map<String,Object>> getVacationListByUserId(@PathVariable String userId,VacationSearchDto dto) throws Exception {

        return responseService.getSingleResult(vacationService.selVacationListByUserId(userId,dto));
    }

    @GetMapping("/vacation/date_check/{userId}/{startDay}/{endDay}/{vacationId}")
    public SingleResult<Boolean> getVacationDateCheck(@PathVariable String userId, @PathVariable String startDay, @PathVariable String endDay, @PathVariable String vacationId) throws Exception {

        return responseService.getSingleResult(vacationService.selVacationDateCheck(userId, startDay, endDay, vacationId));
    }


    @PostMapping("/vacation/cancel")
    public CommonResult postVacationCancel(@RequestBody VacationCancelDto dto) throws Exception {

        MailSenderDto senderDto = vacationService.updVacationCancel(dto);
        mailSenderUtil.sendMails(senderDto);
        return responseService.getSuccessResult();
    }


    @PutMapping("/vacation/total/{userId}")
    public CommonResult putTotalCntByUserId(@PathVariable String userId, @RequestBody TotalCntRequestDto dto, HttpServletRequest request) throws Exception {

        vacationService.updTotalCntByUserId(userId,dto,request);
        return responseService.getSuccessResult();
    }

    @PutMapping("/vacation/edit")
    public CommonResult putVacationEdit(@RequestBody VacationListRequestDto dto) throws Exception {

        vacationService.updVacation(dto);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/vacation/admin/delete")
    public CommonResult deleteAdminVacation(VacationAdminDeleteRequestDto dto) throws Exception {

        MailSenderDto senderDto = vacationService.delVacationAdminByVacationId(dto);
        mailSenderUtil.sendMails(senderDto);

        return responseService.getSuccessResult();
    }

    @GetMapping("/vacation/log/{userId}")
    public SingleResult<Map<String,Object>> getVacationHistoryListByUserId(@PathVariable String userId,VacationSearchDto dto) throws Exception {

        return responseService.getSingleResult(vacationService.selVacationHistoryListByUserId(userId,dto));
    }



    @GetMapping("/vacation/deadline")
    public SingleResult<Map<String,Object>> getVacationDeadline(VacationDeadlineRequestDto dto) throws Exception {

        return responseService.getSingleResult(vacationDeadLineService.selVacationDeadline(dto));
    }
    @PostMapping("/vacation/deadline/sender")
    public CommonResult postVacationDeadlineBySender(@RequestBody VacationDeadlineRequestDto dto) throws Exception {

        MailSenderDto senderDto = vacationDeadLineService.insVacationDeadlineByUser(dto);
        mailSenderUtil.sendMail(senderDto);

        return responseService.getSuccessResult();
    }

    @PostMapping("/vacation/deadline")
    public CommonResult postVacationDeadline(@RequestBody VacationDeadlineRequestDto dto) throws Exception {

        MailSenderDto senderDto = vacationDeadLineService.insVacationDeadline(dto);
        mailSenderUtil.sendMail(senderDto);

        return responseService.getSuccessResult();
    }

    @PatchMapping("/vacation/deadline")
    public CommonResult patchVacationDeadline(@RequestBody VacationDeadlineRequestDto dto) throws Exception {

        MailSenderDto senderDto = vacationDeadLineService.updVacationDeadlineByAdmin(dto);
        mailSenderUtil.sendMail(senderDto);

        return responseService.getSuccessResult();
    }


}
