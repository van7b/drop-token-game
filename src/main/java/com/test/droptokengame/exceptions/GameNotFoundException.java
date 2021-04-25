package com.test.droptokengame.exceptions;

public class GameNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String gameId;
	
	public GameNotFoundException(String gameId) {
		this.gameId = gameId;
	}

	public static GameNotFoundException gameNotFound(String gameId) {
		return new GameNotFoundException(gameId);
	}

	@Override
	public String getMessage() {
		return "Game with "+gameId+" not found";
	}
	
	
}
