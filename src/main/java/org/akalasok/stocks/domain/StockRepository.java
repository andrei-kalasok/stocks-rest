package org.akalasok.stocks.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Andrei Kalasok
 */
public interface StockRepository extends CrudRepository<Stock, Integer> {
}
