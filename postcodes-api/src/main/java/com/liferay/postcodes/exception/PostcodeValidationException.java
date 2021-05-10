package com.liferay.postcodes.exception;

import java.util.List;

public class PostcodeValidationException extends RuntimeException {

	private static final long serialVersionUID = 2902702444158455869L;

	public PostcodeValidationException() {
	}

	public PostcodeValidationException(String msg) {
		super(msg);
	}

	public PostcodeValidationException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public PostcodeValidationException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Custom constructor taking a list as a parameter.
	 * 
	 * @param errors
	 */
	public PostcodeValidationException(List<String> errors) {

		super(String.join(",", errors));
		_errors = errors;
	}

	public List<String> getErrors() {

		return _errors;
	}

	private List<String> _errors;
}
