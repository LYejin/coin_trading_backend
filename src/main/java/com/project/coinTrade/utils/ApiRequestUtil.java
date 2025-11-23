package com.project.coinTrade.utils;

import com.project.coinTrade.constants.KeyConstants;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class ApiRequestUtil {

    private final String accessKey = KeyConstants.ACCESS_KEY;
    private final String secretKey = KeyConstants.SECRET_KEY;

    // HMAC SHA256으로 서명 생성
    public String generateSignature(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }

    // Map을 쿼리 스트링으로 변환
    public String buildQueryString(Map<String, Object> params) {
        StringJoiner joiner = new StringJoiner("&");
        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                joiner.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error encoding query string", e);
        }
        return joiner.toString();
    }

    // API 호출용 URL 생성
    public String buildApiUrl(String baseUrl, Map<String, Object> params) {
        String queryString = buildQueryString(params);
        String signature = generateSignature(queryString);

        return baseUrl + "?" + queryString + "&accessKey=" + accessKey + "&signature=" + signature;
    }
}
