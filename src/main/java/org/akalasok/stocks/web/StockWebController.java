/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.akalasok.stocks.web;

import javax.validation.Valid;

import org.akalasok.stocks.domain.Stock;
import org.akalasok.stocks.domain.StockRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Andrei Kalasok
 */
@Controller
@RequestMapping("/")
public class StockWebController {

	private final StockRepository repository;

	public StockWebController(StockRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public ModelAndView list() {
		Iterable<Stock> stocks = this.repository.findAll();
		return new ModelAndView("stocks/list", "stocks", stocks);
	}

	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Stock stock) {
		return new ModelAndView("stocks/view", "stock", stock);
	}

	@GetMapping(params = "form")
	public String createForm(@ModelAttribute Stock stock) {
		return "stocks/form";
	}

	@PostMapping
	public ModelAndView create(@Valid Stock newStock, BindingResult result,
	                           RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return new ModelAndView("stocks/form", "formErrors", result.getAllErrors());
		}
		Stock stock;
		String message;
		if (newStock.getId() > 0) {
			stock = repository.findOne(newStock.getId());
			stock.setPrice(newStock.getPrice());
			stock.setName(newStock.getName());
			message = "Successfully updated a stock";
		} else {
			stock = newStock;
			message = "Successfully created a new stock";
		}
		stock = this.repository.save(stock);
		redirect.addFlashAttribute("globalMessage", message);
		return new ModelAndView("redirect:/{stock.id}", "stock.id", stock.getId());
	}

	@GetMapping(value = "delete/{id}")
	public ModelAndView delete(@PathVariable("id") int id) {
		this.repository.delete(id);
		Iterable<Stock> stocks = this.repository.findAll();
		return new ModelAndView("stocks/list", "stocks", stocks);
	}

	@GetMapping(value = "modify/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Stock stock) {
		return new ModelAndView("stocks/form", "stock", stock);
	}
}
