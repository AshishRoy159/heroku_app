package com.mindfire.bicyclesharing.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{

	private static final long serialVersionUID = 6800772300151670010L;

	private String message;
	private HttpStatus httpStatus;
	
	public CustomException(String message, HttpStatus httpStatus){
		this.message = message;
		this.httpStatus = httpStatus;
	}

	/**
	 * @return the httpStatus
	 */
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	/**
	 * @param httpStatus the httpStatus to set
	 */
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
