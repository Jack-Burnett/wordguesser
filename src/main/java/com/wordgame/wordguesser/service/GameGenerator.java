package com.wordgame.wordguesser.service;

import com.wordgame.wordguesser.pojo.Game;
import com.wordgame.wordguesser.pojo.word.Definition;
import com.wordgame.wordguesser.pojo.word.Word;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Allows creation of new games.
 * <p>
 * Game creation is uncertain, so we have to retry until it works (could pick words that are not in the dictionary
 * or that do not have enough definitions).
 */
@AllArgsConstructor
public class GameGenerator {
    private WordStore wordStore;
    private WordDetailRetriever wordDetailRetriever;

    public Game generateGame() {
        while (true) {
            Game game = attemptToGenerateGame();
            if (game != null) {
                return game;
            }
        }
    }

    private Game attemptToGenerateGame() {
        String word = wordStore.getRandomWord();
        List<Word> words;
        try {
            words = wordDetailRetriever.getDefinitionForWord(word);
        } catch (RestClientException ex) {
            // TODO We could improve this - 500s should probably have exponential backoff
            //  or circuit-breaking (or both)
            return null;
        }
        List<String> definitions = words.stream()
                .flatMap(w -> w.getMeanings().stream())
                .flatMap(m -> m.getDefinitions().stream())
                .map(Definition::getDefinition).collect(Collectors.toList());
        // Ignore words with less than 6 definitions, as we can't give enough clues to make a full game
        if (definitions.size() < 6) {
            return null;
        }
        Collections.shuffle(definitions);
        definitions = definitions.subList(0, 6);
        return new Game(UUID.randomUUID().toString(), definitions, word);
    }
}
