package com.wordgame.wordguesser.pojo;

import lombok.Value;

import java.util.List;

/**
 * Models the return values a user should see when they make a guess
 */
@Value
public class GameState {
    String instanceId;
    List<String> cluesSoFar;
    int attempts;
    boolean success;
}
