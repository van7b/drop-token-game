package com.test.droptokengame.exceptions;

public class InvalidMoveNumberException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int moveNumber;

	public InvalidMoveNumberException(int moveNumber) {
		this.moveNumber = moveNumber;
	}
	
	public static InvalidMoveNumberException invalidMoveNumberException(int moveNumber) {
		return new InvalidMoveNumberException(moveNumber);
	}

	@Override
	public String getMessage() {
		return "Move number "+moveNumber+" is not valid";
	}
	
	
}
