package com.gen.vacation.server.user.service;

import com.gen.vacation.global.common.dto.SearchRequestDto;

import com.gen.vacation.global.domain.entity.Admin;
import com.gen.vacation.global.domain.entity.User;

import com.gen.vacation.global.domain.entity.VacationInfo;
import com.gen.vacation.global.domain.entity.id.VacationInfoId;
import com.gen.vacation.global.domain.entity.repositorys.*;
import com.gen.vacation.global.util.AESUtil;
import com.gen.vacation.global.util.FileUploadUtil;
import com.gen.vacation.global.util.SHA256Util;
import com.gen.vacation.server.organization.dto.OrganizationChartDto;
import com.gen.vacation.server.user.dto.*;
import com.gen.vacation.server.user.repository.UserRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 2:17
 */
@Validated
@RequiredArgsConstructor
@Service
public class UserService {


    private final UserRepository userRepository;
    private final UserRepositorySupport userRepositorySupport;
    private final VacationInfoRepository vacationInfoRepository;
    private final AdminRepository adminRepository;
    private final OrganizationRepository organizationRepository;
    private final ApproverDetailRepository approverDetailRepository;

    private final FileUploadUtil fileUploadUtil;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Value("${file.uri.path}")
    private String resourcesUriPath;

    @Value("${server.servlet.context-path}")
    private String basicUrl;


    @Value("${spring.jwt.secret}")
    private String encryptKey;




    public Map<String, Object> selUserList(SearchRequestDto dto) throws Exception {

        return userRepositorySupport.findBySearch(dto);
    }

    public Map<String, Object> selUserListByUserId(SearchRequestDto dto) throws Exception {

        return userRepositorySupport.findByUserId(dto);

    }


    public Map<String, Object> selUserListByOrgCode(String orgCode) throws Exception {

        return userRepositorySupport.findAllBySearch(orgCode);
    }

    public boolean selUserIdCheck(String userId) throws Exception {

        return userRepository.countByUserId(userId) > 0;
    }

    public void insUser(@Valid UserRequestDto dto) throws Exception {

        if (dto.getPassword() == null) {
            dto.setPassword(SHA256Util.getEncrypt(dto.getUserId(), encryptKey.getBytes()).toUpperCase());
            dto.setSalt(false);
        } else {
            String password = AESUtil.decrypt(dto.getPassword(), encryptKey);
            dto.setPassword(SHA256Util.getEncrypt(password, dto.getUserId()).toUpperCase());
            dto.setSalt(true);
        }
        userRepository.save(dto.toEntity());

        int hireYear = dto.getHireDate().toLocalDateTime().getYear();
        LocalDate today = LocalDate.now();

        String totalCnt = "15";
        long days = ChronoUnit.DAYS.between(dto.getHireDate().toLocalDateTime().toLocalDate(),today);
        if(hireYear == today.getYear() || days <= 365){
            totalCnt = "0";
        }
        VacationInfoId id = new VacationInfoId(today.getYear()+"",dto.getUserId());
        vacationInfoRepository.save(VacationInfo.builder()
                .id(id)
                .totalCnt(totalCnt)
                .useCnt("0").build());


        if(dto.isAdmin()) {
            adminRepository.save(dto.adminToEntity());
        }



    }

    public void updUser(@Valid UserRequestDto dto) throws Exception {

        User user = userRepository.findByUserIdAndUseYn(dto.getUserId(), true).orElseThrow(() -> new IllegalArgumentException());
        user.update(dto);

        Admin admin = adminRepository.findById(dto.getUserId()).orElse(null);
        if(dto.isAdmin()){
            dto.setPassword(user.getPassword());
            dto.setSalt(user.isSalt());
            if(admin == null) {
                adminRepository.save(dto.adminToEntity());
            }else{
                admin.use(true);
                admin.changePassword(dto.getPassword(),dto.isSalt());
            }
        }else{
            if(admin != null) {
                admin.use(false);
            }
        }
        UserInfoResponseDto infoResponseDto = new UserInfoResponseDto(user);
        approverDetailRepository.updateUserInfo(infoResponseDto);

    }

    public void updUserPass(@Valid UserPasswordRequestDto dto) throws Exception {

        User user = userRepository.findByUserIdAndUseYn(dto.getUserId(), true).orElseThrow(() -> new IllegalArgumentException());
        /** 변경일 때*/
        if (!dto.getPassword().isEmpty()) {
            dto.setPassword(AESUtil.decrypt(dto.getPassword(), encryptKey));

            dto.setPassword(SHA256Util.getEncrypt(dto.getPassword(), dto.getUserId().getBytes()).toUpperCase());
            String pwd = dto.getPassword();
            user.changePassword(pwd, true);

            if(user.isAdmin()){
                adminRepository.findById(dto.getUserId()).ifPresent(admin -> admin.changePassword(pwd, true));
            }
            /**초기화 일때*/
        } else {
            String pwd = SHA256Util.getEncrypt(dto.getUserId(), encryptKey.getBytes()).toUpperCase();
            user.changePassword(pwd, false);
            if(user.isAdmin()){
                adminRepository.findById(dto.getUserId()).ifPresent(admin -> admin.changePassword(pwd, false));
            }
        }
    }

    public  Map<String,Object> selUserInfo(String userId) throws Exception {

        User user = userRepository.findByUserIdAndUseYn(userId, true).orElseThrow(() -> new IllegalArgumentException());

        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(user);
        Map<String,Object> map = new HashMap<>();
        map.put("info",userInfoResponseDto);
        List<Map<String,Object>> fileList = new ArrayList<>();
        if(!userInfoResponseDto.getImgSrc().equals("")) {
            String url = basicUrl + "/" + resourcesUriPath + "/profile/"+user.getProfileImage();
            Map<String,Object> file = new HashMap<>();
            file.put("name",user.getProfileImage());
            file.put("url", url);
            userInfoResponseDto.setImgSrc(url);
            fileList.add(file);
        }
        map.put("fileList",fileList);
        return map;
    }


    public void delUser(String userId, String leaveDate) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException());
        user.delete(LocalDate.parse(leaveDate));

        approverDetailRepository.deleteByUserId(user.getUserId());

    }

    public void insUserProfileImage(UserUploadDto dto) throws Exception {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException());

        String profileImage = fileUploadUtil.fileUpload(dto.getFiles(),uploadDirectory+"/profile");
        user.imageUpload(profileImage);
    }

    public void delUserProfileImage(String userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException());
        fileUploadUtil.fileDelete(user.getProfileImage(), uploadDirectory + "/profile");
        user.imageUpload(null);
    }

    public List<UserInfoResponseDto> selUserListAll() throws Exception {

        return userRepository.findByUseYn(true)
                .stream()
                .map(UserInfoResponseDto::new)
                .collect(Collectors.toList());
    }

    public Map<String,Object> selSeat() throws Exception {
        Map<String,Object> result = new HashMap<>();
        List<SeatResponseDto> list = userRepositorySupport.findSeatByUser();
        for(SeatResponseDto dto : list){
            if(dto.getImgSrc() != null && !dto.getImgSrc().equals("")){
                String url = basicUrl + "/" + resourcesUriPath + "/profile/"+dto.getImgSrc();
                dto.setImgSrc(url);
            }
        }
        result.put("seatList",list);
        result.put("orgList",organizationRepository.findAll().stream().map(OrganizationChartDto::new).collect(Collectors.toList()));
        return result;
    }
}
