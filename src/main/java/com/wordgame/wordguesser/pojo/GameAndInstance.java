package com.wordgame.wordguesser.pojo;

import lombok.Value;

import java.util.List;

/**
 * Ties together a game and an instance for ease of access
 */
@Value
public class GameAndInstance {
    Game game;
    GameInstance gameInstance;

    public List<String> cluesSoFar() {
        return game.getDefinitions().subList(0, Math.min(gameInstance.getAttempts() + 1, 6));
    }

    public GameState getState() {
        return new GameState(
                gameInstance.getInstanceId(),
                cluesSoFar(),
                gameInstance.getAttempts(),
                gameInstance.isSuccess()
        );
    }

}
