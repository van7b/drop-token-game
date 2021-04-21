package com.test.droptokengame.models;

public class MoveResponse {
	private MovesType type;
	private String player;
	private Integer column;
	
	public MoveResponse() {
		
	}
	
	public MoveResponse(MovesType type, String player, Integer column) {
		super();
		this.type = type;
		this.player = player;
		this.column = column;
	}

	public MovesType getType() {
		return type;
	}

	public void setType(MovesType type) {
		this.type = type;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "MoveResponse [type=" + type + ", player=" + player + ", column=" + column + "]";
	}
	
	
}
