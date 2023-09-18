package com.wordgame.wordguesser.pojo;

import lombok.Value;

import java.util.List;

/**
 * Represents a Game definition (as opposed to a user-specific instance).
 * This includes the answer and a list of definitions as clues.
 */
@Value
public class Game {
    String gameId;
    List<String> definitions;
    String answer;
}
