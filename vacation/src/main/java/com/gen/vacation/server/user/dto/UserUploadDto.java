package com.gen.vacation.server.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-04-26
 * Time: 오후 3:49
 */
@Getter
@Setter
public class UserUploadDto {

    private String userId;
    private MultipartFile files;

}
