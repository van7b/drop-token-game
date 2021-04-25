package com.test.droptokengame.services;

import java.util.List;

import com.test.droptokengame.exceptions.ColumnAlreadyFilledException;
import com.test.droptokengame.exceptions.ColumnOutOfBoundException;
import com.test.droptokengame.exceptions.GameNotFoundException;
import com.test.droptokengame.exceptions.GameNotInProgressException;
import com.test.droptokengame.exceptions.InvalidMoveNumberException;
import com.test.droptokengame.exceptions.InvalidPlayerTurnException;
import com.test.droptokengame.exceptions.PlayerNotFoundException;
import com.test.droptokengame.exceptions.SamePlayersException;
import com.test.droptokengame.models.DeleteResponse;
import com.test.droptokengame.models.Game;
import com.test.droptokengame.models.GetStateOfGameResponse;
import com.test.droptokengame.models.Move;
import com.test.droptokengame.models.MoveResponse;

public interface GameService {

	public void createNewGame(Game game) throws SamePlayersException;

	public List<String> getAllGamesInProgress();

	public GetStateOfGameResponse getStateOfTheGame(String gameId) throws GameNotFoundException;

	public Move postAMove(Move move, String gameId, String playerId) throws ColumnOutOfBoundException, 
			ColumnAlreadyFilledException, InvalidPlayerTurnException, PlayerNotFoundException, GameNotFoundException;
	
	public MoveResponse getMoveByMoveNumber(Game game, String gameId, int moveNumber) throws InvalidMoveNumberException, GameNotFoundException;

	public DeleteResponse deletePlayerId(Game game, String gameId, String playerId) throws GameNotInProgressException, PlayerNotFoundException, GameNotFoundException;
	
}
