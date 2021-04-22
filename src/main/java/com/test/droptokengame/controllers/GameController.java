package com.test.droptokengame.controllers;

import java.net.URI;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.droptokengame.exceptions.GameCollectionException;
import com.test.droptokengame.models.Game;
import com.test.droptokengame.models.GetStateOfGameResponse;
import com.test.droptokengame.models.Move;
import com.test.droptokengame.services.GameService;

@RestController
@RequestMapping("/drop_token")
public class GameController {

	@Autowired
	private GameService gameService;
	
	@GetMapping("")
	public ResponseEntity<?> getAllGamesInProgress(){
		List<String> games = gameService.getAllGamesInProgress();
		return new ResponseEntity<>(games, games.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("")
	public ResponseEntity<?> createNewGame(@Valid @RequestBody Game game){
		try {
			gameService.createNewGame(game);
			String json = new ObjectMapper().writeValueAsString(game);
			Game g = new ObjectMapper().readValue(json, Game.class);
			return new ResponseEntity<>(g.getGameId(),HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{gameId}")
	public ResponseEntity<?> getStateOfTheGame(@PathVariable String gameId){
		try {
			GetStateOfGameResponse game = gameService.getStateOfTheGame(gameId);
			String json = new ObjectMapper().writeValueAsString(game);
			Game g = new ObjectMapper().readValue(json, Game.class);
			return new ResponseEntity<>(game, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/{gameId}/{playerId}")
	public ResponseEntity<?> postAMove(@RequestBody Move move, Game game, @PathVariable String gameId, @PathVariable String playerId){
		try {
			move = gameService.postAMove(move, game, gameId, playerId);
			game.getMoves().add(move);

			URI location = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("drop_token/{gameId}/move/{move_number}")
					.buildAndExpand(gameId, game.getMoves().size())
					.toUri();
			
			return ResponseEntity.created(location).build();
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{gameId}/move/{moveNumber}")
	public ResponseEntity<?> getMoveByMoveNumber(Game game, @PathVariable String gameId, @PathVariable int moveNumber){
		try {
			return new ResponseEntity<>(gameService.getMoveByMoveNumber(game, gameId, moveNumber), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{gameId}/{playerId}")
	public ResponseEntity<?> deletePlayerId(Game game, @PathVariable String gameId, @PathVariable String playerId) throws GameCollectionException{
		try {
			return new ResponseEntity<>(gameService.deletePlayerId(game, gameId, playerId), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
