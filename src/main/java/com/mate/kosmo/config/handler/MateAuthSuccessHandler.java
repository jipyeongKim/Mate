package com.mate.kosmo.config.handler;

import com.mate.kosmo.command.users.UsersDTO;
import com.mate.kosmo.command.users.UsersDetailsDTO;
import com.mate.kosmo.utility.common.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class MateAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        System.out.println("3.MateAuthSuccessHandler 시작!");

        UsersDTO usersDTO = ((UsersDetailsDTO) authentication.getPrincipal()).getUsersDTO();

        JSONObject usersJSON = (JSONObject) ConvertUtil.convertObjectToJsonObject(usersDTO);

        Map<String,Object> map = new HashMap<>();

        JSONObject jsonObject;

        if (usersDTO.getEnabled().equals("0")) {
            map.put("user",usersJSON);
            map.put("resultCode",9001);
            map.put("token",null);
            map.put("msg","비활성 계정입니다");
            jsonObject = new JSONObject(map);
        } else {
            map.put("user",usersJSON);
            map.put("resultCode",200);
            map.put("token",usersDTO.getToken());
            jsonObject = new JSONObject(map);
        }// else

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();

    }// onAuthenticationSuccess

}// MateAuthSuccessHandler