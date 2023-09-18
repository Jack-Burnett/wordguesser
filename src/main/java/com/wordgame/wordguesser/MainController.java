package com.wordgame.wordguesser;

import com.wordgame.wordguesser.exceptions.InvalidIdException;
import com.wordgame.wordguesser.exceptions.WrongUserException;
import com.wordgame.wordguesser.pojo.GameAndInstance;
import com.wordgame.wordguesser.pojo.GameState;
import com.wordgame.wordguesser.pojo.Guess;
import com.wordgame.wordguesser.service.PlayService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Defines the two main endpoints needed to play the game
 */
@RestController
@AllArgsConstructor
public class MainController {
    private PlayService playService;

    @PostMapping("/start-game")
    public GameState startGame(@RequestHeader("username") String username) {
        // NOTE we would want to do some user verification here ideally
        GameAndInstance gameAndInstance = playService.startNewGame(username);
        return gameAndInstance.getState();
    }

    @PostMapping("/guess/{instanceId}")
    public GameState guess(@RequestHeader("username") String username, @PathVariable String instanceId, @RequestBody Guess guess) {
        GameAndInstance gameAndInstance = playService.makeMove(instanceId, guess, username);
        return gameAndInstance.getState();
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<String> invalidIdException(
            InvalidIdException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(WrongUserException.class)
    public ResponseEntity<String> wrongUserException(
            WrongUserException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }
}
