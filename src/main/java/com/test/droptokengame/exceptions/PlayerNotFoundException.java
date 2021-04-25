package com.test.droptokengame.exceptions;

public class PlayerNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String playerId;

	public PlayerNotFoundException(String playerId) {
		this.playerId = playerId;
	}
	
	public static PlayerNotFoundException playerNotFoundException(String playerId) {
		return new PlayerNotFoundException(playerId);
	}

	@Override
	public String getMessage() {
		return "Player with "+playerId+" is not found";
	}
	
}
