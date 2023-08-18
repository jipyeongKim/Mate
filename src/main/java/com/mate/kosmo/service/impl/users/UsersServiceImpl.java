package com.mate.kosmo.service.impl.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.mate.kosmo.command.users.*;
import com.mate.kosmo.model.users.UsersDAO;
import com.mate.kosmo.service.MateService;
import com.mate.kosmo.utility.users.TokenUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements MateService<UsersDTO> {

    private final UsersDAO usersDAO;
    private final OauthDTO oauthDTO;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenUtil tokenUtil;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final MultiValueMap<Object, Object> body;
    private final ObjectMapper objectMapper;
    private final JsonParser jsonParser;

    @Override
    public int insert(UsersDTO usersDTO) {
        usersDTO.setPassword(bCryptPasswordEncoder.encode(usersDTO.getPassword()));

        if (usersDTO.getMsg() != null && !usersDTO.getMsg().equals("")) {
            switch (usersDTO.getMsg()) {
                case "kakao":
                    return usersDAO.createKakaoUser(usersDTO);
                case "naver":
                    return usersDAO.createNaverUser(usersDTO);
                case "google":
                    return usersDAO.createGoogleUser(usersDTO);
            }// switch
        }// if

        return usersDAO.createUser(usersDTO);
    }// insert

    public Optional<UsersDTO> login(UsersDTO usersDTO) {
        if (usersDAO.login(usersDTO).isPresent()) {
            usersDAO.login(usersDTO).get().setToken(tokenUtil.setToken(usersDAO.login(usersDTO).get()));
        }// if
        return usersDAO.login(usersDTO);
    }// login

    @Override
    public Optional<UsersDTO> selectOne(UsersDTO usersDTO) {
        Optional<UsersDTO> user = null;

        if (usersDTO.getMsg() != null && !usersDTO.getMsg().equals("")) {
            switch (usersDTO.getMsg()) {
                case "kakao":
                    user = usersDAO.getUserInfoByKakaoId(usersDTO);

                    if (user.isPresent()) {
                        usersDTO = user.get();
                    } else {
                        insert(usersDTO);
                    }// else

                    return login(usersDTO);
                case "naver":
                    user = usersDAO.getUserInfoByNaverId(usersDTO);

                    if (user.isPresent()) {
                        usersDTO = user.get();
                    } else {
                        insert(usersDTO);
                    }// else

                    return login(usersDTO);
                case "google":
                    user = usersDAO.getUserInfoByGoogleId(usersDTO);

                    if (user.isPresent()) {
                        usersDTO = user.get();
                    } else {
                        insert(usersDTO);
                    }// else

                    return login(usersDTO);
            }// switch
        }// if

        return usersDAO.getUserInfo(usersDTO);

    }// selectOne

    @Override
    public List<UsersDTO> selectList(List nums) {
        return null;
    }// selectList

    @Override
    public int delete(UsersDTO usersDTO) {
        return usersDAO.deleteUser(usersDTO);
    }// delete

    @Override
    public int update(UsersDTO usersDTO) {
        return usersDAO.setUserInfo(usersDTO);
    }// update

    public HttpEntity getRequestData(Map map) {
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        switch (map.get("social").toString()) {
            case "kakao":
                if (map.containsKey("code")) {
                    System.out.println("Map 밸류 확인 (1): " + map.values());
                    body.set("grant_type", "authorization_code");
                    body.set("client_id", oauthDTO.getKakao_client_id());
                    body.set("client_secret", oauthDTO.getKakao_client_secret());
                    body.set("redirect_uri", oauthDTO.getKakao_redirect_uri());
                    body.set("code", map.get("code").toString());
                }// if

                if (map.containsKey("kakao")) {
                    System.out.println("Map 밸류 확인 (2): " + map.values());
                    headers.set("Authorization", ((SocialDTO) map.get("kakao")).getToken_type() + " " + ((SocialDTO) map.get("kakao")).getAccess_token());
                }// if

                break;
            case "naver":
                if (map.containsKey("code")) {
                    System.out.println("Map 밸류 확인 (1): " + map.values());
                    body.set("grant_type", "authorization_code");
                    body.set("client_id", oauthDTO.getNaver_client_id());
                    body.set("client_secret", oauthDTO.getNaver_client_secret());
                    body.set("code", map.get("code").toString());
                    body.set("state", map.get("state").toString());
                }// if

                if (map.containsKey("naver")) {
                    System.out.println("Map 밸류 확인 (2): " + map.values());
                    headers.set("Authorization", ((SocialDTO) map.get("naver")).getToken_type() + " " + ((SocialDTO) map.get("naver")).getAccess_token());
                }// if

                break;
            case "google":
                if (map.containsKey("code")) {
                    System.out.println("Map 밸류 확인 (1): " + map.values());
                    body.set("grant_type", "authorization_code");
                    body.set("client_id", oauthDTO.getGoogle_client_id());
                    body.set("client_secret", oauthDTO.getGoogle_client_secret());
                    body.set("redirect_uri", oauthDTO.getGoogle_redirect_uri());
                    body.set("code", map.get("code").toString());
                }// if

                if (map.containsKey("google")) {
                    System.out.println("Map 밸류 확인 (2): " + map.values());
                    headers.set("Authorization", ((SocialDTO) map.get("google")).getToken_type() + " " + ((SocialDTO) map.get("google")).getAccess_token());
                }// if

                if (map.containsKey("logout")) {
                    System.out.println("Map 밸류 확인 (3): " + map.values());
                    body.set("token", oauthDTO.getAccess_token());
                }// if
                break;
        }// switch

        return new HttpEntity<>(body, headers);

    }// getRequestData

    public SocialDTO getSocialLoginToken(Map map) {
        HttpEntity<MultiValueMap<String, String>> RequestData = getRequestData(map);
        map.remove("code");
        SocialDTO socialDTO = new SocialDTO();
        ResponseEntity<String> response = null;
        String url = "";

        try {

            if (map.get("social").toString().equals("kakao")) {
                url = oauthDTO.getKakao_token_uri();
                response = restTemplate.exchange(url, HttpMethod.POST, RequestData, String.class);
                System.out.println("Kakao Token 확인: " + response.getBody());
            } else if (map.get("social").toString().equals("naver")) {
                url = oauthDTO.getNaver_token_uri();
                response = restTemplate.exchange(url, HttpMethod.POST, RequestData, String.class);
                System.out.println("Naver Token 확인: " + response.getBody());
            } else if (map.get("social").toString().equals("google")) {
                url = oauthDTO.getGoogle_token_uri();
                response = restTemplate.exchange(url, HttpMethod.POST, RequestData, String.class);
                System.out.println("Google Token 확인: " + response.getBody());
            }// else if

            socialDTO = objectMapper.readValue(response.getBody(), SocialDTO.class);
            oauthDTO.setAccess_token(socialDTO.getAccess_token());

        } catch (RestClientException e) {
            e.printStackTrace();
            System.out.println("요청이 실패했습니다");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("JSON 파싱이 실패했습니다");

        }// catch

        return socialDTO;

    }// getSocialLoginToken

    public UsersDTO getSocialUserInfo(Map map) {
        HttpEntity<MultiValueMap<String, String>> RequestData = getRequestData(map);
        UsersDTO usersDTO = new UsersDTO();
        ResponseEntity<String> response = null;
        String url = "";
        JsonElement jsonElement;
        JsonObject jsonObject;

        try {

            if (map.get("user").toString().equals("kakao")) {
                url = oauthDTO.getKakao_user_info_uri();
                response = restTemplate.exchange(url, HttpMethod.GET, RequestData, String.class);
                jsonElement = jsonParser.parse(response.getBody());
                jsonObject = jsonElement.getAsJsonObject().get("kakao_account").getAsJsonObject();
                System.out.println("Kakao 유저 정보 확인: " + jsonObject);
                usersDTO.setId(jsonObject.getAsJsonObject().get("email").getAsString());
                usersDTO.setPassword("1234");
                usersDTO.setName(jsonObject.getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString());
                if(jsonObject.getAsJsonObject().get("profile").getAsJsonObject().get("thumbnail_image_url") != null){
                    usersDTO.setProfile(jsonObject.getAsJsonObject().get("profile").getAsJsonObject().get("thumbnail_image_url").getAsString());
                }// if
                usersDTO.setProfile("Default");
                usersDTO.setMsg("kakao");

            } else if (map.get("user").toString().equals("naver")) {
                url = oauthDTO.getNaver_user_info_uri();
                response = restTemplate.exchange(url, HttpMethod.GET, RequestData, String.class);
                jsonElement = jsonParser.parse(response.getBody());
                jsonObject = jsonElement.getAsJsonObject().get("response").getAsJsonObject();

                System.out.println("Naver 유저 정보 확인: " + jsonObject);
                usersDTO.setId(jsonObject.get("email").getAsString());
                usersDTO.setPassword("1234");
                usersDTO.setName(jsonObject.get("name").getAsString());
                usersDTO.setNick(jsonObject.get("nickname").getAsString());
                usersDTO.setTel(jsonObject.get("mobile").getAsString());
                usersDTO.setProfile(jsonObject.get("profile_image").getAsString());
                usersDTO.setMsg("naver");

            } else if (map.get("user").toString().equals("google")) {
                url = oauthDTO.getGoogle_user_info_uri();
                response = restTemplate.exchange(url, HttpMethod.GET, RequestData, String.class);
                jsonElement = jsonParser.parse(response.getBody());
                jsonObject = jsonElement.getAsJsonObject();

                System.out.println("Google 유저 정보 확인: " + jsonObject);
                usersDTO.setId(jsonObject.get("email").getAsString());
                usersDTO.setPassword("1234");
                usersDTO.setName(jsonObject.get("name").getAsString());
                usersDTO.setProfile(jsonObject.get("picture").getAsString());
                usersDTO.setMsg("google");

            }// else if

        } catch (RestClientException e) {
            e.printStackTrace();
            System.out.println("요청이 실패했습니다");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("JSON 파싱이 실패했습니다");

        }// catch

        return usersDTO;

    }// getSocialUserInfo

    public ResponseEntity socialLogout(Map map) {
        HttpEntity<MultiValueMap<String, String>> params = getRequestData(map);
        ResponseEntity response = null;
        String url = "";

        try {

            if (map.get("social").toString().equals("kakao")) {
                url = oauthDTO.getKakao_logout_uri() + "?" + "client_id=" + oauthDTO.getKakao_client_id() + "&logout_redirect_uri=" + oauthDTO.getKakao_redirect_uri();
                response = restTemplate.exchange(url, HttpMethod.GET, params, String.class);
                System.out.println("로그아웃: " + response.getBody());
            } else if (map.get("social").toString().equals("naver")) {
                url = oauthDTO.getNaver_logout_uri() + "?" + "client_id=" + oauthDTO.getNaver_client_id() + "&client_secret=" + oauthDTO.getNaver_client_secret() + "&access_token=" + oauthDTO.getAccess_token() + "&grant_type=delete&service_provider=NAVER";
                response = restTemplate.exchange(url, HttpMethod.GET, params, String.class);
                System.out.println("로그아웃: " + response.getBody());
            } else if (map.get("social").toString().equals("google")) {
                url = oauthDTO.getGoogle_logout_uri();
                response = restTemplate.exchange(url, HttpMethod.POST, params, String.class);
                System.out.println("로그아웃: " + response);
            }// else if

        } catch (RestClientException e) {
            e.printStackTrace();

        }// catch

        return response;

    }// socialLogout

}// UsersServiceImpl