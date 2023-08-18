package com.mate.kosmo.command.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final static int SUCCESS = 200;
    private final static int NOT_FOUND = 400;
    private final static int FAILED = 500;

    private final static String SUCCESS_MESSAGE = "요청 성공!";
    private final static String NOT_FOUND_MESSAGE = "페이지 없음!";
    private final static String FAILED_MESSAGE = "서버 오류!";

    private final static String INVALID_ACCESS_TOKEN = "ACCESS TOKEN INVALID!";
    private final static String INVALID_REFRESH_TOKEN = "REFRESH TOKEN INVALID!";
    private final static String NOT_EXPIRED_TOKEN = "ACCESS TOKEN NOT EXPIRED!";

    private final ApiResponseHeader header;

    private final Map<String, T> body;

    public static <T> ApiResponse<T> success(String name, T body) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);
        return new ApiResponse(new ApiResponseHeader(SUCCESS, SUCCESS_MESSAGE), map);
    }// success

    public static <T> ApiResponse<T> fail() {
        return new ApiResponse(new ApiResponseHeader(FAILED, FAILED_MESSAGE), null);
    }// fail

    public static <T> ApiResponse<T> invalidAccessToken() {
        return new ApiResponse(new ApiResponseHeader(FAILED, INVALID_ACCESS_TOKEN), null);
    }// invalidAccessToken

    public static <T> ApiResponse<T> invalidRefreshToken() {
        return new ApiResponse(new ApiResponseHeader(FAILED, INVALID_REFRESH_TOKEN), null);
    }// invalidRefreshToken

    public static <T> ApiResponse<T> notExpiredToken() {
        return new ApiResponse(new ApiResponseHeader(FAILED, NOT_EXPIRED_TOKEN), null);
    }// notExpiredToken

}// ApiResponse