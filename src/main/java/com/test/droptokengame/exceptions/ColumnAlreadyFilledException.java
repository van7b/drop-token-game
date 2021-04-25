package com.test.droptokengame.exceptions;

public class ColumnAlreadyFilledException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer column;

	public ColumnAlreadyFilledException(Integer column) {
		this.column = column;
	}
	
	public static ColumnAlreadyFilledException columnAlreadyFilledException(Integer column) {
		return new ColumnAlreadyFilledException(column);
	}

	@Override
	public String getMessage() {
		return "Column "+column+" is already full";
	}

	
}
