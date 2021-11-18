package com.gen.vacation.server.vacation.service;

import com.gen.vacation.global.enums.*;
import com.gen.vacation.global.common.dto.MailSenderDto;
import com.gen.vacation.global.config.JwtTokenConfig;
import com.gen.vacation.global.domain.entity.*;
import com.gen.vacation.global.domain.entity.id.VacationInfoId;
import com.gen.vacation.global.domain.entity.repositorys.*;
import com.gen.vacation.global.exception.AlreadyProcessException;
import com.gen.vacation.global.exception.NotFoundApproverGroupException;
import com.gen.vacation.global.util.CommonUtil;
import com.gen.vacation.global.util.FileUploadUtil;
import com.gen.vacation.server.approver.dto.ApproverDto;
import com.gen.vacation.server.approver.dto.ApproversResponseDto;
import com.gen.vacation.server.user.dto.VacationInfoListResponseDto;
import com.gen.vacation.server.user.repository.AdminRepositorySupport;
import com.gen.vacation.server.user.repository.UserRepositorySupport;
import com.gen.vacation.server.vacation.dto.*;
import com.gen.vacation.server.vacation.repository.VacationCountHistoryRepositorySupport;
import com.gen.vacation.server.vacation.repository.VacationInfoRepositorySupport;
import com.gen.vacation.server.vacation.repository.VacationRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 2:17
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@Service
public class VacationService {


    private final VacationRepository vacationRepository;
    private final VacationRepositorySupport vacationRepositorySupport;
    private final VacationInfoRepositorySupport vacationInfoRepositorySupport;
    private final VacationInfoRepository vacationInfoRepository;
    private final ApproverRepository approverRepository;
    private final UserRepository userRepository;
    private final UserRepositorySupport userRepositorySupport;
    private final VacationFileRepository vacationFileRepository;
    private final FileUploadUtil fileUploadUtil;
    private final ApproverGroupRepository approverGroupRepository;

    private final AdminRepository adminRepository;
    private final AdminRepositorySupport adminRepositorySupport;

    private final VacationCountHistoryRepository vacationCountHistoryRepository;

    private final VacationCountHistoryRepositorySupport vacationCountHistoryRepositorySupport;

    private final JwtTokenConfig jwtTokenConfig;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Value("${file.uri.path}")
    private String resourcesUriPath;

    @Value("${server.servlet.context-path}")
    private String basicUrl;


    public Map<String, Object> selVacationList(@Valid VacationSearchDto dto) throws Exception {

        return vacationRepositorySupport.findAllBySearch(dto);
    }

    public MailSenderDto insVacation(@Valid VacationListRequestDto dto) throws Exception {

        User user = userRepository.findById(dto.getUserId()).orElseThrow(IllegalArgumentException::new);

        /** 해당 직위 결제라인 검색*/
        ApproverGroup approverGroup = approverGroupRepository
                .findByOrgCodeAndJobCodeAndUseYn(user.getOrgCode(), user.getJobCd(), true)
                .orElseThrow(NotFoundApproverGroupException::new);

        if(approverGroup.getApproverDetails().isEmpty()) {
            throw new NotFoundApproverGroupException();
        }

        Vacation vacation = vacationRepository.save(dto.toEntity());

        if (dto.getFileIds().length > 0) {
            vacationFileRepository.updateVacationFileVacationIdById(vacation.getVacationId(), Arrays.asList(dto.getFileIds()));
        }

        String nextTo = "";
        String nextId = "";

        /** 휴가 결제자 등록 */
        for (int i = 0; i < approverGroup.getApproverDetails().size(); i++) {
            ApproverDetail approverDetail = approverGroup.getApproverDetails().get(i);
            ApproverDto approverDto = new ApproverDto(approverDetail,vacation.getVacationId());
            approverRepository.save(approverDto.toEntity());
            if(i == 0) {
                nextTo = approverDetail.getEmail();
                nextId = approverDetail.getUserId();
            }
        }
        /** 결제자 승인요청 메일 전송 */
        MailSenderDto senderDto = getSenderDto(nextTo,user.getEmail(),vacation,ApprovalEnum.WAIT.getDesc());
        Map<String, Object> mailData = getMailData(vacation, user.getOrganization().getOrgName());
        mailData.put("subject", senderDto.getSubject());
        mailData.put("token",getToken(nextId));
        mailData.put("state",ApprovalEnum.WAIT.getValue());

        senderDto.setData(mailData);

        return senderDto;
    }

