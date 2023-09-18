package com.wordgame.wordguesser.dao;

import com.wordgame.wordguesser.exceptions.InvalidIdException;
import com.wordgame.wordguesser.pojo.Game;
import com.wordgame.wordguesser.pojo.GameAndInstance;
import com.wordgame.wordguesser.pojo.GameInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Simulates a DAO for storing games and instances.
 * <p>
 * They are stored together as we often want both.
 * <p>
 * In reality this would be a database.
 * We could decide if we wanted to use SQL and do joins, of if it made more sense to denormalize so that instances
 * also contained the full game details
 */
public class GameDAO {
    List<Game> games;
    List<GameInstance> gameInstances;

    public GameDAO() {
        this.games = new ArrayList<>();
        this.gameInstances = new ArrayList<>();
    }

    public void storeGame(Game game) {
        this.games.add(game);
    }

    public void storeInstance(GameInstance gameInstance) {
        this.gameInstances.add(gameInstance);
    }

    public GameAndInstance getInstanceById(String instanceId) {
        // NOTE: Doing two separate calls is not ideal.
        // Could address this by doing a SQL join OR storing all game details on instance to denormalize
        Optional<GameInstance> instance = gameInstances.stream()
                .filter(i -> i.getInstanceId().equals(instanceId))
                .findFirst();
        if (instance.isEmpty()) {
            throw new InvalidIdException("No instance with given id exists");
        }
        Optional<Game> game = games.stream()
                .filter(i -> i.getGameId().equals(instance.get().getGameId()))
                .findFirst();
        if (game.isEmpty()) {
            throw new InvalidIdException("No game with given id exists");
        }
        return new GameAndInstance(game.get(), instance.get());
    }

    public Optional<Game> findUnplayedGameForUser(String user) {
        List<String> gameIdsPlayed = gameInstances.stream().filter(i -> i.getUserId().equals(user)).map(GameInstance::getGameId).toList();
        List<Game> games = this.games.stream().filter(g -> !gameIdsPlayed.contains(g.getGameId())).toList();
        if (games.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(games.get(ThreadLocalRandom.current().nextInt(games.size())));
        }
    }

    public OptionalDouble getAverageAttemptsForGame(String gameId) {
        return gameInstances.stream()
                .filter(i -> i.getGameId().equals(gameId))
                .filter(GameInstance::isSuccess)
                .mapToInt(GameInstance::getAttempts).average();
    }
}
