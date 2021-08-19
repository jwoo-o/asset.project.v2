package com.gen.vacation.server.login.web;

import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.contant.ErrorContant;
import com.gen.vacation.global.exception.PasswordNotMatchException;
import com.gen.vacation.global.exception.UnauthorizedException;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.server.login.dto.AdminLoginResponseDto;

import com.gen.vacation.server.login.dto.AdminLoginDto;
import com.gen.vacation.server.login.dto.UserLoginDto;
import com.gen.vacation.server.login.dto.UserLoginResponseDto;
import com.gen.vacation.server.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 1:29
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginApiController {

    private final ResponseService responseService;

    private final LoginService loginService;


    @PostMapping("/admin/login")
    public SingleResult<AdminLoginResponseDto> adminLogin(@RequestBody AdminLoginDto dto, HttpServletRequest req) throws Exception {

        AdminLoginResponseDto adminLoginResponseDto = loginService.updAdminLogin(dto, req);

        String errCode = adminLoginResponseDto.getErrCode();

        if (errCode.equals(ErrorContant.WRNG_LOGIN)) {

            throw new PasswordNotMatchException();

        } else if (errCode.equals(ErrorContant.NOT_ACCESS)) {
            throw new UnauthorizedException();
        }
        return responseService.getSingleResult(adminLoginResponseDto);
    }

    @PostMapping("/user/login")
    public SingleResult<UserLoginResponseDto> userLogin(@RequestBody UserLoginDto dto, HttpServletRequest req) throws Exception {

        UserLoginResponseDto userLoginResponseDto = loginService.updUserLogin(dto, req);

        String errCode = userLoginResponseDto.getErrCode();
        if (errCode.equals(ErrorContant.WRNG_LOGIN)) {
            throw new PasswordNotMatchException();

        } else if (errCode.equals(ErrorContant.NOT_ACCESS)) {
            throw new UnauthorizedException();
        }
        return responseService.getSingleResult(userLoginResponseDto);
    }

}
