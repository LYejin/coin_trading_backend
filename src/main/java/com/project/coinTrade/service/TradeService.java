package com.project.coinTrade.service;

import com.project.coinTrade.utils.UpbitWebSocket;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    UpbitWebSocket upbitWebSocket;

    public void subscribeBtcTrade() throws Exception {
        String msg = """
        [
          {"ticket":"trade"},
          {"type":"trade","codes":["KRW-BTC"]}
        ]
        """;

        upbitWebSocket.sendSubscribeMessage(msg);
    }
}
