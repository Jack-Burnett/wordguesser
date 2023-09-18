package com.wordgame.wordguesser;

import com.wordgame.wordguesser.pojo.Game;
import com.wordgame.wordguesser.pojo.word.Definition;
import com.wordgame.wordguesser.pojo.word.Meaning;
import com.wordgame.wordguesser.pojo.word.Word;
import com.wordgame.wordguesser.service.GameGenerator;
import com.wordgame.wordguesser.service.WordDetailRetriever;
import com.wordgame.wordguesser.service.WordStore;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameGeneratorTest {
    private final List<Word> duckResponse = List.of(
            new Word(
                    List.of(
                            new Meaning(
                                    List.of(
                                            new Definition("Goes quack"),
                                            new Definition("Waddles around"),
                                            new Definition("Likes bread")
                                    )
                            )
                    )
            ),
            new Word(
                    List.of(
                            new Meaning(
                                    List.of(
                                            new Definition("Crouch"),
                                            new Definition("Get Low"),
                                            new Definition("Stoop to avoid")
                                    )
                            )
                    )
            )
    );

    @Test
    void generateGame() {
        WordStore wordStore = mock(WordStore.class);
        WordDetailRetriever wordDetailRetriever = mock(WordDetailRetriever.class);

        GameGenerator generator = new GameGenerator(wordStore, wordDetailRetriever);

        when(wordStore.getRandomWord()).thenReturn("Duck");
        when(wordDetailRetriever.getDefinitionForWord("Duck")).thenReturn(duckResponse);
        Game game = generator.generateGame();
        assertEquals("Duck", game.getAnswer());
        assertEquals(Set.of(
                "Goes quack",
                "Waddles around",
                "Likes bread",
                "Crouch",
                "Get Low",
                "Stoop to avoid"
        ), new HashSet<>(game.getDefinitions()));
    }

    @Test
    void generateGameRetries() {
        WordStore wordStore = mock(WordStore.class);
        WordDetailRetriever wordDetailRetriever = mock(WordDetailRetriever.class);

        GameGenerator generator = new GameGenerator(wordStore, wordDetailRetriever);

        when(wordStore.getRandomWord()).thenReturn("Pelican", "Moose", "Duck");
        when(wordDetailRetriever.getDefinitionForWord("Duck")).thenReturn(duckResponse);
        Game game = generator.generateGame();
        assertEquals("Duck", game.getAnswer());
        assertEquals(Set.of(
                "Goes quack",
                "Waddles around",
                "Likes bread",
                "Crouch",
                "Get Low",
                "Stoop to avoid"
        ), new HashSet<>(game.getDefinitions()));
    }
}
