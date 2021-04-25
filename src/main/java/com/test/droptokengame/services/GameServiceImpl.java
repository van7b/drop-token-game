package com.test.droptokengame.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.test.droptokengame.models.MovesType;
import com.test.droptokengame.models.State;
import com.test.droptokengame.repositories.GameRepository;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepository gameRepository;
	
	@Override
	public List<String> getAllGamesInProgress() {
		List<Game> games = gameRepository.findAll();
		if(games.size() > 0) {
			List<Game> gamesInProgress = gameRepository.findAllGamesInProgress(State.IN_PROGRESS);
			
			List<String> gamesAllInProgress = new ArrayList<>();
			for(Game game: gamesInProgress) {
				gamesAllInProgress.add(game.getGameId());
			}
			return gamesAllInProgress;
		} else {
			return new ArrayList<String>();
		}
	}
	
	
	@Override
	public void createNewGame(Game game) throws SamePlayersException {
		if(game.getPlayer1().equals(game.getPlayer2())) {
			throw SamePlayersException.samePlayersException();
		}
		game.setState(State.IN_PROGRESS);
		game.setPlayer1(game.getPlayer1());
		game.setPlayer2(game.getPlayer2());
		game.setBoard(new String[game.getNoOfRows()][game.getNoOfColumns()]);
		gameRepository.save(game);
	}


	@Override
	public GetStateOfGameResponse getStateOfTheGame(String gameId) throws GameNotFoundException {
		Optional<Game> gameOptional = gameRepository.findByGameId(gameId);
		if(gameOptional.isPresent()) {
			GetStateOfGameResponse response = new GetStateOfGameResponse();
			response.setPlayers(gameOptional.get().getPlayers());
			response.setState(gameOptional.get().getState());
			if(gameOptional.get().getState() == State.DONE) {
				response.setWinner(gameOptional.get().getWinner());
			} else {
				response.setWinner(null);
			}
			return response;
		} else {
			throw GameNotFoundException.gameNotFound(gameId);
		}
	}
	
	@Override
	public Move postAMove(Move move, String gameId, String playerId)
		throws ColumnOutOfBoundException, 
				ColumnAlreadyFilledException, 
				InvalidPlayerTurnException, 
				PlayerNotFoundException, 
				GameNotFoundException {
		
			Optional<Game> gameOptional = gameRepository.findByGameId(gameId);
			if(gameOptional.isPresent() && gameOptional.get().getState().equals(State.IN_PROGRESS)) {
				Optional<String> player = gameRepository.findByPlayerId(playerId);
				if(player.isPresent()) {
					if((gameOptional.get().getLastPlayer()==null) || (!gameOptional.get().getLastPlayer().equals(playerId))) {
						move.setPlayerId(playerId);
						move.setColumn(move.getColumn());
						
						Integer column = move.getColumn();
						if(column <= 0 || column > gameOptional.get().getNoOfColumns()) {
							throw ColumnOutOfBoundException.columnOutOfBoundException(column);
						} else {
								Game game = gameOptional.get();
								game.getMoves().add(move);
								game.setLastPlayer(playerId);
								
								String board[][] = game.getBoard();
								int i;
								for(i=game.getNoOfRows()-1;i>=0;i--) {
									if(board[i][column-1] == null) {
										board[i][column-1] = playerId;
										game.setBoard(board);
										break;
									}
								}	
								if(i<0) {
									throw ColumnAlreadyFilledException.columnAlreadyFilledException(column);
								}
								
								
								if(checkWinner(game)) {
									game.setState(State.DONE);
									game.setWinner(playerId);
								}
								gameRepository.save(game);
								return move;
						}
						
						
					}else {
						throw InvalidPlayerTurnException.invalidPlayerTurnException(playerId);
					}
					
				} else {
					throw PlayerNotFoundException.playerNotFoundException(playerId);
				}
			} else {
				throw GameNotFoundException.gameNotFound(gameId);
			}
	}
	

	@Override
	public MoveResponse getMoveByMoveNumber(Game game, String gameId, int moveNumber) 
			throws InvalidMoveNumberException, GameNotFoundException{
		
		Optional<Game> gameOptional = gameRepository.findByGameId(gameId);
		if(gameOptional.isPresent()) {
			if(moveNumber <= gameOptional.get().getMoves().size()) {
				MoveResponse moveResponse = new MoveResponse();
				moveResponse.setType(MovesType.MOVE);
				moveResponse.setPlayer(gameOptional.get().getMoves().get(moveNumber-1).getPlayerId());
				moveResponse.setColumn(gameOptional.get().getMoves().get(moveNumber-1).getColumn());
				return moveResponse;
			} else {
				throw InvalidMoveNumberException.invalidMoveNumberException(moveNumber);
			}
		} else {
			throw GameNotFoundException.gameNotFound(gameId);
		}
	}

	@Override
	public DeleteResponse deletePlayerId(Game game, String gameId, String playerId) 
			throws GameNotInProgressException, PlayerNotFoundException, GameNotFoundException {
		Optional<Game> gameOptional = gameRepository.findByGameId(gameId);
		if(gameOptional.isPresent()) {
			Optional<String> player = gameRepository.findByPlayerId(playerId);
			if(player.isPresent()) {
				if(gameOptional.get().getState() == State.IN_PROGRESS) {
					DeleteResponse response = new DeleteResponse(MovesType.QUIT, playerId);
					gameRepository.deleteByPlayerId(playerId);
					return response;
				} else {
					throw GameNotInProgressException.gameNotInProgressException(gameId);
				}
			}else {
				throw PlayerNotFoundException.playerNotFoundException(playerId);
			}
		} else {
			throw GameNotFoundException.gameNotFound(gameId);
		}
		
	}
	
	private boolean checkWinner(Game game) {
		String board[][] = game.getBoard();
		
		int i, j;
		//check row wise
		for(i=0;i<game.getNoOfRows();i++) {
			if(board[i][0] != null) {
				String player = board[i][0];
				for(j=1;j<game.getNoOfColumns();j++) {
					if(board[i][j] == null || !board[i][j].equals(player)) {
						break;
					}
				}
				if(j==game.getNoOfColumns()) {
					return true;
				}
			}
		}
		
		
		//check column wise
		for(j=0;j<game.getNoOfColumns();j++) {
			if(board[0][j] != null) {
				String player = board[0][j];
				for(i=1;i<game.getNoOfRows();i++) {
					if(board[i][j] == null || !board[i][j].equals(player)) {
						break;
					}
				}
				if(i==game.getNoOfRows()) {
					return true;
				}
			}
		}
		
		//check diagonal
		String player = "";
		if(board[0][0] != null) {
			player = board[0][0];
			for(i=1;i<game.getNoOfRows();i++) {
				if(!board[i][i].equals(player)) {
					break;
				}
			}
			if(i==game.getNoOfRows()) {
				return true;
			}
		}
		
		//check diagonal(top right to bottom left)
		if(board[0][game.getNoOfColumns()-1] != null) {
			player = board[0][game.getNoOfColumns()-1];
			for(i=1;i<game.getNoOfRows();i++) {
				if(!board[game.getNoOfRows()-i-1][i].equals(player)) {
					break;
				}
			}
			if(i==game.getNoOfRows()) {
				return true;
			}
		}
		
		return false;
	}
	
}
