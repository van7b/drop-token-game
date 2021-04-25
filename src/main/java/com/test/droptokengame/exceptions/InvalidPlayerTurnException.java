package com.test.droptokengame.exceptions;

public class InvalidPlayerTurnException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String player;

	public InvalidPlayerTurnException(String player) {
		this.player = player;
	}
	
	public static InvalidPlayerTurnException invalidPlayerTurnException(String player) {
		return new InvalidPlayerTurnException(player);
	}

	@Override
	public String getMessage() {
		return "Not "+player+"'s turn";
	}
	
}
