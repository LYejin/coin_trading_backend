package com.project.coinTrade.integration;

import com.project.coinTrade.dto.CoinDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoinIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private com.project.coinTrade.mapper.CoinMapper coinMapper;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 초기화
        //coinMapper.getAllCoins().clear(); // Mapper에서 DB 초기화
    }

    @Test
    void testGetAllCoins() {
        // Mapper를 통해 직접 데이터 추가
        Map<String, Object> param = new HashMap<>();
        param.put("id", 1);
        param.put("name", "BTC");
        param.put("price", 50000);
        // 직접 Mapper SQL 호출 또는 @Sql 스크립트 사용 가능
        // coinMapper.insertCoin(param);  // 실제 Insert 메서드가 있다면

        ResponseEntity<CoinDTO[]> response = restTemplate.getForEntity("/coin/all", CoinDTO[].class);
        assertEquals(200, response.getStatusCodeValue());

        CoinDTO[] coins = response.getBody();
        assertNotNull(coins);
    }

    @Test
    void testCoinTestEndpoint() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/coin/test", Map.class);
        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> result = response.getBody();
        assertNotNull(result);
        assertEquals("테스트 성공!", result.get("msg"));
    }
}
