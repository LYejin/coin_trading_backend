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

    // SHA256 해시
    public static String sha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA256 hashing failed", e);
        }
    }

    // 간단한 null/empty 체크
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
