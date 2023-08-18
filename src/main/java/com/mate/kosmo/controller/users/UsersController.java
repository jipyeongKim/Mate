package com.mate.kosmo.controller.users;

import com.mate.kosmo.command.common.res.ApiResponse;
import com.mate.kosmo.command.common.success.SuccessCode;
import com.mate.kosmo.command.users.AuthConstants;
import com.mate.kosmo.command.users.UsersDTO;
import com.mate.kosmo.service.impl.users.UsersServiceImpl;
import com.mate.kosmo.utility.users.TokenUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/spring/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersServiceImpl usersService;
    private final TokenUtil tokenUtil;

    @PostMapping("/security/join")
    public UsersDTO createUser(@RequestBody UsersDTO usersDTO) {
        if (usersService.insert(usersDTO) != 1) {
            usersDTO.setMsg("회원 가입 성공");
        } else {
            usersDTO.setMsg("회원 가입 실패");
        }// else
        return usersDTO;
    }// createUser

    @RequestMapping("/{social}/login")
    public UsersDTO socialLogin(@PathVariable String social, @RequestParam Map map, UsersDTO usersDTO) {
        if (social.equals("kakao")) {
            System.out.println("Kakao 인가 코드 확인: " + map.get("code"));
        } else if (social.equals("naver")) {
            System.out.println("Naver 인가 코드 확인: " + map.get("code"));
            System.out.println("Naver 인가 State 확인: " + map.get("state"));
        } else if (social.equals("google")) {
            System.out.println("Google 인가 코드 확인: " + map.get("code"));
        }// else if

        map.put("social", social);
        map.put(social, usersService.getSocialLoginToken(map));

        map.put("user", social);
        usersDTO = usersService.getSocialUserInfo(map);

        Optional<UsersDTO> user = usersService.selectOne(usersDTO);

        if (user.isPresent()) {
            usersDTO = user.get();
            usersDTO.setMsg(social);
        }// if

        return usersDTO;

    }// socialLogin

    @GetMapping("/{social}/logout")
    public ResponseEntity<String> socialLogout(@PathVariable String social, @RequestParam Map map) {
        map.put("social", social);
        map.put("logout", social);
        return usersService.socialLogout(map);
    }// socialLogout

    @GetMapping("/info")
    public UsersDTO getUserInfo(@RequestParam int unum, UsersDTO usersDTO) {
        usersDTO.setUnum(unum);
        return usersService.selectOne(usersDTO).get();
    }// getUserInfo

    @PostMapping("/test")
    private ResponseEntity<ApiResponse> testToken(@RequestBody UsersDTO usersDTO) {
        String token = tokenUtil.setToken(usersDTO);

        ApiResponse apiResponse = ApiResponse.builder()
                .result(AuthConstants.TOKEN_TYPE + " " + token)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }// testToken

    @DeleteMapping("/delete")
    public UsersDTO deleteUser(@RequestBody UsersDTO usersDTO) {
        if (usersService.delete(usersDTO) != 1) {
            usersDTO.setMsg("회원정보 삭제가 실패했습니다.");
        } else {
            usersDTO.setMsg("회원정보 삭제가 성공했습니다.");
        }// else
        return usersDTO;
    }// deleteUser

    @PutMapping("/edit")
    public UsersDTO setUserInfo(@RequestBody UsersDTO usersDTO) {
        if (usersService.update(usersDTO) != 1) {
            usersDTO.setMsg("회원정보 수정이 실패했습니다.");
        } else {
            usersDTO.setMsg("회원정보 수정이 성공했습니다.");
        }// else
        return usersDTO;
    }// setUserInfo

}// UsersController