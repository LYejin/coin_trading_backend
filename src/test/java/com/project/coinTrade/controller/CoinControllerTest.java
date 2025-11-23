package com.project.coinTrade.controller;

import com.project.coinTrade.dto.CoinDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoinControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllCoins() {
//        ResponseEntity<CoinDTO[]> response = restTemplate.getForEntity("/coin/all", CoinDTO[].class);
//        assertEquals(200, response.getStatusCodeValue());
//        CoinDTO[] coins = response.getBody();
//        assertNotNull(coins);
    }

    @Test
    void testCoinTestEndpoint() {
//        ResponseEntity<Map> response = restTemplate.getForEntity("/coin/test", Map.class);
//        assertEquals(200, response.getStatusCodeValue());
//        Map<String, Object> result = response.getBody();
//        assertNotNull(result);
//        assertEquals("테스트 성공!", result.get("msg"));
    }
}
