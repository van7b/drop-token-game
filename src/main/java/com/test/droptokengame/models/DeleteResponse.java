package com.test.droptokengame.models;

public class DeleteResponse {
	private MovesType type;
	private String playerId;
	
	public DeleteResponse() {
		
	}
	
	public DeleteResponse(MovesType type, String playerId) {
		super();
		this.type = type;
		this.playerId = playerId;
	}

	public MovesType getType() {
		return type;
	}

	public void setType(MovesType type) {
		this.type = type;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	@Override
	public String toString() {
		return "DeleteResponse [type=" + type + ", playerId=" + playerId + "]";
	}
	
	
}
