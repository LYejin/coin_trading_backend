package com.project.coinTrade.service;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@Service("CoinService")
public class CoinService implements CoinServiceImpl{

    @Override
    public Map<String, Object> test(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 구현 내용
        return null;
    }
}
