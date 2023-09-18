package com.wordgame.wordguesser.exceptions;

/**
 * Used if a user tries to take a turn on someone else's game
 */
public class WrongUserException extends RuntimeException {
    public WrongUserException(String message) {
        super(message);
    }
}