    public MailSenderDto updApproval(@Valid VacationApprovalRequestDto dto) throws Exception {


        Vacation vacation = vacationRepository.findById(dto.getVacationId()).orElseThrow(IllegalArgumentException::new);
        User user = vacation.getUser();
        List<Approver> approvers = approverRepository.findAllByVacationIdAndDivision(vacation.getVacationId(), DivisionEnum.approver);
        Approver approver = approvers.stream().filter(o -> o.getOrder() == dto.getOrderPosition()).findFirst().orElseThrow(IllegalArgumentException::new);

        ApproverFlagEnum approvalFlag = ApproverFlagEnum.REJECT;
        ApprovalEnum approveState = ApprovalEnum.REJECT;


        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();

        String from = user.getEmail();

        MailSenderDto senderDto;

        Map<String, Object> data = getMailData(vacation,user.getOrganization().getOrgName());



        /** 이미 처리되었는지 */
        if (!approver.getApprovalFlag().equals(ApproverFlagEnum.WAIT) || vacation.getOrderPosition() != dto.getOrderPosition()) {
            throw new AlreadyProcessException();
        }
        /** 대기중이거나, 진행중일때만 진행 */
        if (dto.getApproveState().equals(ApprovalEnum.WAIT.getValue()) || dto.getApproveState().equals(ApprovalEnum.ING.getValue())) {
            approvalFlag = ApproverFlagEnum.APPROVE;

            /** 최종 결제 완료 */
            if (dto.getOrderPosition() >= approvers.size() - 1) {
                approveState = ApprovalEnum.APPROVE;

                /** 경조사,기타 휴가가 아닌지*/
                if (!VacationKind.notAnnual(dto.getVacationKind())) {
                    VacationInfo vacationInfo = vacationInfoRepository.findByUserIdAndYears(vacation.getUserId(), vacation.getCreatedAt().getYear() + "").orElseThrow(IllegalArgumentException::new);
                    String vacationUseCnt = vacationInfo.getUseCnt();
                    vacationUseCnt = Double.toString(Double.parseDouble(vacationUseCnt) + Double.parseDouble(vacation.getCountDay()));
                    vacationUseCnt = new BigDecimal(vacationUseCnt).stripTrailingZeros().toPlainString();
                    vacationInfo.update(vacationUseCnt);
                }
                List<String> adminList = adminRepositorySupport.findEmailById("admin");
                cc.addAll(adminList);
                to.add(user.getEmail());

                /** 결제 진행 */
            } else {
                approveState = ApprovalEnum.ING;


                Approver nextApprover = approvers.stream().filter(o -> o.getOrder() == dto.getOrderPosition()+1).findFirst().orElseThrow(IllegalArgumentException::new);
                to.add(nextApprover.getEmail());

                data.put("state",ApprovalEnum.WAIT.getValue());
                data.put("token", getToken(nextApprover.getUserId()));
            }
            /** 반려일 때*/
        } else if (dto.getApproveState().equals(ApprovalEnum.REJECT.getValue())) {
            from = approver.getEmail();
            to.add(user.getEmail());
            data.put("rejectReason", dto.getRejectReason());

        }
        dto.setOrderPosition(dto.getOrderPosition() + 1);
        vacation.update(approveState, dto.getOrderPosition(), dto.getRejectReason());
        approver.update(approvalFlag);

        senderDto = getSenderDto(to,cc,from,vacation,approveState.equals(ApprovalEnum.ING) ? ApprovalEnum.WAIT.getDesc() : approveState.getDesc());

        data.put("subject", senderDto.getSubject());
        data.put("orderPosition", dto.getOrderPosition());


        senderDto.setData(data);


        return senderDto;
    }

    public MailSenderDto updVacationCancel(VacationCancelDto dto) throws Exception {

        Vacation vacation = vacationRepository.findById(dto.getVacationId()).orElseThrow(IllegalArgumentException::new);
        User user = vacation.getUser();
        vacation.updateState(ApprovalEnum.CANCEL);

        List<String> toList = adminRepository.findByAdminIdNotAndUseYn("admin", true);
        List<String> ccList = userRepositorySupport.findEmailByVacationId(dto.getVacationId());

        MailSenderDto senderDto = getSenderDto(toList,ccList,vacation.getUser().getEmail(),vacation,ApprovalEnum.CANCEL.getDesc());

        Map<String, Object> data = getMailData(vacation,user.getOrganization().getOrgName());
        data.put("subject", senderDto.getSubject());
        data.put("reason", dto.getReason());
        senderDto.setData(data);

        return senderDto;

    }

