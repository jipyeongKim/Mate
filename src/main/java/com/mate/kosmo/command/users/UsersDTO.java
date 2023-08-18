package com.mate.kosmo.command.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersDTO {
    private int unum;// 회원 번호
    private String id;// 회원 아이디
    private String password;// 회원 비밀번호
    private String name;// 회원 이름
    private String nick;// 회원 닉네임
    private String tel;// 회원 연락처
    private String profile;// 회원 프로필 이미지 주소
    private String udate;// 회원 가입일
    private String enabled;// 회원 활성화 여부
    private String authority;// 회원 권한

    private String token;// 회원 식별자 jwt 토큰
    private String msg;// 데이터 처리에 대한 메시지

}// UsersDTO