package com.project.coinTrade.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoinMapperTest {

    @Autowired
    private CoinMapper coinMapper;

    @Test
    void testGetAllCoins() {
        //Map<String, Object> coins = coinMapper.getAllCoins();
        //assertNotNull(coins);
        //assertTrue(coins.size() >= 0);
    }
}
