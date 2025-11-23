package com.project.coinTrade.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface CoinServiceImpl {

    Map<String, Object> test(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
