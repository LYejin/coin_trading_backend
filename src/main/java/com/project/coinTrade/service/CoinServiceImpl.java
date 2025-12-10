package com.project.coinTrade.service;

import com.project.coinTrade.annotation.LogExecution;
import com.project.coinTrade.dto.APIResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface CoinServiceImpl {

    Map<String, Object> test(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception;

    @LogExecution
    APIResult order(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception;

    @LogExecution
    APIResult balance(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception;

    @LogExecution
    APIResult chance(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
