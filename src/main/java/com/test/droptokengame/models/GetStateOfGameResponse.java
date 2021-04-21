package com.test.droptokengame.models;

import java.util.List;

public class GetStateOfGameResponse {
	private List<String> players;
	private State state;
	private String winner;
	
	public GetStateOfGameResponse() {
		
	}

	public GetStateOfGameResponse(List<String> players, State state, String winner) {
		super();
		this.players = players;
		this.state = state;
		this.winner = winner;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	@Override
	public String toString() {
		return "GetStateOfGameResponse [players=" + players + ", state=" + state + ", winner=" + winner + "]";
	}
	
	
}
