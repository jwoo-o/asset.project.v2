package com.gen.vacation.server.vacation.service;

import com.gen.vacation.global.Enum.*;
import com.gen.vacation.global.common.dto.MailSenderDto;
import com.gen.vacation.global.config.JwtTokenConfig;
import com.gen.vacation.global.domain.entity.*;
import com.gen.vacation.global.domain.entity.id.VacationDeadlineId;
import com.gen.vacation.global.domain.entity.id.VacationInfoId;
import com.gen.vacation.global.domain.entity.repositorys.*;
import com.gen.vacation.global.exception.AlreadyProcessException;
import com.gen.vacation.global.exception.CommonFailException;
import com.gen.vacation.global.exception.NotFoundApproverGroupException;
import com.gen.vacation.global.util.CommonUtil;
import com.gen.vacation.global.util.FileUploadUtil;
import com.gen.vacation.global.util.JsonUtil;
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
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    private final VacationDeadlineRepository vacationDeadlineRepository;

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

        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException());

        ApproverGroup approverGroup = approverGroupRepository.findByOrgCodeAndJobCodeAndUseYn(user.getOrgCode(), user.getJobCd(), true).orElseThrow(() -> new NotFoundApproverGroupException());

        Vacation vacation = vacationRepository.save(dto.toEntity());
        if (dto.getFileIds().length > 0) {
            vacationFileRepository.updateVacationFileVacationIdById(vacation.getVacationId(), Arrays.asList(dto.getFileIds()));
        }

        for (int i = 0; i < approverGroup.getApproverDetails().size(); i++) {
            ApproverDetail approverDetail = approverGroup.getApproverDetails().get(i);

            approverRepository.save(Approver.builder()
                    .approvalFlag(ApproverFlagEnum.WAIT)
                    .approverDetailCode(approverDetail.getApproverDetailCode())
                    .approverGroupCode(approverDetail.getApproverGroupCode())
                    .division(approverDetail.getDivision())
                    .jobName(approverDetail.getJobName())
                    .order(approverDetail.getOrder())
                    .orgCode(approverDetail.getOrgCode())
                    .orgName(approverDetail.getOrgName())
                    .rankCd(approverDetail.getRankCd())
                    .rankNm(approverDetail.getRankNm())
                    .userId(approverDetail.getUserId())
                    .userName(approverDetail.getUserName())
                    .vacationId(vacation.getVacationId())
                    .email(approverDetail.getEmail())
                    .build());
        }
        MailSenderDto senderDto = new MailSenderDto();

        /** 상신 할 결제라인 이메일*/
        senderDto.setTo(approverGroup.getApproverDetails().get(0).getEmail());

        /** 본인 이메일 */
        senderDto.setFrom(user.getEmail());
        senderDto.setSubject("[SYSTEM] " + vacation.getUserName() + " - " + vacation.getVacationKind().getDesc() + " 휴가건 " + ApprovalEnum.WAIT.getDesc());
        Map<String, Object> mailData = new HashMap<>();
        mailData.put("id", approverGroup.getApproverDetails().get(0).getUserId());
        mailData.put("role", TokenEnum.USER.getAuthority());
        mailData.put("subject", senderDto.getSubject());
        mailData.put("name", vacation.getUserName());
        mailData.put("startDay", vacation.getStartDay());
        mailData.put("endDay", vacation.getEndDay());
        mailData.put("vacationKindDesc", vacation.getVacationKind().getDesc());
        mailData.put("vacationKind", vacation.getVacationKind().getKey());
        mailData.put("vacationReason", vacation.getVacationReason());
        mailData.put("vacationId", vacation.getVacationId());
        mailData.put("orderPosition", vacation.getOrderPosition());
        mailData.put("orgName", user.getOrganization().getOrgName());
        mailData.put("countDay", vacation.getCountDay());

        String token = jwtTokenConfig.createUserToken(mailData,3);
        mailData.put("token", token);
        senderDto.setData(mailData);
        senderDto.setTemplate("mail-template");


        return senderDto;
    }

    public MailSenderDto updApproval(@Valid VacationApprovalRequestDto dto) throws Exception {
        Long vacationId = dto.getVacationId();
        Vacation vacation = vacationRepository.findById(dto.getVacationId()).orElseThrow(() -> new IllegalArgumentException());
        User user = userRepository.findById(vacation.getUserId()).orElseThrow(() -> new IllegalArgumentException());
        Approver approver = approverRepository.findAllByVacationIdAndDivisionAndOrder(vacationId, "approver", dto.getOrderPosition());
        int approverCount = approverRepository.countByVacationIdAndDivision(vacationId, "approver");
        ApproverFlagEnum approvalFlag = ApproverFlagEnum.REJECT;
        ApprovalEnum approveState = ApprovalEnum.REJECT;


        MailSenderDto senderDto = new MailSenderDto();
        senderDto.setTemplate("mail-template");
        Map<String, Object> data = new HashMap<>();
        data.put("startDay", vacation.getStartDay());
        data.put("endDay", vacation.getEndDay());
        data.put("name", vacation.getUserName());
        data.put("vacationKindDesc", vacation.getVacationKind().getDesc());
        data.put("vacationKind", vacation.getVacationKind().getKey());
        data.put("vacationReason", vacation.getVacationReason());
        data.put("orgName", user.getOrganization().getOrgName());
        data.put("countDay", vacation.getCountDay());


        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        if (!approver.getApprovalFlag().equals(ApproverFlagEnum.WAIT) || vacation.getOrderPosition() != dto.getOrderPosition()) {
            throw new AlreadyProcessException();
        }
        if (dto.getApproveState().equals(ApprovalEnum.WAIT.getValue()) || dto.getApproveState().equals(ApprovalEnum.ING.getValue())) {
            approvalFlag = ApproverFlagEnum.APPROVE;
            approveState = ApprovalEnum.ING;

            if (dto.getOrderPosition() >= approverCount - 1) {
                approveState = ApprovalEnum.APPROVE;

                VacationInfo vacationInfo = vacationInfoRepository.findByUserIdAndYears(vacation.getUserId(), vacation.getCreatedAt().getYear() + "").orElseThrow(() -> new IllegalArgumentException());
                String vacationUseCnt = vacationInfo.getUseCnt();
                if (!(dto.getVacationKind().equals(VacationKind.GYEONGJOSA.getDesc())) && !(dto.getVacationKind().equals(VacationKind.ETC.getDesc()))) {
                    vacationUseCnt = Double.toString(Double.parseDouble(vacationUseCnt) + Double.parseDouble(vacation.getCountDay()));
                    vacationUseCnt = new BigDecimal(vacationUseCnt).stripTrailingZeros().toPlainString();
                }
                vacationInfo.update(vacationUseCnt);

                senderDto.setFrom(user.getEmail());
                cc.addAll(adminRepositorySupport.findEmailById("admin"));
                to.add(user.getEmail());
                senderDto.setSubject("[SYSTEM] " + vacation.getUserName() + " - " + vacation.getVacationKind().getDesc() + " 휴가건 " + ApprovalEnum.APPROVE.getDesc());

            } else {
                Approver nextApprover = approverRepository.findAllByVacationIdAndDivisionAndOrder(vacationId, "approver", dto.getOrderPosition() + 1);
                senderDto.setSubject("[SYSTEM] " + vacation.getUserName() + " - " + vacation.getVacationKind().getDesc() + " 휴가건 " + ApprovalEnum.WAIT.getDesc());
                to.add(nextApprover.getEmail());
                senderDto.setFrom(user.getEmail());
                data.put("id", nextApprover.getUserId());
                data.put("role", TokenEnum.USER.getAuthority());
                data.put("vacationId", vacation.getVacationId());
                String token = jwtTokenConfig.createUserToken(data,3);
                data.put("token", token);
            }
        } else if (dto.getApproveState().equals(ApprovalEnum.REJECT.getValue())) {
            senderDto.setSubject("[SYSTEM] " + vacation.getUserName() + " - " + vacation.getVacationKind().getDesc() + " 휴가건 " + ApprovalEnum.REJECT.getDesc());
            senderDto.setFrom(approver.getEmail());
            to.add(user.getEmail());
            data.put("rejectReason", dto.getRejectReason());

        }
        dto.setOrderPosition(dto.getOrderPosition() + 1);
        vacation.update(approveState, dto.getOrderPosition(), dto.getRejectReason());
        approver.update(approvalFlag);

        data.put("subject", senderDto.getSubject());
        data.put("orderPosition", dto.getOrderPosition());

        senderDto.setTos(to.toArray(new String[to.size()]));
        senderDto.setCcs(cc.toArray(new String[cc.size()]));
        senderDto.setData(data);


        return senderDto;
    }

    public Map<String, Object> selVacationDetailByVacationId(Long vacationId) {
        Map<String, Object> result = new HashMap<>();


        Vacation vacation = vacationRepository.findByVacationId(vacationId).orElseThrow(() -> new IllegalArgumentException());

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

        String division = "approver";

        List<ApproversResponseDto> approverList = approverRepository.findAllByVacationIdAndDivisionOrderByOrderAsc(vacationId, division).stream().map(ApproversResponseDto::new).collect(Collectors.toList());

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

        VacationFile vacationFile = vacationFileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        String key = vacationFile.getKey();
        vacationFileRepository.delete(vacationFile);
        fileUploadUtil.fileDelete(key, uploadDirectory + "/vacation");

    }

    public List<CalendarResponseDto> selVacationCalendar(VacationSearchDto dto) throws Exception {


        return vacationRepositorySupport.findCalendar(dto);
    }

    public boolean selVacationDateCheck(String userId, String startDay, String endDay, String vacationId) throws Exception {

        boolean isAlready = false;

        LocalDate dateStartDay = LocalDate.parse(startDay, DateTimeFormatter.ISO_DATE);
        LocalDate dateEndDay = LocalDate.parse(endDay, DateTimeFormatter.ISO_DATE);


        int vacationCount = vacationRepositorySupport.countByUserIdAndStartDayAndEndDay(userId, dateStartDay, dateEndDay, vacationId);

        if (vacationCount > 0) {
            isAlready = true;
        }
        return isAlready;
    }

    public void updTotalCntByUserId(String userId, TotalCntRequestDto dto, HttpServletRequest request) throws Exception {

        VacationInfoId id = new VacationInfoId(dto.getYear(), userId);
        VacationInfo vacationInfo = vacationInfoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());

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

    public MailSenderDto updVacationCancel(VacationCancelDto dto) throws Exception {

        Vacation vacation = vacationRepository.findById(dto.getVacationId()).orElseThrow(() -> new IllegalArgumentException());
        User user = userRepository.findById(vacation.getUserId()).orElseThrow(() -> new IllegalArgumentException());
        vacation.updateState(ApprovalEnum.CANCEL);

        List<String> toList = adminRepository.findByAdminIdNotAndUseYn("admin", true).stream().map(Admin::getEmail).collect(Collectors.toList());
        List<String> ccList = userRepositorySupport.findEmailByVacationId(dto.getVacationId());

        MailSenderDto senderDto = new MailSenderDto();
        senderDto.setTos(toList.toArray(new String[toList.size()]));
        senderDto.setCcs(ccList.toArray(new String[ccList.size()]));
        senderDto.setFrom(vacation.getUser().getEmail());
        senderDto.setSubject("[SYSTEM] " + vacation.getUserName() + " - " + vacation.getVacationKind().getDesc() + " 휴가 건 " + ApprovalEnum.CANCEL.getDesc());
        Map<String, Object> data = new HashMap<>();
        data.put("subject", senderDto.getSubject());
        data.put("name", vacation.getUserName());
        data.put("reason", dto.getReason());
        data.put("startDay", vacation.getStartDay());
        data.put("endDay", vacation.getEndDay());
        data.put("vacationKindDesc", vacation.getVacationKind().getDesc());
        data.put("vacationKind", vacation.getVacationKind().getKey());
        data.put("vacationReason", vacation.getVacationReason());
        data.put("orgName", user.getOrganization().getOrgName());
        data.put("countDay", vacation.getCountDay());

        senderDto.setData(data);
        senderDto.setTemplate("mail-template");

        return senderDto;

    }

    public void updVacation(VacationListRequestDto dto) {

        if (dto.getFileIds() != null) {
            vacationFileRepository.updateVacationFileVacationIdById(dto.getVacationId(), Arrays.asList(dto.getFileIds()));
        }

        Vacation vacation = vacationRepository.findById(dto.getVacationId()).orElseThrow(() -> new IllegalArgumentException());
        vacation.update(dto);
    }

    public MailSenderDto delVacationAdminByVacationId(VacationAdminDeleteRequestDto dto) throws Exception {

        VacationInfo vacationInfo = vacationInfoRepository.findByUserIdAndYears(dto.getUserId(), dto.getYear() + "").orElseThrow(() -> new IllegalArgumentException());
        String vacationUseCnt = vacationInfo.getUseCnt();

        Vacation vacation = vacationRepository.findById(dto.getVacationId()).orElseThrow(() -> new IllegalArgumentException());

        User user = userRepository.findById(vacation.getUserId()).orElseThrow(() -> new IllegalArgumentException());
        if(dto.isCheck()) {
            if (!(vacation.getVacationKind().equals(VacationKind.GYEONGJOSA)) && !(vacation.getVacationKind().equals(VacationKind.ETC))) {
                vacationUseCnt = Double.toString(Double.parseDouble(vacationUseCnt) - Double.parseDouble(vacation.getCountDay()));
                vacationUseCnt = new BigDecimal(vacationUseCnt).stripTrailingZeros().toPlainString();
                vacationInfo.update(vacationUseCnt);
            }

            vacation.update(ApprovalEnum.CANCELSUCCESS);
        } else {
            vacation.update(ApprovalEnum.APPROVE);
        }
        List<String> toList = adminRepository.findByAdminIdNotAndUseYn("admin", true).stream().map(Admin::getEmail).collect(Collectors.toList());
        List<String> ccList = userRepositorySupport.findEmailByVacationId(dto.getVacationId());

        MailSenderDto senderDto = new MailSenderDto();
        senderDto.setTos(toList.toArray(new String[toList.size()]));
        senderDto.setCcs(ccList.toArray(new String[ccList.size()]));
        senderDto.setFrom(vacation.getUser().getEmail());
        senderDto.setSubject("[SYSTEM] " + vacation.getUserName() + " - " + vacation.getVacationKind().getDesc() + " 휴가 건" + (vacation.getApproveState().equals(ApprovalEnum.CANCELSUCCESS) ? ApprovalEnum.CANCELSUCCESS.getDesc() : "취소요청 반려"));
        Map<String, Object> data = new HashMap<>();
        data.put("subject", senderDto.getSubject());
        data.put("orgName", user.getOrganization().getOrgName());
        data.put("name", vacation.getUserName());
        data.put("startDay", vacation.getStartDay());
        data.put("endDay", vacation.getEndDay());
        data.put("countDay", vacation.getCountDay());
        data.put("vacationKindDesc", vacation.getVacationKind().getDesc());
        data.put("vacationKind", vacation.getVacationKind().getKey());
        data.put("vacationReason", vacation.getVacationReason());


        senderDto.setData(data);
        senderDto.setTemplate("mail-template");

        return senderDto;
    }

    public void updBatchNextYearsByUserId() throws Exception {

        List<User> users = userRepository.findByUseYn(true);
        LocalDate today = LocalDate.now();
        for (User user : users) {
            VacationInfoId id = new VacationInfoId(today.getYear() + "", user.getUserId());
            VacationInfo info = vacationInfoRepository.findById(id).orElse(null);
            if (info == null) {
                int totalCnt = 15;
                if (user.getOrgCode().equals("00000000")) {
                    totalCnt = 0;
                }
                int years = today.getYear() - user.getHireDate().getYear() + 1;
                if (years >= 5) {
                    totalCnt += Math.min((years - 5 + 2) / 2, 10);
                }
                if (years == 1 || (years == 2 && user.getHireDate().getMonthValue() > 1)) {
                    VacationInfo prevInfo = vacationInfoRepository.findByUserIdAndYears(user.getUserId(), today.getYear() - 1 + "").orElse(null);
                    if (prevInfo == null) {
                        totalCnt = 0;
                    } else {
                        totalCnt = Integer.parseInt(prevInfo.getTotalCnt()) - Integer.parseInt(prevInfo.getUseCnt());
                    }
                }
                vacationInfoRepository.save(VacationInfo.builder()
                        .id(id)
                        .totalCnt(totalCnt + "")
                        .useCnt(0 + "").build());


                vacationCountHistoryRepository
                        .save(VacationCountHistory.builder()
                                .adminId("admin")
                                .userId(user.getUserId())
                                .changeCnt(0+" -> "+ totalCnt)
                                .changeReason("연차휴가 발생")
                                .pcIp("localhost")
                                .writeType(LoginHistoryEnum.SYSTEM)
                                .years(today.getYear()+"").build()
                        );

            }
        }
    }


    public void updBatchLess1YearByUserId() throws Exception {
        List<User> users = userRepository.findByUseYn(true);
        LocalDate today = LocalDate.now();
        //users.stream().filter(user -> user.getHireDate())
        for (User user : users) {
            VacationInfoId id = new VacationInfoId(today.getYear() + "", user.getUserId());
            VacationInfo info = vacationInfoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
            Long days = ChronoUnit.DAYS.between(user.getHireDate(), today);
            int years = today.getYear() - user.getHireDate().getYear() + 1;
            log.info(user.getUserName() + " ----------------- 입사일 기준 : " + days);
            if (years == 1 || (years == 2 && user.getHireDate().getMonthValue() > 1)) {
                LocalDate hireDate = user.getHireDate();
                log.info("입사일 ---------------- " + hireDate);
                hireDate = hireDate.withYear(today.getYear());
                hireDate = hireDate.withMonth(today.getMonthValue());
                log.info("cur + hire month Date -------------" + hireDate);
                if (hireDate.isEqual(today)) {
                    int totalCnt = Integer.parseInt(info.getTotalCnt());
                    totalCnt++;
                    vacationCountHistoryRepository
                            .save(VacationCountHistory.builder()
                                    .adminId("admin")
                                    .userId(user.getUserId())
                                    .changeCnt(info.getTotalCnt()+" -> "+ totalCnt)
                                    .changeReason("연차휴가 발생")
                                    .pcIp("localhost")
                                    .writeType(LoginHistoryEnum.SYSTEM)
                                    .years(info.getId().getYears()).build()
                    );
                    info.totalCntUpdate(totalCnt + "");

                }
            }
        }
    }

    public Map<String,Object> selVacationDeadline(VacationDeadlineRequestDto dto) throws Exception {
        Map<String,Object> result = new HashMap<>();
        VacationDeadlineResponseDto responseDto = null;
        VacationDeadline deadline = vacationDeadlineRepository.findById(dto.getId()).orElse(null);
        if (deadline != null) {
            responseDto = new VacationDeadlineResponseDto(deadline);
        }
        result.put(dto.getWriter(),responseDto);
        if(dto.getWriter().equals(LoginHistoryEnum.ADMIN.name())) {
            responseDto = null;
            dto.setWriter(LoginHistoryEnum.USER.name());
            VacationDeadline userDeadline = vacationDeadlineRepository.findById(dto.getId()).orElse(null);
            if (userDeadline != null) {
                responseDto = new VacationDeadlineResponseDto(userDeadline);
            }
            result.put(LoginHistoryEnum.USER.name(),responseDto);
        }

        return result;
    }

    public MailSenderDto insVacationDeadlineByUser(VacationDeadlineRequestDto dto) throws Exception {
        LocalDate today = LocalDate.now();
        VacationInfoListResponseDto user = userRepositorySupport.findVacationInfo(dto.getUserId(),dto.getYear());
        Long days = ChronoUnit.DAYS.between(user.getHireDate(), today);
        log.info(user.getUserName() + " ----------------- 입사일 기준 : " + days);
        LocalDate hireDate = user.getHireDate();
        log.info("입사일 ---------------- " + hireDate);
        Double total = Double.parseDouble(user.getVacationTotalCnt());
        Double use = Double.parseDouble(user.getVacationUseCnt());
        MailSenderDto senderDto = new MailSenderDto();
        log.info("잔여일수 ---------------- " + (total-use));
        if (total - use > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("today", today);
            data.put("id", user.getUserId());
            data.put("role", TokenEnum.USER.getAuthority());
            data.put("name", user.getUserName());
            data.put("orgName", user.getOrgName());
            data.put("hireDate", hireDate);
            data.put("total", CommonUtil.formatD(total));
            data.put("use", CommonUtil.formatD(use));
            data.put("unUse", CommonUtil.formatD(total - use));
            data.put("year", today.getYear() + "");
            data.put("deadline", today.plusDays(15).format(DateTimeFormatter.ISO_DATE));
            String token = jwtTokenConfig.createUserToken(data,15);
            data.put("token", token);
            senderDto.setSubject("[SYSTEM] " + "연차유급휴가일수 통지");
            senderDto.setTemplate("mail-promotion");
            senderDto.setTo(user.getEmail());
            senderDto.setFrom("vacation@joeunins.com");
            senderDto.setData(data);
            VacationDeadlineId id = new VacationDeadlineId(today.getYear() + "", user.getUserId(), LoginHistoryEnum.USER);
            VacationDeadline vacationDeadline = vacationDeadlineRepository.findById(id).orElse(null);
            if(vacationDeadline == null) {
                vacationDeadlineRepository.save(VacationDeadline.builder().id(id).build());
            }
        } else {
            throw new CommonFailException("연차 잔여일 수가 0일 이하는 전송할 수 없습니다");
        }
        return senderDto;
    }
    public MailSenderDto insVacationDeadline(VacationDeadlineRequestDto dto) throws Exception {

        MailSenderDto senderDto = new MailSenderDto();
        VacationInfoListResponseDto user = userRepositorySupport.findVacationInfo(dto.getUserId(), dto.getYear() + "");
        LocalDate today = LocalDate.now();

        Map<String, Object> data = new HashMap<>();
        Double total = Double.parseDouble(user.getVacationTotalCnt());
        Double use = Double.parseDouble(user.getVacationUseCnt());
        data.put("today", today);
        data.put("name", user.getUserName());
        data.put("orgName", user.getOrgName());
        data.put("hireDate", user.getHireDate());
        data.put("total", CommonUtil.formatD(total));
        data.put("use", CommonUtil.formatD(use));
        data.put("unUse", CommonUtil.formatD(total - use));
        data.put("year", today.getYear() + "");
        data.put("writer", dto.getWriter());
        if (dto.getWriter().equals(LoginHistoryEnum.ADMIN.name())) {
            senderDto.setSubject("[SYSTEM] " + "미사용 연차유급휴가 사용시기 지정통보");
            senderDto.setTo(user.getEmail());
            senderDto.setFrom("vacation@joeunins.com");
            vacationDeadlineRepository.save(dto.toEntity());
            VacationDeadlineId id = dto.getId();
            id.setWriteType(LoginHistoryEnum.USER);
            VacationDeadline deadline = vacationDeadlineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
            data.put("notificationTime", deadline.getModifiedAt().format(DateTimeFormatter.ISO_DATE));
            data.put("id", user.getUserId());
            String token = jwtTokenConfig.createUserToken(data,3);
            data.put("token", token);
            senderDto.setTemplate("mail-report-admin");

        } else {
            String key = fileUploadUtil.fileUpload(Base64.decodeBase64(dto.getImage()), uploadDirectory + "/deadline");
            senderDto.setSubject("[SYSTEM] " + "미사용 연차유급휴가 사용시기 지정통보 확인(관리자용)");
            data.put("image", basicUrl + "/" + resourcesUriPath + "/deadline/" + key);
            senderDto.setTo("vacation@joeunins.com");
            senderDto.setFrom(user.getEmail());
            VacationDeadline deadline = vacationDeadlineRepository.findById(dto.getId()).orElseThrow(() -> new IllegalArgumentException());
            deadline.update(JsonUtil.dtoToString(dto.getDeadlines()));
            senderDto.setTemplate("mail-report-user");
        }

        senderDto.setData(data);

        return senderDto;
    }

    public List<MailSenderDto> updBatchVacationDeadlineByUser() throws Exception {
        LocalDate today = LocalDate.now();
        List<VacationInfoListResponseDto> users = userRepositorySupport.findVacationInfos(today.getYear() + "");
        List<MailSenderDto> senderList = new ArrayList<>();
        for (VacationInfoListResponseDto user : users) {
            Long days = ChronoUnit.DAYS.between(user.getHireDate(), today);
            log.info(user.getUserName() + " ----------------- 입사일 기준 : " + days);
            LocalDate hireDate = user.getHireDate();
            log.info("입사일 ---------------- " + hireDate);

            Double total = Double.parseDouble(user.getVacationTotalCnt());
            Double use = Double.parseDouble(user.getVacationUseCnt());

            if (total - use > 0 && days == 275) {
                MailSenderDto dto = new MailSenderDto();
                Map<String, Object> data = new HashMap<>();
                data.put("today", today);
                data.put("id", user.getUserId());
                data.put("role", TokenEnum.USER.getAuthority());
                data.put("name", user.getUserName());
                data.put("orgName", user.getOrgName());
                data.put("hireDate", hireDate);
                data.put("total", total);
                data.put("use", use);
                data.put("unUse", total - use);
                data.put("year", today.getYear() + "");
                data.put("deadline", today.plusDays(15).format(DateTimeFormatter.ISO_DATE));
                String token = jwtTokenConfig.createUserToken(data, 15);
                data.put("token", token);
                dto.setSubject("[SYSTEM] " + "연차유급휴가일수 통지");
                dto.setTemplate("mail-promotion");
                dto.setTo(user.getEmail());
                dto.setFrom("vacation@joeunins.com");
                dto.setData(data);
                VacationDeadlineId id = new VacationDeadlineId(today.getYear() + "", user.getUserId(), LoginHistoryEnum.USER);
                VacationDeadline vacationDeadline = vacationDeadlineRepository.findById(id).orElse(null);
                if(vacationDeadline == null) {
                    vacationDeadlineRepository.save(VacationDeadline.builder().id(id).build());
                }
                senderList.add(dto);
            }
        }
        return senderList;
    }

    public List<String> selBatchUserHireDateOfEntry() throws Exception {

        LocalDate today = LocalDate.now();
        List<VacationInfoListResponseDto> users = userRepositorySupport.findVacationInfos(today.getYear() + "");
        List<String> userNames = new ArrayList<>();
        for (VacationInfoListResponseDto user : users) {
            Long days = ChronoUnit.DAYS.between(user.getHireDate(), today);
            log.info(user.getUserName() + " ----------------- 입사일 기준 : " + days);
            LocalDate hireDate = user.getHireDate();
            log.info("입사일 ---------------- " + hireDate);

            if (days < 365 && days == 355) {
                userNames.add(user.getUserName());
            }
        }
        return userNames;
    }

    public Map<String,Object> selVacationHistoryListByUserId(String userId, VacationSearchDto dto) throws Exception {
        return vacationCountHistoryRepositorySupport.findAllBySearch(userId,dto);
    }
}
