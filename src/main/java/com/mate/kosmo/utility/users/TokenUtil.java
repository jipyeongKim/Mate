package com.mate.kosmo.utility.users;

import com.mate.kosmo.command.users.UsersDTO;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class TokenUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String setToken(UsersDTO usersDTO) {

        JwtBuilder jwtBuilder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(usersDTO))
                .setSubject(String.valueOf(usersDTO.getUnum()))
                .signWith(SignatureAlgorithm.HS256,createSignature())
                .setExpiration(createExpiredDate());

        return jwtBuilder.compact();
    }// setToken

    public String getUserInfo(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }// getUserInfo

    public boolean isValidToken(String token){
        try {
            Claims claims = getClaimsFromToken(token);

            System.out.println("만료 시간: " + claims.getExpiration());
            System.out.println("아이디: " + claims.get("id"));
            System.out.println("닉네임: " + claims.get("nick"));

            return true;

        } catch (ExpiredJwtException e){
            System.out.println("만료된 토큰!");
            return false;
        } catch (JwtException e){
            System.out.println("변조된 토큰!");
            return false;
        } catch (NullPointerException e){
            System.out.println("토큰이 없습니다");
            return false;
        }// catch

    }// isValidToken

    public String getTokenFromHeader(String header){
        return header.split(" ")[1];
    }// getTokenFromHeader

    private Date createExpiredDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,8);
        return calendar.getTime();
    }// createExpiredDate

    private Map<String,Object> createHeader(){
        Map<String,Object> header = new HashMap<>();

        header.put("typ","JWT");
        header.put("alg","HS256");
        header.put("regDate",System.currentTimeMillis());
        return header;
    }// createHeader

    private Map<String,Object> createClaims(UsersDTO usersDTO){
        Map<String,Object> claims = new HashMap<>();

        System.out.println("아이디: " + usersDTO.getId());
        System.out.println("닉네임: " + usersDTO.getNick());

        claims.put("id",usersDTO.getId());
        claims.put("nick",usersDTO.getNick());
        return claims;
    }// createClaims

    private Key createSignature(){
        byte[] byteSecretKey = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(byteSecretKey,SignatureAlgorithm.HS256.getJcaName());
    }// createSignature

    private Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();
    }// getClaimsFromToken

    public String getIDfromToken(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.get("id").toString();
    }// getIDfromToken

}// TokenUtil