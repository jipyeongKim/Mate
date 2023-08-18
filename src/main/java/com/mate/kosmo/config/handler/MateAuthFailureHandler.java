package com.mate.kosmo.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class MateAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("3.MateAuthFailureHandler 시작!");
        JSONObject jsonObject;
        String msg = "";

        if (exception instanceof AuthenticationServiceException) {
            msg = "로그인 정보가 틀립니다";
        } else if (exception instanceof BadCredentialsException) {
            msg = "비밀번호가 틀립니다";
        } else if (exception instanceof LockedException) {
            msg = "비활성 계정입니다";
        } else if (exception instanceof DisabledException) {
            msg = "회원이 아닙니다";
        } else if (exception instanceof AccountExpiredException) {
            msg = "만료된 계정입니다";
        } else if (exception instanceof CredentialsExpiredException) {
            msg = "만료된 인증입니다";
        }// else if

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();

        log.debug(msg);

        Map<String,Object> map = new HashMap<>();
        map.put("UserInfo",null);
        map.put("resultCode",9999);
        map.put("msg",msg);
        map.put("token",null);
        jsonObject = new JSONObject(map);

        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();

    }// onAuthenticationFailure

}// MateAuthFailureHandler