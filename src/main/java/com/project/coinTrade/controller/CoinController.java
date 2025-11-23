package com.project.coinTrade.controller;

import com.project.coinTrade.service.CoinService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @PostMapping("/coin/test")
    public Map<String, Object> test(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return coinService.test(paramMap, request, response);
    }
}
