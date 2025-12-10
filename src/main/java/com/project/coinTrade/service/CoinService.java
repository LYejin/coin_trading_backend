package com.project.coinTrade.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.coinTrade.annotation.LogExecution;
import com.project.coinTrade.constants.KeyConstants;
import com.project.coinTrade.dto.APIResult;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;

import static com.project.coinTrade.constants.UrlConstants.*;

@Service("CoinService")
public class CoinService implements CoinServiceImpl{

    @Override
    @LogExecution
    public Map<String, Object> test(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 구현 내용
        return null;
    }

    @LogExecution
    @Override
    public APIResult order(Map<String, Object> paramMap, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        APIResult apiResult = new APIResult();
        String accessKey = KeyConstants.ACCESS_KEY;
        String secretKey = KeyConstants.SECRET_KEY;

        Map<String, String> params = new HashMap<>();
        params.put("market", paramMap.get("market").toString());
        params.put("side", paramMap.get("side").toString()); // bid - 매수, ask - 매도
        params.put("ord_type", paramMap.get("orderType").toString());
        params.put("price", paramMap.get("price").toString());
        params.put("smp_type", "reduce");

        if (paramMap.get("orderType") != null && paramMap.get("orderType").equals("limit")) {
            params.put("volume", paramMap.get("volume").toString());
        }

        String queryString = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + String.valueOf(e.getValue()))
                .collect(Collectors.joining("&"));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : md.digest()) {
            sb.append(String.format("%02x", b));
        }
        String queryHash = sb.toString();

        Algorithm algorithm = Algorithm.HMAC512(secretKey.getBytes(StandardCharsets.UTF_8));
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authHeader = "Bearer " + jwtToken;

        String jsonBody = new Gson().toJson(params);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UPBIT_BASE_URL + ORDER_PATH_TEST)
                .post(RequestBody.create(jsonBody, okhttp3.MediaType.parse("application/json; charset=utf-8")))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authHeader)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = Objects.requireNonNull(response.body()).string();
            System.out.println(response.code());
            System.out.println(body);
            apiResult.setCode(response.code());
            apiResult.setData(body);
        }
        return apiResult;
    }

    @LogExecution
    @Override
    public APIResult balance(Map<String, Object> paramMap, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        APIResult apiResult = new APIResult();
        String accessKey = KeyConstants.ACCESS_KEY;
        String secretKey = KeyConstants.SECRET_KEY;

        Algorithm algorithm = Algorithm.HMAC512(secretKey.getBytes(StandardCharsets.UTF_8));
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authHeader = "Bearer " + jwtToken;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UPBIT_BASE_URL + ACCOUNT_PATH)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authHeader)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = Objects.requireNonNull(response.body()).string();
            System.out.println(response.code());
            System.out.println(body);
            apiResult.setCode(response.code());
            apiResult.setData(body);
        }
        return apiResult;
    }

    @LogExecution
    @Override
    public APIResult chance(Map<String, Object> paramMap, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        APIResult apiResult = new APIResult();
        String accessKey = KeyConstants.ACCESS_KEY;
        String secretKey = KeyConstants.SECRET_KEY;

        Map<String, String> params = new HashMap<>();
        params.put("market", paramMap.get("market").toString());

        String queryString = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + String.valueOf(e.getValue()))
                .collect(Collectors.joining("&"));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : md.digest()) {
            sb.append(String.format("%02x", b));
        }
        String queryHash = sb.toString();

        Algorithm algorithm = Algorithm.HMAC512(secretKey.getBytes(StandardCharsets.UTF_8));
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authHeader = "Bearer " + jwtToken;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UPBIT_BASE_URL + CHANCE_PATH + "?" + queryString)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authHeader)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = Objects.requireNonNull(response.body()).string();
            System.out.println(response.code());
            System.out.println(body);
            apiResult.setCode(response.code());
            apiResult.setData(body);
        }
        return apiResult;
    }
}
