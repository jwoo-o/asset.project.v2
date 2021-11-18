package com.gen.vacation.server.user.web;

import com.gen.vacation.global.common.CommonResult;
import com.gen.vacation.global.common.ListResult;
import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.server.user.dto.*;
import com.gen.vacation.server.user.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final ResponseService responseService;

    private final UserService userService;

    @GetMapping("/user/user-list")
    public SingleResult<Map<String, Object>> getUserListBySearch(@Valid SearchRequestDto dto) throws Exception {


        return responseService.getSingleResult(userService.selUserList(dto));

    }
    @GetMapping("/user")
    public ListResult<UserInfoResponseDto> getUserListAll() throws Exception {

        return responseService.getListResult(userService.selUserListAll());

    }


    @GetMapping("/user/user-list-userId")
    public SingleResult<Map<String, Object>> getUserListByUserId(SearchRequestDto dto) throws Exception {


        return responseService.getSingleResult(userService.selUserListByUserId(dto));

    }

    @GetMapping("/user/user-list/{orgCode}")
    public SingleResult<Map<String, Object>> getUserListByOrgCode(@PathVariable String orgCode) throws Exception {

        return responseService.getSingleResult(userService.selUserListByOrgCode(orgCode));

    }

    @GetMapping("/user/{userId}/check")
    public SingleResult<Boolean> getUserCheck(@PathVariable String userId) throws Exception {

        return responseService.getSingleResult(userService.selUserIdCheck(userId));
    }

    @PostMapping("/user")
    public CommonResult postUser(@RequestBody UserRequestDto dto) throws Exception {

        userService.insUser(dto);
        return responseService.getSuccessResult();

    }
    @PatchMapping("/user")
    public CommonResult patchUser(@RequestBody UserPasswordRequestDto dto) throws Exception {

        userService.updUserPass(dto);

        return responseService.getSuccessResult();
    }
    @PutMapping("/user")
    public CommonResult putUser(@RequestBody UserRequestDto dto) throws Exception {

        userService.updUser(dto);
        return responseService.getSuccessResult();
    }
    @DeleteMapping("/user/{userId}")
    public CommonResult deleteUser(@PathVariable String userId, @RequestParam("leaveDate") String leaveDate) throws Exception {
        userService.delUser(userId,leaveDate);
        return responseService.getSuccessResult();
    }

    @GetMapping("/user/{userId}")
    public SingleResult<Map<String,Object>> getUserById(@PathVariable String userId) throws Exception {

        return responseService.getSingleResult(userService.selUserInfo(userId));
    }

    @PostMapping("/user/image")
    public CommonResult postUserProfileImage(UserUploadDto dto) throws Exception {

        userService.insUserProfileImage(dto);
        return responseService.getSuccessResult();
    }
    @DeleteMapping("/user/image/{userId}")
    public CommonResult deleteUserProfileImage(@PathVariable String userId) throws Exception {

        userService.delUserProfileImage(userId);
        return responseService.getSuccessResult();
    }
    @GetMapping("/seat")
    public SingleResult<Map<String,Object>> getSeat() throws Exception {

        return responseService.getSingleResult(userService.selSeat());
    }
}
