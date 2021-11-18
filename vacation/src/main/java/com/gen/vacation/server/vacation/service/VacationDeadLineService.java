package com.gen.vacation.server.vacation.service;

import com.gen.vacation.global.domain.entity.User;
import com.gen.vacation.global.domain.entity.repositorys.UserRepository;
import com.gen.vacation.global.enums.LoginHistoryEnum;
import com.gen.vacation.global.enums.TokenEnum;
import com.gen.vacation.global.common.dto.MailSenderDto;
import com.gen.vacation.global.config.JwtTokenConfig;
import com.gen.vacation.global.domain.entity.VacationDeadline;
import com.gen.vacation.global.domain.entity.id.VacationDeadlineId;
import com.gen.vacation.global.domain.entity.repositorys.VacationDeadlineRepository;
import com.gen.vacation.global.exception.CommonFailException;
import com.gen.vacation.global.util.CommonUtil;
import com.gen.vacation.global.util.FileUploadUtil;
import com.gen.vacation.global.util.JsonUtil;
import com.gen.vacation.server.user.dto.VacationInfoListResponseDto;
import com.gen.vacation.server.user.repository.UserRepositorySupport;
import com.gen.vacation.server.vacation.dto.VacationDeadlineRequestDto;
import com.gen.vacation.server.vacation.dto.VacationDeadlineResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class VacationDeadLineService {

    private final VacationDeadlineRepository vacationDeadlineRepository;

    private final UserRepositorySupport userRepositorySupport;

    private final UserRepository userRepository;

    private final JwtTokenConfig jwtTokenConfig;

    private final FileUploadUtil fileUploadUtil;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Value("${file.uri.path}")
    private String resourcesUriPath;

    @Value("${server.servlet.context-path}")
    private String basicUrl;


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
        double total = Double.parseDouble(user.getVacationTotalCnt());
        double use = Double.parseDouble(user.getVacationUseCnt());
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
            VacationDeadline deadline = vacationDeadlineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
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
            VacationDeadline deadline = vacationDeadlineRepository.findById(dto.getId()).orElseThrow(IllegalArgumentException::new);
            deadline.update(JsonUtil.dtoToString(dto.getDeadlines()));
            senderDto.setTemplate("mail-report-user");
        }

        senderDto.setData(data);

        return senderDto;
    }

    public MailSenderDto updVacationDeadlineByAdmin(VacationDeadlineRequestDto dto) throws Exception{


        VacationDeadline deadline = vacationDeadlineRepository.findById(dto.getId()).orElseThrow(IllegalArgumentException::new);
        User user = userRepository.findById(dto.getUserId()).orElseThrow(IllegalArgumentException::new);

        if(!deadline.isConfirm()) {
            deadline.updateConfirm();
            Map<String, Object> data = new HashMap<>();
            data.put("userNames", user.getUserName());
            data.put("currentDate" , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            data.put("subject",  "통지 - 지정일 확인");
            data.put("anniversary", false);

            return MailSenderDto.builder()
                    .to("vacation@joeunins.com")
                    .from(user.getEmail())
                    .subject("[SYSTEM] 미사용 연차유급휴가일수 통지 - 지정일 확인")
                    .data(data)
                    .template("mail-alarm").build();
        }

        return null;

    }
}
