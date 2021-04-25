package com.test.droptokengame.exceptions;

public class ColumnOutOfBoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer column;

	public ColumnOutOfBoundException(Integer column) {
		this.column = column;
	}
	
	public static ColumnOutOfBoundException columnOutOfBoundException(Integer column) {
		return new ColumnOutOfBoundException(column);
	}

	@Override
	public String getMessage() {
		return "Column "+column+" is out of range";
	}
	
	
}
