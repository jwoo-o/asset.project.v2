package com.gen.vacation.global.service;

import com.gen.vacation.global.enums.TokenEnum;
import com.gen.vacation.global.config.security.custom.CustomAdminDetail;
import com.gen.vacation.global.config.security.custom.CustomUserDetail;
import com.gen.vacation.global.domain.entity.repositorys.AdminRepository;
import com.gen.vacation.global.domain.entity.repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-05-19
 * Time: 오후 7:19
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {


    private final AdminRepository adminRepository;

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String key) throws UsernameNotFoundException {

        return null;

    }

    public UserDetails loadAdminById(String id) {


        int count = adminRepository.countByAdminIdAndUseYn(id, true);
        if (count <= 0) {
            throw new UsernameNotFoundException("User not found");
        }

        return CustomAdminDetail.builder()
                .adminId(id)
                .authority(TokenEnum.ADMIN.getAuthority()).build();
    }

    public UserDetails loadUserById(String id) {

        int count = userRepository.countByUserIdAndUseYn(id, true);
        if (count <= 0) {
            throw new UsernameNotFoundException("User not found");
        }

        return CustomUserDetail.builder()
                .userId(id)
                .authority(TokenEnum.USER.getAuthority()).build();
    }
}
