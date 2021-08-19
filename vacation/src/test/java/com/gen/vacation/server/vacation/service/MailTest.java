package com.gen.vacation.server.vacation.service;

import com.gen.vacation.global.common.dto.MailSenderDto;
import com.gen.vacation.global.util.CustomMailSenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-05-25
 * Time: 오후 3:50
 */
/*@ActiveProfiles("local")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j*/
public class MailTest {

   /* @Autowired
    private CustomMailSenderUtil senderUtil;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private VacationService vacationService;*/

    /*public void 테스트() throws Exception {

        System.out.println(vacationService.updBatchVacationDeadlineByUser());
    }*/

}

   /* public void 배치서비스() throws Exception {*/

      /*  List<MailSenderDto> list =  vacationService.updBatchVacationDeadlineByUser();
        for(MailSenderDto dto : list){
            senderUtil.sendMail(dto);
        }*/
       /* int totalCnt = 15;
        int years = 27;
        totalCnt += Math.min((years-5+2)/2, 10);
        System.out.println(totalCnt);*/
   /* @Test
    public void 메일전송() throws Exception {
        LocalDate today = LocalDate.now();
        MailSenderDto dto = new MailSenderDto();
        Map<String,Object> data = new HashMap<>();
        data.put("today",today);
        data.put("name","테스트");
        data.put("orgName","테스트 부서");
        data.put("hireDate","입사일");
        data.put("total","15");
        data.put("use","7");
        data.put("unUse","8");
        data.put("year",today.format(DateTimeFormatter.ofPattern("YYYY")));
        data.put("deadline",today.withMonth(6).withDayOfMonth(30).format(DateTimeFormatter.ISO_DATE));
        dto.setSubject("연차유급휴가일수 통지");
        dto.setTemplate("mail-promotion");
        dto.setTo("jwoh@joeunins.com");
        dto.setFrom("mskim@joeunins.com");
        dto.setData(data);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(dto.getSubject());
            helper.setTo(dto.getTo());
            helper.setFrom(dto.getFrom());

            Context context = new Context();
            dto.getData().entrySet().forEach(entry -> {
                context.setVariable(entry.getKey(), entry.getValue());
            });
            String html = templateEngine.process(dto.getTemplate(), context);
            helper.setText(html, true);
            javaMailSender.send(message);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }*/
