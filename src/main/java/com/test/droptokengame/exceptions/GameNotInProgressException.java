package com.test.droptokengame.exceptions;

public class GameNotInProgressException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String gameId;

	public GameNotInProgressException(String gameId) {
		super();
		this.gameId = gameId;
	}
	
	public static GameNotInProgressException gameNotInProgressException(String gameId) {
		return new GameNotInProgressException(gameId);
	}

	@Override
	public String getMessage() {
		return "Game with "+gameId+" is finished";
	}
	
	
}
