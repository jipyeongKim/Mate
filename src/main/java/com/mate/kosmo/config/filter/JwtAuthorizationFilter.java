package com.mate.kosmo.config.filter;

import com.mate.kosmo.command.common.exception.ErrorCode;
import com.mate.kosmo.command.users.AuthConstants;
import com.mate.kosmo.config.exception.BusinessExceptionHandler;
import com.mate.kosmo.utility.users.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Resource
    private TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        List<String> list = Arrays.asList(
                "/spring/users/security/join",
                "/spring/users/security/login",
                "/spring/users/security/logout",
                "/spring/users/kakao/login",
                "/spring/users/kakao/logout",
                "/spring/users/naver/login",
                "/spring/users/naver/logout",
                "/spring/users/google/login",
                "/spring/users/google/logout"
                );

        if (list.contains(request.getRequestURI())){

            filterChain.doFilter(request,response);
            return;
        }// if

        if (request.getMethod().equalsIgnoreCase("OPTIONS")){
            filterChain.doFilter(request,response);
            return;
        }// if

        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        System.out.println("헤더 확인: " + header);

        try{
            if (header != null && !header.equalsIgnoreCase("")) {
                String token = tokenUtil.getTokenFromHeader(header);

                if (tokenUtil.isValidToken(token)) {
                    String id = tokenUtil.getIDfromToken(token);
                    System.out.println("아이디 확인: " + id);

                    if (id != null && !id.equalsIgnoreCase("")) {
                        filterChain.doFilter(request,response);
                    } else {
                        throw new BusinessExceptionHandler("회원이 아닙니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
                    }// else

                } else {
                    throw new BusinessExceptionHandler("토큰이 유효하지 않습니다",ErrorCode.BUSINESS_EXCEPTION_ERROR);
                }// else

            } else {
                throw new BusinessExceptionHandler("토큰이 없습니다",ErrorCode.BUSINESS_EXCEPTION_ERROR);
            }// else

        } catch (Exception e) {
            e.printStackTrace();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = reponseJson(e);
            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }// catch

    }// doFilterInternal

    private JSONObject reponseJson(Exception e){
        String msg = "";

        if (e instanceof ExpiredJwtException) {
            msg = "토큰이 만료되었습니다";
        } else if (e instanceof SignatureException) {
            msg = "토큰이 허가되지 않았습니다";
        } else if (e instanceof JwtException) {
            msg = "토큰 발행 중 오류 발생!";
        } else {
            msg = "토큰 오류 발생!";
        }// else

        Map<String,Object> map = new HashMap<>();
        map.put("status",401);
        map.put("code",9999);
        map.put("msg",msg);
        map.put("reason",e.getMessage());
        JSONObject jsonObject = new JSONObject(map);

        return jsonObject;

    }// reponseJson

}// JwtAuthorizationFilter