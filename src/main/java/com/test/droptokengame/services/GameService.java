package com.test.droptokengame.services;

import java.util.List;

import com.test.droptokengame.exceptions.GameCollectionException;
import com.test.droptokengame.models.DeleteResponse;
import com.test.droptokengame.models.Game;
import com.test.droptokengame.models.GetStateOfGameResponse;
import com.test.droptokengame.models.Move;
import com.test.droptokengame.models.MoveResponse;

public interface GameService {

	public void createNewGame(Game game) throws GameCollectionException;

	public List<String> getAllGamesInProgress();

	public GetStateOfGameResponse getStateOfTheGame(String gameId) throws GameCollectionException;

	public Move postAMove(Move move, Game game, String gameId, String playerId) throws GameCollectionException;
	
	public MoveResponse getMoveByMoveNumber(Game game, String gameId, int moveNumber) throws GameCollectionException;

	public DeleteResponse deletePlayerId(Game game, String gameId, String playerId) throws GameCollectionException;
	
	public void playGame(Game game, String gameId) throws GameCollectionException;
}
