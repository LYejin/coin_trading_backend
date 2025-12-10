package com.project.coinTrade.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.coinTrade.utils.UpbitUtil;
import com.project.coinTrade.utils.UpbitWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssetService {

    @Autowired
    UpbitUtil upbitUtil;

    @Autowired
    UpbitWebSocket upbitWebSocket;

    public List<Map<String, Object>> getTotalAccounts() throws Exception {

        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Map<String, Object>> account = getAccounts();

        for(Map<String,Object> item : account) {

            // 구매한 코인?? 이라고 해야하나 들고있는 코인이라고 해야하나?? 고민중
            String code = (String) item.get("currency");

            // 코인을 구매 한게 아니기에 넘어 간다.
            if ("KRW".equals(code)) continue;

            // 보유수량
            double balance = Double.parseDouble((String) item.get("balance"));
            // 매수 평균가
            double avgBuyPrice = Double.parseDouble((String) item.get("avg_buy_price"));
            // 현재가
            String codes = "KRW-" + code;
            double currentPrice = getTickers(codes);

            // 총 금액(현재 평가 금액)
            double totalValue = balance * currentPrice;
            // 투자 원금(평단 기준 평가)
            double investAmount = balance * avgBuyPrice;
            // 수익금
            double profit = totalValue - investAmount;
            // 수익률
            double profitRate = (profit / investAmount) * 100;

            Map<String, Object> newCoinData = new HashMap<>();

            newCoinData.put("coin", "KRW-" + code);
            newCoinData.put("totalValue", totalValue);
            newCoinData.put("investAmount", investAmount);
            newCoinData.put("profit", profit);
            newCoinData.put("profitRate", profitRate);

            resultList.add(newCoinData);

        }

        return resultList;
    }


    public List<Map<String, Object>> getAccounts() throws Exception {

        Object result = upbitUtil.upbitApi("/v1/accounts");

        if (result instanceof List<?>) {
            return (List<Map<String, Object>>) result;
        }

        return Collections.emptyList();
    }

    public double getTickers(String code) throws Exception {

        String subscribeMsg =
                "[" +
                        "{\"ticket\":\"dynamic-ticker\"}," +
                        "{\"type\":\"ticker\",\"codes\":[\"" + code + "\"],\"isOnlyRealtime\":true}," +
                        "{\"format\":\"DEFAULT\"}" +
                        "]";

        String result = upbitWebSocket.sendAndReceiveOnce(subscribeMsg);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(result);
        Double tradePrice = node.path("trade_price").asDouble();

        return tradePrice;
    }
}
