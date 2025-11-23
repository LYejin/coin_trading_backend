package com.project.coinTrade.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

public class CommonUtil {

    private CommonUtil() {} // 인스턴스화 방지

    // UUID 생성
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // Map → Query String 변환
    public static String buildQueryString(Map<String, Object> params) {
        StringJoiner joiner = new StringJoiner("&");
        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                joiner.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()) + "="
                        + URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8.name()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Query string encoding failed", e);
        }
        return joiner.toString();
    }
}
