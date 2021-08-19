package com.gen.vacation.global.util;

import com.gen.vacation.global.common.dto.MailSenderDto;
import com.gen.vacation.server.vacation.service.VacationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-05-27
 * Time: 오전 10:15
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ScheduledUtil {


    private final VacationService vacationService;

    private final CustomMailSenderUtil senderUtil;



    @Scheduled(cron = "0 0 0 * * *")
    public void vacationBatch() {
        log.info("vacation scheduled");
        try {
            vacationService.updBatchNextYearsByUserId();
            vacationService.updBatchLess1YearByUserId();
            deadlineBatch();
            findLessThanOneYearTenLeft();
        } catch (Exception e){
          log.error(e.getMessage());
        }

    }

    private void deadlineBatch() {
        log.info("deadline scheduled");
        try {
            List<MailSenderDto> list = vacationService.updBatchVacationDeadlineByUser();
            List<String> userNames = new ArrayList<>();
            for(MailSenderDto dto : list) {
                senderUtil.sendMail(dto);
                userNames.add((String) dto.getData().get("name"));
            }
            if(!userNames.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("userNames", Arrays.toString(userNames.toArray(new String[userNames.size()])));
                data.put("currentDate" , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                data.put("subject", "통지메일 전송 알림");
                data.put("anniversary", false);
                senderUtil.sendMail(MailSenderDto.builder()
                        .to("vacation@joeunins.com")
                        .from("vacation@joeunins.com")
                        .subject("[SYSTEM] 연차유급휴가일수 통지메일 전송 알람")
                        .data(data)
                        .template("mail-alarm").build());
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }

    private void findLessThanOneYearTenLeft(){
        log.info("find Then Left LessThanOneYear batch");
        try {
            List<String> list = vacationService.selBatchUserHireDateOfEntry();
            for(String userName : list) {
                Map<String, Object> data = new HashMap<>();
                data.put("userNames", userName);
                data.put("currentDate" , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                data.put("subject",  userName+ "-1주년 근속일 알림");
                data.put("anniversary", true);
                senderUtil.sendMail(MailSenderDto.builder()
                        .to("vacation@joeunins.com")
                        .from("vacation@joeunins.com")
                        .subject("[SYSTEM] 알림")
                        .data(data)
                        .template("mail-alarm").build());
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }



    }

}
