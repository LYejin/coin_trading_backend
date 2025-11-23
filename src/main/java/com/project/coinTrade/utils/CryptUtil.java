package com.project.coinTrade.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CryptUtil {
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
}