    public void updVacation(VacationListRequestDto dto) {

        if (dto.getFileIds() != null) {
            vacationFileRepository.updateVacationFileVacationIdById(dto.getVacationId(), Arrays.asList(dto.getFileIds()));
        }

        Vacation vacation = vacationRepository.findById(dto.getVacationId()).orElseThrow(IllegalArgumentException::new);
        vacation.update(dto);
    }

    public Map<String, Object> selVacationDetailByVacationId(Long vacationId) {
        Map<String, Object> result = new HashMap<>();


        Vacation vacation = vacationRepository.findByVacationId(vacationId).orElseThrow(IllegalArgumentException::new);

        List<Map<String, Object>> fileList = new ArrayList<>();

        for (int i = 0; i < vacation.getVacationFiles().size(); i++) {
            Map<String, Object> data = new HashMap<>();
            VacationFile vacationFile = vacation.getVacationFiles().get(i);
            data.put("name", vacationFile.getFileName());
            data.put("url", basicUrl + "/" + resourcesUriPath + "/vacation/" + vacationFile.getKey());
            data.put("id", vacationFile.getId());

            fileList.add(data);
        }

        VacationListResponseDto vacationListResponseDto = new VacationListResponseDto(vacation, fileList);

        result.put("vacationDetail", vacationListResponseDto);


        List<ApproversResponseDto> approverList = approverRepository.findAllByVacationIdAndDivisionOrderByOrderAsc(vacationId, DivisionEnum.approver).stream().map(ApproversResponseDto::new).collect(Collectors.toList());

        result.put("approverList", approverList);

        return result;
    }

    public void delVacationId(Long vacationId) throws Exception {
        List<VacationFile> vacationFiles = vacationFileRepository.findAllByVacationId(vacationId);
        for (VacationFile file : vacationFiles) {
            fileUploadUtil.fileDelete(file.getKey(), uploadDirectory + "/vacation");
            vacationFileRepository.delete(file);
        }
        approverRepository.deleteByVacationId(vacationId);
        vacationRepository.deleteById(vacationId);

    }

    public List<String> selYears() throws Exception {

        return vacationInfoRepository.findGroupByYears();
    }

    public List<String> selYearsByOrgCode(String orgCode) throws Exception {

        return vacationInfoRepositorySupport.findByOrgCodeGroupByYear(orgCode);
    }

    public Map<String, Object> selUserList(VacationSearchDto dto) throws Exception {

        return userRepositorySupport.findVacationInfos(dto);
    }

    public Map<String, Object> selVacationListByUserId(String userId, VacationSearchDto dto) throws Exception {

        VacationInfoListResponseDto vacationInfoListResponseDto = userRepositorySupport.findVacationInfo(userId, dto.getYear());

        dto.setSearchType("userId");
        dto.setSearchWord(userId);

        Map<String, Object> result = vacationRepositorySupport.findAllBySearch(dto);
        result.put("userInfo", vacationInfoListResponseDto);

        return result;
    }

    public List<String> selYearsByUserId(String userId) throws Exception {

        return vacationInfoRepository.findByUserIdGroupByYears(userId);
    }

    public List<Long> insVacationFiles(List<MultipartFile> files) throws Exception {

        List<Long> list = new ArrayList<>();
        for (MultipartFile file : files) {

            String key = fileUploadUtil.fileUpload(file, uploadDirectory + "/vacation");
            VacationFile vacationFile = vacationFileRepository.save(VacationFile.builder()
                    .fileName(file.getOriginalFilename())
                    .key(key)
                    .fileSize(file.getSize()).build());
            list.add(vacationFile.getId());
        }

        return list;
    }

    public void delVacationFile(Long id) throws Exception {

        VacationFile vacationFile = vacationFileRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        String key = vacationFile.getKey();
        vacationFileRepository.delete(vacationFile);
        fileUploadUtil.fileDelete(key, uploadDirectory + "/vacation");

    }

    public List<CalendarResponseDto> selVacationCalendar(VacationSearchDto dto) throws Exception {


        return vacationRepositorySupport.findCalendar(dto);
    }

    public boolean selVacationDateCheck(String userId, String startDay, String endDay, String vacationId) throws Exception {

        LocalDate dateStartDay = LocalDate.parse(startDay, DateTimeFormatter.ISO_DATE);
        LocalDate dateEndDay = LocalDate.parse(endDay, DateTimeFormatter.ISO_DATE);

        return vacationRepositorySupport.countByUserIdAndStartDayAndEndDay(userId, dateStartDay, dateEndDay, vacationId) > 0;
    }

