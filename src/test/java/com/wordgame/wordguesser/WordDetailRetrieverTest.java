package com.wordgame.wordguesser;

import com.wordgame.wordguesser.pojo.word.Word;
import com.wordgame.wordguesser.service.WordDetailRetriever;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WordDetailRetrieverTest {
    @Test
    void testWordDetails() {
        String url = "localhost:8080";
        RestTemplate restTemplate = mock(RestTemplate.class);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(url));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        List<Word> cat = List.of(new Word(Collections.emptyList()));

        when(restTemplate.getForObject("cat", Word[].class, headers)).thenReturn(cat.toArray(new Word[]{}));

        WordDetailRetriever wordDetailRetriever = new WordDetailRetriever(restTemplate);
        assertEquals(cat, wordDetailRetriever.getDefinitionForWord("cat"));

    }
}
