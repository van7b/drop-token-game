package com.test.droptokengame.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class Move {
	private String playerId;
	
	@NotNull
	@PositiveOrZero
	private Integer column;
	
	public Move() {
	}

	public Move(String playerId, Integer column) {
		super();
		this.playerId = playerId;
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	
}