    public void updTotalCntByUserId(String userId, TotalCntRequestDto dto, HttpServletRequest request) throws Exception {

        VacationInfoId id = new VacationInfoId(dto.getYear(), userId);
        VacationInfo vacationInfo = vacationInfoRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        String adminId = jwtTokenConfig.getAdminId(request);
        String accessIp = CommonUtil.getClientIpAddr(request);
        vacationCountHistoryRepository
                .save(VacationCountHistory.builder()
                        .adminId(adminId)
                        .userId(userId)
                        .changeCnt(vacationInfo.getTotalCnt()+" -> "+ dto.getTotalCnt())
                        .changeReason(dto.getChangeReason())
                        .pcIp(accessIp)
                        .writeType(LoginHistoryEnum.ADMIN)
                        .years(dto.getYear()).build()
                );
        vacationInfo.totalCntUpdate(dto.getTotalCnt());
    }


    public MailSenderDto delVacationAdminByVacationId(VacationAdminDeleteRequestDto dto) throws Exception {

        Vacation vacation = vacationRepository.findById(dto.getVacationId()).orElseThrow(IllegalArgumentException::new);

        User user = userRepository.findById(vacation.getUserId()).orElseThrow(IllegalArgumentException::new);
        if(dto.isCheck()) {
            if (!VacationKind.notAnnual(vacation.getVacationKind().getValue())) {
                VacationInfo vacationInfo = vacationInfoRepository.findByUserIdAndYears(dto.getUserId(), dto.getYear() + "").orElseThrow(IllegalArgumentException::new);
                String vacationUseCnt = vacationInfo.getUseCnt();
                vacationUseCnt = Double.toString(Double.parseDouble(vacationUseCnt) - Double.parseDouble(vacation.getCountDay()));
                vacationUseCnt = new BigDecimal(vacationUseCnt).stripTrailingZeros().toPlainString();
                vacationInfo.update(vacationUseCnt);
            }

            vacation.update(ApprovalEnum.CANCELSUCCESS);
        } else {
            vacation.update(ApprovalEnum.APPROVE);
        }
        List<String> toList = adminRepository.findByAdminIdNotAndUseYn("admin", true);
        List<String> ccList = userRepositorySupport.findEmailByVacationId(dto.getVacationId());

        MailSenderDto senderDto = getSenderDto(toList,ccList,vacation.getUser().getEmail(),vacation,
                vacation.getApproveState().equals(ApprovalEnum.CANCELSUCCESS) ? ApprovalEnum.CANCELSUCCESS.getDesc() : "취소요청 반려");
        Map<String, Object> data = getMailData(vacation, user.getOrganization().getOrgName());
        data.put("subject", senderDto.getSubject());
        senderDto.setData(data);

        return senderDto;
    }

    public Map<String,Object> selVacationHistoryListByUserId(String userId, VacationSearchDto dto) throws Exception {
        return vacationCountHistoryRepositorySupport.findAllBySearch(userId,dto);
    }

    private Map<String,Object> getMailData(Vacation vacation, String orgName) {

        Map<String, Object> mailData = new HashMap<>();

        mailData.put("name", vacation.getUserName());
        mailData.put("startDay", vacation.getStartDay());
        mailData.put("endDay", vacation.getEndDay());
        mailData.put("vacationKindDesc", vacation.getVacationKind().getDesc());
        mailData.put("vacationKind", vacation.getVacationKind().getKey());
        mailData.put("vacationReason", vacation.getVacationReason());
        mailData.put("vacationId", vacation.getVacationId());
        mailData.put("orderPosition", vacation.getOrderPosition());
        mailData.put("countDay", vacation.getCountDay());
        mailData.put("orgName", orgName);


        return mailData;
    }

    private MailSenderDto getSenderDto(String to, String from, Vacation vacation, String message) {

        return MailSenderDto.builder()
                .from(from)
                .subject("[SYSTEM] " + vacation.getUserName() + " - " + vacation.getVacationKind().getDesc() + " 휴가건 " + message)
                .template("mail-template")
                .to(to).build();
    }
    private MailSenderDto getSenderDto(List<String> to, List<String> cc,String from, Vacation vacation, String message) {

        return MailSenderDto.builder()
                .from(from)
                .subject("[SYSTEM] " + vacation.getUserName() + " - " + vacation.getVacationKind().getDesc() + " 휴가건 " + message)
                .template("mail-template")
                .tos(to.toArray(new String[to.size()]))
                .ccs(cc.toArray(new String[cc.size()])).build();
    }

    private String getToken(String userId) {

        Map<String,Object> data = new HashMap<>();
        data.put("id",userId);
        data.put("role",TokenEnum.USER.getAuthority());

        return jwtTokenConfig.createUserToken(data,3);

    }
}
