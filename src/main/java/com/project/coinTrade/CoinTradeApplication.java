package com.project.coinTrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CoinTradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinTradeApplication.class, args);
	}

}
