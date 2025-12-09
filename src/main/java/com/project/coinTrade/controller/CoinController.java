package com.project.coinTrade.controller;

import com.project.coinTrade.annotation.LogExecution;
import com.project.coinTrade.dto.APIResult;
import com.project.coinTrade.service.CoinService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/coin")
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @PostMapping("/test")
    @LogExecution
    public Map<String, Object> test(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        coinService.test(paramMap, request, response);

        Map<String, Object> hii = new HashMap<>();

        hii.put("hii", "jiii");

        return hii;
    }

    @PostMapping("/order")
    @LogExecution
    public APIResult order(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        APIResult apiResult = coinService.order(paramMap, request, response);
        return apiResult;
    }

    @PostMapping("/balance")
    @LogExecution
    public APIResult balance(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        APIResult apiResult = coinService.balance(paramMap, request, response);
        return apiResult;
    }

    @PostMapping("/chance")
    @LogExecution
    public APIResult chance(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        APIResult apiResult = coinService.chance(paramMap, request, response);
        return apiResult;
    }
}
