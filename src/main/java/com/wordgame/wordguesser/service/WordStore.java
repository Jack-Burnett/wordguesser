package com.wordgame.wordguesser.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Loads a local list of words and lets us randomly access them
 */
public class WordStore {
    private final List<String> words;

    public WordStore() {
        ClassLoader classLoader = WordStore.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream("google-10000-english-no-swears.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        // We skip 100 to ignore things like 'the' or 'and'
        this.words = reader.lines().skip(100).toList();
    }

    public String getRandomWord() {
        int index = ThreadLocalRandom.current().nextInt(0, this.words.size());
        return this.words.get(index);
    }
}
