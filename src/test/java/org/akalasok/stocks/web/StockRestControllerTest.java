package org.akalasok.stocks.web;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.akalasok.stocks.domain.Stock;
import org.akalasok.stocks.domain.StockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Andrei Kalasok
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockRestControllerTest {

	@LocalServerPort
	private int port;

	private String url;

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private StockRepository repository;

	@Before
	public void setUp() {
		url = "http://localhost:" + port + "/api/stocks";
	}

	@Test
	public void getStockNotFound() {
		ResponseEntity<Stock> entity = callGetStockEndPoint(1);

		assertEquals(404, entity.getStatusCodeValue());
	}

	@Test
	public void getStock() {
		StockAnswer stock = new StockAnswer(1, "StockToGet", BigDecimal.ONE);
		when(repository.findOne(stock.getId())).thenReturn(stock);

		ResponseEntity<Stock> entity = callGetStockEndPoint(stock.getId());

		assertEquals(200, entity.getStatusCodeValue());
		assertStock(stock, entity.getBody());
	}

	@Test
	public void getStocks() {
		List<Stock> stocks = asList(
				new StockAnswer(1, "GetStock1", BigDecimal.ONE),
				new StockAnswer(2, "GetStock2", BigDecimal.TEN));
		when(repository.findAll()).thenReturn(stocks);

		Stock[] respStocks = restTemplate.getForObject(url, Stock[].class);

		assertEquals(2, respStocks.length);
		assertStock(stocks.get(0), respStocks[0]);
		assertStock(stocks.get(1), respStocks[1]);
	}

	@Test
	public void addStock() {
		StockAnswer stock = new StockAnswer("StockToAdd", BigDecimal.TEN);
		doAnswer(stock).when(repository).save(any(Stock.class));

		HttpStatus status = callAddStockEndPoint(stock);

		assertEquals("Wrong response status", 200, status.value());
		assertTrue("The stock hasn't been stored", stock.isStored());
	}

	@Test
	public void updateNonExistingStock() {
		ResponseEntity<Stock> entity = callUpdateStockEndPoint(1, BigDecimal.ZERO);

		assertEquals(404, entity.getStatusCodeValue());
	}

	@Test
	public void updateStock() {
		StockAnswer storedStock = new StockAnswer(1, "StockToUpdate", BigDecimal.ONE);
		when(repository.findOne(storedStock.getId())).thenReturn(storedStock);
		StockAnswer stockToUpdate = new StockAnswer(1, "StockToUpdate", BigDecimal.TEN);
		doAnswer(stockToUpdate).when(repository).save(any(Stock.class));

		ResponseEntity<Stock> entity = callUpdateStockEndPoint(storedStock.getId(), BigDecimal.TEN);

		assertEquals(200, entity.getStatusCodeValue());
		assertStock(stockToUpdate, entity.getBody());
		assertTrue("The storedStock hasn't been updated", stockToUpdate.isStored());
	}

	@Test
	public void updateWithNullPrice() {
		StockAnswer storedStock = new StockAnswer(1, "StockToUpdate", BigDecimal.ONE);
		when(repository.findOne(storedStock.getId())).thenReturn(storedStock);

		ResponseEntity<Stock> entity = callUpdateStockEndPoint(1, null);

		assertEquals(400, entity.getStatusCodeValue());
	}

	private ResponseEntity<Stock> callGetStockEndPoint(Integer id) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Accept-Type", "application/json");
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		return restTemplate.exchange(
				url + "/" + id,
				HttpMethod.GET,
				httpEntity,
				Stock.class
		);
	}

	private HttpStatus callAddStockEndPoint(Stock stock) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", "application/json");
		HttpEntity<?> httpEntity = new HttpEntity<>(stock, headers);
		return restTemplate.exchange(
				url,
				HttpMethod.POST,
				httpEntity,
				String.class
		).getStatusCode();
	}

	private ResponseEntity<Stock> callUpdateStockEndPoint(int id, BigDecimal price) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", "application/json");
		HttpEntity<?> httpEntity = new HttpEntity<>(price, headers);
		return restTemplate.exchange(
				url + "/" + id,
				HttpMethod.PUT,
				httpEntity,
				Stock.class
		);
	}

	private static void assertStock(Stock stock1, Stock stock2) {
		assertEquals(stock1.getName(), stock2.getName());
		assertEquals(stock1.getCurrentPrice(), stock2.getCurrentPrice());
	}

	private static class StockAnswer extends Stock implements Answer {

		private boolean stored = false;

		private StockAnswer(int id, String name, BigDecimal price) {
			setId(id);
			setName(name);
			setCurrentPrice(price);

		}

		private StockAnswer(String name, BigDecimal price) {
			this(0, name, price);
		}

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			Stock stock = (Stock) invocation.getArguments()[0];
			assertStock(this, stock);
			stored = true;
			return stock;
		}


		private boolean isStored() {
			return stored;
		}
	}
}