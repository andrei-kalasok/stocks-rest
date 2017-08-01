package org.akalasok.stocks.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.math.BigDecimal;

import org.akalasok.stocks.domain.Stock;
import org.akalasok.stocks.domain.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Andrei Kalasok
 */
@RestController
public class StockRestController {

	@Autowired
	private StockRepository repository;

	@RequestMapping(value = "/api/stocks/{id}", method = GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ResponseEntity<Stock> getStock(@PathVariable int id) {
		Stock stock = repository.findOne(id);
		return stock != null ? ResponseEntity.ok(stock) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/api/stocks", method = GET, produces = "application/json; charset=UTF-8")
	public Iterable<Stock> getStocks() {
		return repository.findAll();
	}

	@RequestMapping(value = "/api/stocks", method = POST, consumes = "application/json; charset=UTF-8")
	public int addStock(@RequestBody Stock stock) {
		return repository.save(stock).getId();
	}

	@RequestMapping(value = "/api/stocks/{id}", method = PUT)
	@ResponseBody
	public ResponseEntity<Stock> updateStock(@PathVariable int id, @RequestBody BigDecimal price) {
		Stock stock = repository.findOne(id);
		if (stock == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		stock.setPrice(price);

		return ResponseEntity.ok(repository.save(stock));
	}
}
