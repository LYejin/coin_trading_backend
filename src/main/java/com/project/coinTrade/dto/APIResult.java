package com.project.coinTrade.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResult<T> {

    private int code;
    private String message;
    private T data;

    public static <T> APIResult<T> success(T data) {
        return APIResult.<T>builder()
                .code(200)
                .message("SUCCESS")
                .data(data)
                .build();
    }

    public static <T> APIResult<T> success(String message, T data) {
        return APIResult.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> APIResult<T> apiResult(int code, String message, T data) {
        return APIResult.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
}
