package org.akalasok.stocks.domain;

import static org.akalasok.stocks.Application.stock;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Andrei Kalasok
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class StockRepositoryTest {

	@Autowired
	StockRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void lastUpdate() throws InterruptedException {
		Stock stock = stock("StockToSave", 5.);
		repository.save(stock);
		Timestamp lastUpdate = stock.getLastUpdate();

		stock.setPrice(BigDecimal.TEN);
		repository.save(stock);
		entityManager.flush();

		assertTrue("LastUpdate field should be updated", stock.getLastUpdate().after(lastUpdate));
	}
}