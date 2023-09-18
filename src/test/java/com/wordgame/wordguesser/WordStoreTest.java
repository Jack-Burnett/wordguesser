package com.wordgame.wordguesser;

import com.wordgame.wordguesser.service.WordStore;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WordStoreTest {
    @Test
    public void test() throws IOException {
        try (MockedStatic<ThreadLocalRandom> randomStatic = Mockito.mockStatic(ThreadLocalRandom.class)) {
            ThreadLocalRandom random = mock(ThreadLocalRandom.class);
            randomStatic.when(ThreadLocalRandom::current).thenReturn(random);

            when(random.nextInt()).thenReturn(5);

            WordStore wordStore = new WordStore();
            assertEquals("price", wordStore.getRandomWord());
        }
    }
}
