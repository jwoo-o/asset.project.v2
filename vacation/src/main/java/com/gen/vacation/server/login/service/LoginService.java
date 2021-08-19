package com.gen.vacation.server.login.service;

import com.gen.vacation.global.Enum.LoginHistoryEnum;
import com.gen.vacation.global.Enum.TokenEnum;
import com.gen.vacation.global.config.JwtTokenConfig;
import com.gen.vacation.global.contant.ErrorContant;
import com.gen.vacation.global.domain.entity.*;
import com.gen.vacation.global.domain.entity.repositorys.*;
import com.gen.vacation.global.util.*;
import com.gen.vacation.server.login.dto.AdminLoginResponseDto;
import com.gen.vacation.server.login.dto.AdminLoginDto;
import com.gen.vacation.server.login.dto.UserLoginDto;
import com.gen.vacation.server.login.dto.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 2:17
 */
@Validated
@RequiredArgsConstructor
@Service
public class LoginService {


    private final LoginHistoryRepository loginHistoryRepository;

    private final AdminRepository adminRepository;

    private final UserRepository userRepository;

    private final JwtTokenConfig jwtTokenConfig;

    private final ApproverDetailRepository approverDetailRepository;

    private final OrganizationRepository organizationRepository;

    @Value("${spring.jwt.secret}")
    private String encryptKey;

    public AdminLoginResponseDto updAdminLogin(@Valid AdminLoginDto dto, HttpServletRequest req) throws Exception {

        String accessIp = CommonUtil.getClientIpAddr(req);
        dto.setPassword(AESUtil.decrypt(dto.getPassword(), encryptKey));

        Admin admin = adminRepository.findByAdminIdAndUseYn(dto.getAdminId(), true).orElseThrow(() -> new UsernameNotFoundException(""));

        AdminLoginResponseDto adminLoginResponseDto = new AdminLoginResponseDto();

        LoginHistory history = loginHistoryRepository.save(LoginHistory.builder()
                .loginId(admin.getAdminId())
                .loginYn(false)
                .loginType(LoginHistoryEnum.ADMIN)
                .pcIp(accessIp)
                .errMsg(ErrorContant.FAIL).build());
        adminLoginResponseDto.setErrCode(ErrorContant.FAIL);
        String encPwd = "";

            if (admin.isSalt()) {
                encPwd = SHA256Util.getEncrypt(dto.getPassword(), dto.getAdminId().getBytes()).toUpperCase();
                if (admin.getPassword().equals(encPwd)) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", admin.getAdminId());
                    data.put("name", admin.getName());
                    data.put("mgrOrgCode", admin.getMgrOrgCode());
                    data.put("order", admin.getMgrOrganization().getOrder());
                    data.put("mgrOrgName", admin.getMgrOrganization().getOrgName());
                    data.put("role", TokenEnum.ADMIN.getAuthority());
                    String token = jwtTokenConfig.createToken(data);

                    String refreshToken = UUID.randomUUID().toString();

                    adminLoginResponseDto.setRefreshToken(refreshToken);
                    adminLoginResponseDto.setToken(token);

                    admin.login();
                    history.success(true, ErrorContant.SUCCESS);
                    adminLoginResponseDto.setErrCode(ErrorContant.SUCCESS);
                } else {
                    history.fail(ErrorContant.WRNG_LOGIN);
                    adminLoginResponseDto.setErrCode(ErrorContant.WRNG_LOGIN);
                }

            } else {
                encPwd = SHA256Util.getEncrypt(dto.getPassword(), encryptKey.getBytes()).toUpperCase();
                if (admin.getPassword().equals(encPwd)) {
                    adminLoginResponseDto.setPwdChange(true);
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", admin.getAdminId());
                    String token = jwtTokenConfig.createToken(data);
                    adminLoginResponseDto.setToken(token);
                } else {
                    history.fail(ErrorContant.WRNG_LOGIN);
                    adminLoginResponseDto.setErrCode(ErrorContant.WRNG_LOGIN);
                }
            }
        return adminLoginResponseDto;
    }

    public UserLoginResponseDto updUserLogin(@Valid UserLoginDto dto, HttpServletRequest req) throws Exception {

        String accessIp = CommonUtil.getClientIpAddr(req);
        dto.setPassword(AESUtil.decrypt(dto.getPassword(), encryptKey));

        User user = userRepository.findByUserIdAndUseYn(dto.getUserId(), true).orElseThrow(() -> new UsernameNotFoundException(""));

        int isApprover = approverDetailRepository.countByUserId(user.getUserId());

        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto();

        LoginHistory history = loginHistoryRepository.save(LoginHistory.builder()
                .loginId(user.getUserId())
                .loginYn(false)
                .loginType(LoginHistoryEnum.USER)
                .pcIp(accessIp)
                .errMsg(ErrorContant.FAIL).build());
        userLoginResponseDto.setErrCode(ErrorContant.FAIL);
        String encPwd = "";

        if (user.isSalt()) {
            encPwd = SHA256Util.getEncrypt(dto.getPassword(), dto.getUserId().getBytes()).toUpperCase();
            if (user.getPassword().equals(encPwd)) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", user.getUserId());
                data.put("userName", user.getUserName());
                data.put("order", user.getOrganization().getOrder());
                data.put("rankNm", user.getRankNm());
                data.put("rankCd", user.getRankCd());
                data.put("role", TokenEnum.USER.getAuthority());
                data.put("jobNm", user.getJobNm());
                data.put("jobCd", user.getJobCd());
                data.put("tel", user.getTel());
                data.put("isApprover", isApprover != 0);
                data.put("isAdmin",user.isAdmin());
                data.put("isAssetAdmin",user.isAssetAdmin());
                if(user.isAdmin()){
                    Organization organization = organizationRepository.findById("00000000").orElseThrow(() -> new IllegalArgumentException());
                    data.put("orgName", organization.getOrgName());
                    data.put("orgCode", organization.getOrgCode());
                    data.put("mgrOrgCode", organization.getOrgCode());
                }else {
                    data.put("orgName", user.getOrganization().getOrgName());
                    data.put("orgCode", user.getOrganization().getOrgCode());
                }
                if(user.isAssetAdmin()) {
                    data.put("mgrOrgCode", user.getMgrOrgCode());
                }
                String token = jwtTokenConfig.createToken(data);
                String refreshToken = UUID.randomUUID().toString();
                userLoginResponseDto.setRefreshToken(refreshToken);
                userLoginResponseDto.setToken(token);

                user.login();
                history.success(true, ErrorContant.SUCCESS);
                userLoginResponseDto.setErrCode(ErrorContant.SUCCESS);
            } else {
                history.fail(ErrorContant.WRNG_LOGIN);
                userLoginResponseDto.setErrCode(ErrorContant.WRNG_LOGIN);
            }

        } else {
            encPwd = SHA256Util.getEncrypt(dto.getPassword(), encryptKey.getBytes()).toUpperCase();
            if (user.getPassword().equals(encPwd)) {
                userLoginResponseDto.setPwdChange(true);
                Map<String, Object> data = new HashMap<>();
                data.put("id", user.getUserId());
                String token = jwtTokenConfig.createToken(data);
                userLoginResponseDto.setToken(token);
            } else {
                history.fail(ErrorContant.WRNG_LOGIN);
                userLoginResponseDto.setErrCode(ErrorContant.WRNG_LOGIN);

            }
        }
        return userLoginResponseDto;
    }
}
