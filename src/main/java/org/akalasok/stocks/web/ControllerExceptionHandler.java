package org.akalasok.stocks.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.TransactionSystemException;
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
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(new ErrorMessage(status.value(), e.getMessage()), status);
	}

	@ExceptionHandler(value = ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<?> handleOptimisticLockingException(HttpServletRequest req, Exception e){
		logger.warn("Race condition price update on '{} {}'",
				req.getMethod(),
				req.getRequestURI() + (req.getQueryString() != null ? req.getQueryString() : ""));

		HttpStatus status = HttpStatus.CONFLICT;
		String message = "Simultaneous price update, please check current price and try again";
		return new ResponseEntity<Object>(new ErrorMessage(status.value(), message), status);
	}

	@ExceptionHandler(value = TransactionSystemException.class)
	public ResponseEntity<?> handleTransactionSystemException(HttpServletRequest req, TransactionSystemException e) {
		if (e.getRootCause() instanceof ConstraintViolationException) {
			return handleConstraintViolationException(req, (ConstraintViolationException) e.getRootCause());
		}

		return handleException(req, e);
	}

	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {
		StringBuilder message = new StringBuilder();
		for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
			if(message.length() > 0){
				message.append(", ");
			}
			message.append(constraintViolation.getPropertyPath())
					.append(" - ")
					.append(constraintViolation.getMessage());
		}

		return badRequest(req, message.toString());
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpServletRequest req, Exception e) {
		String message = e.getMessage();
		int ind = message.indexOf(':');
		return badRequest(req, ind > 0 ? message.substring(0, ind) : message);
	}

	private ResponseEntity<?> badRequest(HttpServletRequest req, String message) {
		logger.warn("Bad request on '{} {}': {}",
				req.getMethod(),
				req.getRequestURI() + (req.getQueryString() != null ? req.getQueryString() : ""),
				message);

		HttpStatus status = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<>(new ErrorMessage(status.value(), message), status);
	}

	public static class ErrorMessage {

		private final int statusCode;
		private final String message;

		public ErrorMessage(int statusCode, String message) {
			this.statusCode = statusCode;
			this.message = message;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public String getMessage() {
			return message;
		}
	}
}