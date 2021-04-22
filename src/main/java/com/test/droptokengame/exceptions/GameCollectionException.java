package com.test.droptokengame.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GameCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GameCollectionException(String message) {
		super(message);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static String GameNotFoundException(String gameId) {
		return "Game with "+gameId+" not found! or Game has already done";
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static String PlayerNotFoundException(String playerId) {
		return "Player with "+playerId+" not found!";
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static String ColumnOutOfBoundException(Integer column) {
		return "Column number with "+column+" is invalid!";
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static String ColumnAlreadyFilledException(Integer column) {
		return "Column number with "+column+" is already filled";
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	public static String InvalidPlayerTurnException(String player) {
		return "Not your turn "+player;
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static String InvalidMoveNumberException(int moveNumber) {
		return "Move number with "+moveNumber+" is invalid!";
	}
	
	@ResponseStatus(HttpStatus.GONE)
	public static String GameNotInProgressException(String gameId) {
		return "Game with "+gameId+" is already Done!";
	}

	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public static String SamePlayersException() {
		return "Game should have 2 unique players!";
	}

}
