package com.test.droptokengame.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.droptokengame.exceptions.ColumnAlreadyFilledException;
import com.test.droptokengame.exceptions.ColumnOutOfBoundException;
import com.test.droptokengame.exceptions.GameCollectionException;
import com.test.droptokengame.exceptions.GameNotFoundException;
import com.test.droptokengame.exceptions.GameNotInProgressException;
import com.test.droptokengame.exceptions.InvalidMoveNumberException;
import com.test.droptokengame.exceptions.InvalidPlayerTurnException;
import com.test.droptokengame.exceptions.NoGamesInProgressException;
import com.test.droptokengame.exceptions.PlayerNotFoundException;
import com.test.droptokengame.exceptions.SamePlayersException;
import com.test.droptokengame.models.DeleteResponse;
import com.test.droptokengame.models.Game;
import com.test.droptokengame.models.GetStateOfGameResponse;
import com.test.droptokengame.models.Move;
import com.test.droptokengame.models.MoveResponse;
import com.test.droptokengame.repositories.GameRepository;
import com.test.droptokengame.services.GameService;

@RestController
@RequestMapping("/drop_token")
public class GameController {

	@Autowired
	private GameService gameService;
	
	@Autowired
	private GameRepository repository;
	
	@GetMapping("")
	public ResponseEntity<?> getAllGamesInProgress() throws NoGamesInProgressException{
		List<String> games = gameService.getAllGamesInProgress();
		if(games.size() > 0) {
			return new ResponseEntity<>(games, HttpStatus.OK);
		}else {
			throw NoGamesInProgressException.noGamesInProgressException();
		}
	}
	
	@PostMapping("")
	public ResponseEntity<?> createNewGame(@Valid @RequestBody Game game) 
			throws JsonProcessingException, SamePlayersException{
		gameService.createNewGame(game);
		String json = new ObjectMapper().writeValueAsString(game);
		Game g = new ObjectMapper().readValue(json, Game.class);
		
		return new ResponseEntity<>(g.getGameId(), HttpStatus.OK);
	}
	
	@GetMapping("/{gameId}")
	public ResponseEntity<?> getStateOfTheGame(@PathVariable String gameId) 
			throws GameNotFoundException{
		GetStateOfGameResponse game = gameService.getStateOfTheGame(gameId);
		return new ResponseEntity<>(game, HttpStatus.OK);
	}
	
	@PostMapping("/{gameId}/{playerId}")
	public ResponseEntity<?> postAMove(@RequestBody Move move, @PathVariable String gameId, @PathVariable String playerId) 
			throws JsonProcessingException, ColumnOutOfBoundException, ColumnAlreadyFilledException, InvalidPlayerTurnException, 
			PlayerNotFoundException, GameNotFoundException{
		
		move = gameService.postAMove(move, gameId, playerId);
		Optional<Game> game = repository.findByGameId(gameId);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("drop_token/{gameId}/move/{move_number}")
				.buildAndExpand(gameId, game.get().getMoves().size())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/{gameId}/move/{moveNumber}")
	public ResponseEntity<?> getMoveByMoveNumber(Game game, @PathVariable String gameId, @PathVariable int moveNumber) 
			throws InvalidMoveNumberException, GameNotFoundException{
		MoveResponse response = gameService.getMoveByMoveNumber(game, gameId, moveNumber);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{gameId}/{playerId}")
	public ResponseEntity<?> deletePlayerId(Game game, @PathVariable String gameId, @PathVariable String playerId) 
			throws GameCollectionException, GameNotInProgressException, PlayerNotFoundException, GameNotFoundException{
		DeleteResponse response = gameService.deletePlayerId(game, gameId, playerId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
