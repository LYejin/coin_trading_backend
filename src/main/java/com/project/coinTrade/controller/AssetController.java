package com.project.coinTrade.controller;

import com.project.coinTrade.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coin")
public class AssetController {

    @Autowired
    AssetService assetService;

    @PostMapping("/assets")
    public Object getTotalAccounts() throws Exception {
        return assetService.getTotalAccounts();
    }

}
