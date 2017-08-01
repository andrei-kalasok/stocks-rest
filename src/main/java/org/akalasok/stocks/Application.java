package org.akalasok.stocks;

import java.math.BigDecimal;

import org.akalasok.stocks.domain.Stock;
import org.akalasok.stocks.domain.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner loadData(StockRepository repository) {
		return (args) -> {
			repository.save(stock("Twitter", 17.09));
			repository.save(stock("Facebook", 171.55));
		};
	}

	public static Stock stock(String name, Double price) {
		Stock stock = new Stock();
		stock.setName(name);
		stock.setCurrentPrice(BigDecimal.valueOf(price));
		return stock;
	}
}