package org.akalasok.stocks.domain;

import static junit.framework.TestCase.assertTrue;
import static org.akalasok.stocks.Application.stock;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Andrei Kalasok
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class StockRepositoryTest {

	@Autowired
	StockRepository repository;

	@Test
	public void lastUpdate() {
		Stock stock = stock("StockToUpdate", 5.);
		repository.save(stock);
		Timestamp firstUpdate = stock.getLastUpdate();

		stock.setCurrentPrice(BigDecimal.TEN);
		repository.save(stock);

		assertTrue("LastUpdate field should be updated", stock.getLastUpdate().after(firstUpdate));
	}

}