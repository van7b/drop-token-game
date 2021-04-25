package com.test.droptokengame.exceptions;

public class NoGamesInProgressException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static NoGamesInProgressException noGamesInProgressException() {
		return new NoGamesInProgressException();
	}

	@Override
	public String getMessage() {
		return "No Games in progress";
	}

	
}
