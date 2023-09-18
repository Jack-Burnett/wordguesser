package com.wordgame.wordguesser.pojo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * A game instance is an instance of a game tied to a particular user, tracking their attempts and if they have
 * succeeded
 */
@Data
@RequiredArgsConstructor
public class GameInstance {
    final String instanceId;
    final String gameId;
    final String userId;

    int attempts = 0;
    boolean success = false;
}
