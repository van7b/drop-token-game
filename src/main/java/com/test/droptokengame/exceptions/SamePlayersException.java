package com.test.droptokengame.exceptions;

public class SamePlayersException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static SamePlayersException samePlayersException() {
		return new SamePlayersException();
	}

	@Override
	public String getMessage() {
		return "Game should have 2 unique players";
	}
	
	
}
