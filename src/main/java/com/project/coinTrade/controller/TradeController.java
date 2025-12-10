package com.project.coinTrade.controller;

import com.project.coinTrade.annotation.LogExecution;
import com.project.coinTrade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TradeController {

    @Autowired
    TradeService service;

    @GetMapping("/upbit/trade")
    @LogExecution
    public ResponseEntity<String> subscribeTradeBtc() throws Exception {
        service.subscribeBtcTrade();
        return ResponseEntity.ok("Upbit myAsset subscribed!");
    }


}
