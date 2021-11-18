package com.gen.vacation.server.vacation.service;

import com.gen.vacation.global.common.dto.MailSenderDto;
import com.gen.vacation.global.config.JwtTokenConfig;
import com.gen.vacation.global.domain.entity.User;
import com.gen.vacation.global.domain.entity.VacationCountHistory;
import com.gen.vacation.global.domain.entity.VacationDeadline;
import com.gen.vacation.global.domain.entity.VacationInfo;
import com.gen.vacation.global.domain.entity.id.VacationDeadlineId;
import com.gen.vacation.global.domain.entity.id.VacationInfoId;
import com.gen.vacation.global.domain.entity.repositorys.UserRepository;
import com.gen.vacation.global.domain.entity.repositorys.VacationCountHistoryRepository;
import com.gen.vacation.global.domain.entity.repositorys.VacationDeadlineRepository;
import com.gen.vacation.global.domain.entity.repositorys.VacationInfoRepository;
import com.gen.vacation.global.enums.LoginHistoryEnum;
import com.gen.vacation.global.enums.TokenEnum;
import com.gen.vacation.server.user.dto.VacationInfoListResponseDto;
import com.gen.vacation.server.user.repository.UserRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class VacationBatchService {

    private final UserRepository userRepository;
    private final VacationInfoRepository vacationInfoRepository;
    private final VacationCountHistoryRepository vacationCountHistoryRepository;
    private final UserRepositorySupport userRepositorySupport;
    private final VacationDeadlineRepository vacationDeadlineRepository;

    private final JwtTokenConfig jwtTokenConfig;

    public void updBatchNextYearsByUserId() throws Exception {

        List<User> users = userRepository.findByUseYn(true);
        LocalDate today = LocalDate.now();
        for (User user : users) {
            VacationInfoId id = new VacationInfoId(today.getYear() + "", user.getUserId());
            VacationInfo info = vacationInfoRepository.findById(id).orElse(null);
            if (info == null) {
                log.info(today.getYear() +" 연차 발생 "+ user.getUserName());
                int totalCnt = 15;
                if (user.getOrgCode().equals("00000000")) {
                    totalCnt = 0;
                } else {
                    int years = today.getYear() - user.getHireDate().getYear() + 1;
                    if (years >= 5) {
                        totalCnt += Math.min((years - 5 + 2) / 2, 10);
                    }
                    if (years <= 1 || (years == 2 && user.getHireDate().getMonthValue() > 3)) {
                        VacationInfo prevInfo = vacationInfoRepository.findByUserIdAndYears(user.getUserId(), today.getYear() - 1 + "").orElse(null);
                        if (prevInfo == null) {
                            totalCnt = 0;
                        } else {
                            totalCnt = Integer.parseInt(prevInfo.getTotalCnt()) - Integer.parseInt(prevInfo.getUseCnt());
                        }
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
        for (User user : users) {
            VacationInfoId id = new VacationInfoId(today.getYear() + "", user.getUserId());
            VacationInfo info = vacationInfoRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            long days = ChronoUnit.DAYS.between(user.getHireDate(), today);
            int years = today.getYear() - user.getHireDate().getYear() + 1;
            log.info(user.getUserName() + " ----------------- 입사일 기준 : " + days);
            if (years == 1 || (years == 2 && user.getHireDate().getMonthValue() > 3)) {
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


    public List<String> selBatchUserHireDateOfEntry() throws Exception {

        LocalDate today = LocalDate.now();
        List<VacationInfoListResponseDto> users = userRepositorySupport.findVacationInfos(today.getYear() + "");
        List<String> userNames = new ArrayList<>();
        for (VacationInfoListResponseDto user : users) {
            long days = ChronoUnit.DAYS.between(user.getHireDate(), today);
            log.info(user.getUserName() + " ----------------- 입사일 기준 : " + days);
            LocalDate hireDate = user.getHireDate();
            log.info("입사일 ---------------- " + hireDate);

            if (days == 355) {
                userNames.add(user.getUserName());
            }
        }
        return userNames;
    }


    public List<MailSenderDto> updBatchVacationDeadlineByUser() throws Exception {
        LocalDate today = LocalDate.now();
        List<VacationInfoListResponseDto> users = userRepositorySupport.findVacationInfos(today.getYear() + "");
        List<MailSenderDto> senderList = new ArrayList<>();
        for (VacationInfoListResponseDto user : users) {
            long days = ChronoUnit.DAYS.between(user.getHireDate(), today);
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
}
