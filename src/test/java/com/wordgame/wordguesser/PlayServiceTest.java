package com.wordgame.wordguesser;

import com.wordgame.wordguesser.dao.GameDAO;
import com.wordgame.wordguesser.exceptions.WrongUserException;
import com.wordgame.wordguesser.pojo.*;
import com.wordgame.wordguesser.service.GameGenerator;
import com.wordgame.wordguesser.service.PlayService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayServiceTest {
    Game myGame = new Game(
            "my-id",
            List.of("a", "b", "c", "d", "e", "f"),
            "letters"
    );

    @Test
    void testPlayService() {
        GameDAO gameDAO = new GameDAO();
        GameGenerator gameGenerator = mock(GameGenerator.class);

        when(gameGenerator.generateGame()).thenReturn(myGame);

        PlayService playService = new PlayService(gameDAO, gameGenerator);

        GameAndInstance start = playService.startNewGame("jack");
        assertEquals(myGame, start.getGame());
        assertEquals(List.of("a"), start.cluesSoFar());
        assertEquals("my-id", start.getGameInstance().getGameId());
        assertEquals(0, start.getGameInstance().getAttempts());
        assertFalse(start.getGameInstance().isSuccess());

        GameAndInstance move1 = playService.makeMove(start.getGameInstance().getInstanceId(), new Guess("alphabet"), "jack");
        assertEquals(myGame, move1.getGame());
        assertEquals(List.of("a", "b"), move1.cluesSoFar());
        assertEquals("my-id", move1.getGameInstance().getGameId());
        assertEquals(1, move1.getGameInstance().getAttempts());
        assertFalse(move1.getGameInstance().isSuccess());

        GameAndInstance move2 = playService.makeMove(start.getGameInstance().getInstanceId(), new Guess("letters"), "jack");
        assertEquals(myGame, move2.getGame());
        // TODO it is a little odd that it reveals one more word on the turn you win
        //  This is because revealed clues is just based off attempts, and attempts to up when you win (which makes sense)
        assertEquals(List.of("a", "b", "c"), move2.cluesSoFar());
        assertEquals("my-id", move2.getGameInstance().getGameId());
        assertEquals(2, move2.getGameInstance().getAttempts());
        assertTrue(move2.getGameInstance().isSuccess());
    }

    @Test
    void testAllCluesRevealed() {
        GameDAO gameDAO = new GameDAO();
        GameGenerator gameGenerator = mock(GameGenerator.class);

        when(gameGenerator.generateGame()).thenReturn(myGame);

        PlayService playService = new PlayService(gameDAO, gameGenerator);

        GameAndInstance start = playService.startNewGame("jack");
        for (int i = 0; i < 10; i++) {
            playService.makeMove(start.getGameInstance().getInstanceId(), new Guess("alphabet"), "jack");
        }
        GameAndInstance ganda = playService.makeMove(start.getGameInstance().getInstanceId(), new Guess("alphabet"), "jack");
        GameState state = ganda.getState();
        assertEquals(11, state.getAttempts());
        assertEquals(List.of("a", "b", "c", "d", "e", "f"), state.getCluesSoFar());
    }

    @Test
    void testCannotEditOthersGames() {
        GameDAO gameDAO = new GameDAO();
        GameGenerator gameGenerator = mock(GameGenerator.class);

        when(gameGenerator.generateGame()).thenReturn(myGame);

        PlayService playService = new PlayService(gameDAO, gameGenerator);

        GameAndInstance start = playService.startNewGame("jack");
        assertThrows(
                WrongUserException.class,
                () -> playService.makeMove(
                        start.getGameInstance().getInstanceId(),
                        new Guess("alphabet"),
                        "third-party"
                )
        );
    }
}
