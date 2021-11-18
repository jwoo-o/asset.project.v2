package com.gen.vacation.global.util;

import com.gen.vacation.global.common.dto.MailSenderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-05-04
 * Time: 오전 10:33
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CustomMailSenderUtil {

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(MailSenderDto dto) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(dto.getSubject());
            helper.setTo(dto.getTo());
            helper.setFrom(dto.getFrom());

            Context context = new Context();
            dto.getData().forEach(context::setVariable);
            String html = templateEngine.process(dto.getTemplate(), context);
            helper.setText(html, true);
            javaMailSender.send(message);
        }catch (Exception e) {
           log.error(e.getMessage());
        }



    }
    @Async
    public void sendMails(MailSenderDto dto) throws MessagingException, IOException {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(dto.getSubject());
            helper.setTo(dto.getTos());
            helper.setCc(dto.getCcs());
            helper.setFrom(dto.getFrom());

            Context context = new Context();

            dto.getData().forEach(context::setVariable);
            String html = templateEngine.process(dto.getTemplate(), context);

            helper.setText(html, true);

            javaMailSender.send(message);
        }catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
