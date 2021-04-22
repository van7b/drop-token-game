package com.test.droptokengame.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.test.droptokengame.exceptions.GameCollectionException;
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
	public void createNewGame(Game game) throws GameCollectionException {
		if(game.getPlayer1().equals(game.getPlayer2())) {
			throw new GameCollectionException(GameCollectionException.SamePlayersException());
		}
		game.setState(State.IN_PROGRESS);
		game.setPlayer1(game.getPlayer1());
		game.setPlayer2(game.getPlayer2());
		game.setBoard(new String[game.getNoOfRows()][game.getNoOfColumns()]);
		gameRepository.save(game);
	}


	@Override
	public GetStateOfGameResponse getStateOfTheGame(String gameId) throws GameCollectionException {
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
			throw new GameCollectionException(GameCollectionException.GameNotFoundException(gameId));
		}
	}
	
	@Override
	public Move postAMove(Move move, Game game, String gameId, String playerId)
		throws GameCollectionException {
			Optional<Game> gameOptional = gameRepository.findByGameId(gameId);
			if(gameOptional.isPresent()) {
				Optional<String> player = gameRepository.findByPlayerId(playerId);
				if(player.isPresent()) {
					move.setPlayerId(playerId);
					move.setColumn(move.getColumn());
					
					Integer column = move.getColumn();
					if(column <= 0 || column > gameOptional.get().getNoOfColumns()) {
						throw new GameCollectionException(GameCollectionException.ColumnOutOfBoundException(column));
					} else {
						game = gameOptional.get();
						game.getMoves().add(move);
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
							throw new GameCollectionException(GameCollectionException.ColumnAlreadyFilledException(column));
						}
						gameRepository.save(game);
						return move;
					}
				} else {
					throw new GameCollectionException(GameCollectionException.PlayerNotFoundException(playerId));
				}
			} else {
				throw new GameCollectionException(GameCollectionException.GameNotFoundException(gameId));
			}
	}
	

	@Override
	public MoveResponse getMoveByMoveNumber(Game game, String gameId, int moveNumber) throws GameCollectionException {
		Optional<Game> gameOptional = gameRepository.findByGameId(gameId);
		if(gameOptional.isPresent()) {
			if(moveNumber <= gameOptional.get().getMoves().size()) {
				MoveResponse moveResponse = new MoveResponse();
				moveResponse.setType(MovesType.MOVE);
				moveResponse.setPlayer(gameOptional.get().getMoves().get(moveNumber-1).getPlayerId());
				moveResponse.setColumn(gameOptional.get().getMoves().get(moveNumber-1).getColumn());
				return moveResponse;
			} else {
				throw new GameCollectionException(GameCollectionException.InvalidMoveNumberException(moveNumber));
			}
		} else {
			throw new GameCollectionException(GameCollectionException.GameNotFoundException(gameId));
		}
	}

	@Override
	public DeleteResponse deletePlayerId(Game game, String gameId, String playerId) throws GameCollectionException {
		Optional<Game> gameOptional = gameRepository.findByGameId(gameId);
		if(gameOptional.isPresent()) {
			Optional<String> player = gameRepository.findByPlayerId(playerId);
			if(player.isPresent()) {
				if(gameOptional.get().getState() == State.IN_PROGRESS) {
					DeleteResponse response = new DeleteResponse(MovesType.QUIT, playerId);
					gameRepository.deleteByPlayerId(playerId);
					return response;
				} else {
					throw new GameCollectionException(GameCollectionException.GameNotInProgressException(gameId));
				}
			}else {
				throw new GameCollectionException(GameCollectionException.PlayerNotFoundException(playerId));
			}
		} else {
			throw new GameCollectionException(GameCollectionException.GameNotFoundException(gameId));
		}
		
	}

	@Override
	public void playGame(Game game, String gameId) throws GameCollectionException {
		Optional<Game> gameOptional = gameRepository.findByGameId(gameId);
		if(gameOptional.isPresent()) {
			game = gameOptional.get();
			if(game.getState().equals(State.DONE)) {
				throw new GameCollectionException(GameCollectionException.GameNotInProgressException(gameId));
			}
			
			Move move = new Move();
			int count = 0;
			while(true) {
//				move = (count%2 == 0) ? postAMove(new Move(), game, gameId, game.getPlayer1()) 
//						: postAMove(new Move(), game, gameId, game.getPlayer2());
				
				if(count%2 == 0) {
					System.out.println("Enter a column number by Player1 - "+game.getPlayer1()+": ");
					move = postAMove(new Move(), game, gameId, game.getPlayer1());
				} else {
					System.out.println("Enter a column number by Player2 - "+game.getPlayer2()+": ");
					move = postAMove(new Move(), game, gameId, game.getPlayer2());
				}
				
				count++;
				gameRepository.save(game);
				checkWinner(game);
				
				if(game.getState().equals(State.DONE)) {
					break;
				} else if(count == game.getNoOfRows()*game.getNoOfColumns() && game.getState().equals(State.IN_PROGRESS)) {
					game.setState(State.DONE);
					break;
				}
			}
		}
	}
	
	private void checkWinner(Game game) {
		String board[][] = game.getBoard();
		
		int i, j;
		//check row wise
		for(i=0;i<game.getNoOfRows();i++) {
			String player = board[i][0];
			for(j=1;j<game.getNoOfColumns();j++) {
				if(board[i][j] == null || !board[i][j].equals(player)) {
					break;
				}
			}
			if(j==game.getNoOfColumns()) {
				game.setState(State.DONE);
				game.setWinner(player);
				gameRepository.save(game);
				return;
			}
		}
		
		
		//check column wise
		for(j=0;j<game.getNoOfColumns();j++) {
			String player = board[0][j];
			for(i=1;i<game.getNoOfRows();i++) {
				if(board[i][j] == null || board[i][j].equals(player)) {
					break;
				}
			}
			if(i==game.getNoOfRows()) {
				game.setState(State.DONE);
				game.setWinner(player);
				gameRepository.save(game);
				return;
			}
		}
		
		//check diagonal
		String player = board[0][0];
		for(i=1;i<game.getNoOfRows();i++) {
			if(!board[i][i].equals(player)) {
				break;
			}
		}
		if(i==game.getNoOfRows()) {
			game.setState(State.DONE);
			game.setWinner(player);
			gameRepository.save(game);
			return;
		}
		
		//check diagonal(top right to bottom left)
		player = board[0][game.getNoOfColumns()-1];
		for(i=1;i<game.getNoOfRows();i++) {
			if(!board[game.getNoOfRows()-i-1][i].equals(player)) {
				break;
			}
		}
		if(i==game.getNoOfRows()) {
			game.setState(State.DONE);
			game.setWinner(player);
			gameRepository.save(game);
			return;
		}
	}
	
}
