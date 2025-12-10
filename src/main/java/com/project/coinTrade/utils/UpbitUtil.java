package com.project.coinTrade.utils;

import com.project.coinTrade.constants.KeyConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/*
* upbit 호출 하는 부분
* return 값은 객체/배열 구분 후
* 제네릭 으로 반환 값 틀리기 해주기 TypeReference
* */
@Component
public class UpbitUtil {
    static String accessKey = KeyConstants.ACCESS_KEY;
    static String secretKey = KeyConstants.SECRET_KEY;
    static String serverUrl = "https://api.upbit.com";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public UpbitUtil() {
        this.httpClient = HttpClientBuilder.create().build();
        this.objectMapper = new ObjectMapper();
    }

    public static String createJwt() {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public Object upbitApi(String url) throws Exception {

        String jwtToken = createJwt();

        String authenticationToken = "Bearer " + jwtToken;

        HttpGet request = new HttpGet(serverUrl + url);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", authenticationToken);

        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity == null) {
            throw new Exception("오류라니!!!");
        }

        String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

        json = json.trim();
        if (json.startsWith("[")) {
            // 배열 → List<Map>
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
            });
        } else if (json.startsWith("{")) {
            // 객체 → Map
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        }

        throw new RuntimeException("데이터가 이상함!!! 확인 요청 해야함 " + json);
    }




}
