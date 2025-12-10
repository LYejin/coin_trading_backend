package com.project.coinTrade.dto;

import lombok.Data;

@Data
public class AssetDto {
    // 예: "KRW", "BTC", "ETH"
    private String currency;

    // 주문 가능 잔고
    private String balance;

    // 주문 중인 잠금 잔고
    private String locked;

    // 매수 평균가
    private String avgBuyPrice;

    // 기준 통화 (보통 "KRW")
    private String unitCurrency;
}
