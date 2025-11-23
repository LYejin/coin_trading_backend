package com.project.coinTrade.service;

import com.project.coinTrade.dto.CoinDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoinServiceTest {

    @Autowired
    private CoinService coinService;

    @Test
    void testGetAllCoins() {
//        List<CoinDTO> coins = coinService.getAllCoins();
//        assertNotNull(coins);
//        assertTrue(coins.size() >= 0);
    }

    @Test
    void testServiceTestMethod() throws Exception {
//        Map<String, Object> result = coinService.test(null, null);
//        assertNotNull(result);
//        assertEquals("테스트 성공!", result.get("msg"));
    }
}
