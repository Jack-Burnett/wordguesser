package com.wordgame.wordguesser.service;

import com.wordgame.wordguesser.dao.GameDAO;
import com.wordgame.wordguesser.exceptions.WrongUserException;
import com.wordgame.wordguesser.pojo.Game;
import com.wordgame.wordguesser.pojo.GameAndInstance;
import com.wordgame.wordguesser.pojo.GameInstance;
import com.wordgame.wordguesser.pojo.Guess;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The play service is responsible for starting new games and letting the user make moves in ongoing games
 */
@AllArgsConstructor
public class PlayService {
    private GameDAO gameDAO;
    private GameGenerator gameGenerator;

    public GameAndInstance startNewGame(String username) {
        Optional<Game> game = this.gameDAO.findUnplayedGameForUser(username);
        if (game.isEmpty()) {
            // TODO Would be better to have a pre-generated list of games so as to speed this up
            //  and to be resilient to downtime on the api
            Game newGame = gameGenerator.generateGame();
            gameDAO.storeGame(newGame);
            game = Optional.of(newGame);
        }

        GameInstance gameInstance = new GameInstance(
                UUID.randomUUID().toString(),
                game.get().getGameId(),
                username
        );
        gameDAO.storeInstance(gameInstance);

        return new GameAndInstance(game.get(), gameInstance);
    }

    public GameAndInstance makeMove(String instanceId, Guess guess, String username) {
        GameAndInstance game = gameDAO.getInstanceById(instanceId);
        if (!Objects.equals(username, game.getGameInstance().getUserId())) {
            throw new WrongUserException("Tried to make a move in another users game");
        }
        // Complete games cannot be updated further
        if (!game.getGameInstance().isSuccess()) {
            boolean success = game.getGame().getAnswer().equals(guess.getGuess());
            GameInstance instance = game.getGameInstance();
            instance.setSuccess(success);
            instance.setAttempts(instance.getAttempts() + 1);
            // NOTE if we were not using in memory storage, we would want to re-persist the GameInstance here
        }

        return game;
    }

}
