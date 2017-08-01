package org.akalasok.stocks.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Andrei Kalasok
 */
@Entity
public class Stock {

	@Id
	@GeneratedValue
	private int id;
	@NotNull
	@Size(min=2, max=30)
	private String name;

	@Min(0)
	@NotNull
	private BigDecimal currentPrice;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp lastUpdate;

	@PreUpdate
	@PrePersist
	protected void onUpdate() {
		lastUpdate = new Timestamp(new Date().getTime());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal price) {
		this.currentPrice = price;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
