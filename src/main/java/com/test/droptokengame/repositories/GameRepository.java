package com.test.droptokengame.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.test.droptokengame.models.Game;
import com.test.droptokengame.models.State;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {

	@Query("{state : ?0}")
	List<Game> findAllGamesInProgress(State state);
	
	@Query("{gameId: ?0}")
	Optional<Game> findByGameId(String gameId);
	
	@Query(value = "{'players' : {'$all' : [?0]}}")
	Optional<String> findByPlayerId(String playerId);

	@Query(value="{'players' : {'$all' : [?0]}}", delete=true)
	void deleteByPlayerId(String playerId);

}
