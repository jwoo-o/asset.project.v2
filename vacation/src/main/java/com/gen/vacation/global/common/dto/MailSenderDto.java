package com.gen.vacation.global.common.dto;

import lombok.*;

import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-05-04
 * Time: 오전 10:42
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailSenderDto {

    private String to;
    private String cc;
    private String[] tos;
    private String[] ccs;
    private String from;
    private String subject;
    private String template;
    private Map<String,Object> data;
}
