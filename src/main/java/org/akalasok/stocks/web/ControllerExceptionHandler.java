package org.akalasok.stocks.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Andrei Kalasok
 */
@ControllerAdvice
public class ControllerExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> handleException(HttpServletRequest req, Exception e) {

		logger.error("Unhandled exception on '{} {}'",
				req.getMethod(),
				req.getRequestURI() + (req.getQueryString() != null ? req.getQueryString() : ""),
				e
		);
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}