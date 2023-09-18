package com.wordgame.wordguesser.service;

import com.wordgame.wordguesser.pojo.word.Word;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Retrieves definitions of words from a remote source
 */
public class WordDetailRetriever {
    private final RestTemplate restTemplate;

    public WordDetailRetriever(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Word> getDefinitionForWord(String word) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Word[] words = restTemplate.getForObject(word, Word[].class, headers);

        return Arrays.stream(words).toList();
    }
}
