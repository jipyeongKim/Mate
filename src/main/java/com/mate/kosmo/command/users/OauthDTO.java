package com.mate.kosmo.command.users;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class OauthDTO {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakao_client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakao_client_secret;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakao_token_uri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakao_user_info_uri;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakao_redirect_uri;

    @Value("${kakao.logout-uri}")
    private String kakao_logout_uri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naver_client_id;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naver_client_secret;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String naver_token_uri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String naver_user_info_uri;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naver_redirect_uri;

    @Value("${naver.logout-uri}")
    private String naver_logout_uri;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String google_client_id;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String google_client_secret;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String google_token_uri;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String google_user_info_uri;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String google_redirect_uri;

    @Value("${google.logout-uri}")
    private String google_logout_uri;

    private String access_token;

}// OauthDTO