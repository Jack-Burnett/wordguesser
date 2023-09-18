package com.wordgame.wordguesser.exceptions;

/**
 * Used if a user tries to access a game that does not exist
 */
public class InvalidIdException extends RuntimeException {
    public InvalidIdException(String message) {
        super(message);
    }
}
