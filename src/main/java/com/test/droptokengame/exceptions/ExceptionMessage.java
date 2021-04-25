package com.test.droptokengame.exceptions;

public class ExceptionMessage {

	private int statusCode;
	private String message;
	private String description;
	
	public ExceptionMessage(int statusCode, String message, String description) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.description = description;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

	public String getDescription() {
		return description;
	}
	
	
}
