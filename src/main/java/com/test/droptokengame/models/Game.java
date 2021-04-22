package com.test.droptokengame.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Game")
public class Game {

	@Id
	private String gameId;
	
	@NotNull
	@Size(min=2, max=2, message = "Two players are required to play the game")
	private List<String> players;
	
	
	@NotNull(message = "Rows count is required")
	private int noOfRows;
	
	@NotNull(message = "Columns count is required")
	private int noOfColumns;
	
	private State state;
	private String winner;
	
	private List<Move> moves;
	
	private String player1;
	
	private String player2;
	
	private String[][] board;
	
	public Game() {
		this.players = new ArrayList<>();
		this.moves = new ArrayList<>();
		this.board = new String[this.noOfRows][this.noOfColumns];
	}
	
	public Game(String gameId, List<String> players, int noOfRows, int noOfColumns,
			State state, String winner, List<Move> moves) {
		super();
		this.gameId = gameId;
		this.players = players;
		this.noOfRows = noOfRows;
		this.noOfColumns = noOfColumns;
		this.state = state;
		this.winner = winner;
		this.moves = moves;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	public String getPlayer1() {
		return players.get(0);
	}
	
	public String getPlayer2() {
		return players.get(1);
	}
	
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
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

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(Vector<Move> moves) {
		this.moves = moves;
	}

	public String[][] getBoard() {
		return board;
	}

	public void setBoard(String[][] board) {
		this.board = board;
	}	
	
	
	
	
}
