package com.wordgame.wordguesser;

import com.wordgame.wordguesser.dao.GameDAO;
import com.wordgame.wordguesser.service.GameGenerator;
import com.wordgame.wordguesser.service.PlayService;
import com.wordgame.wordguesser.service.WordDetailRetriever;
import com.wordgame.wordguesser.service.WordStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;

/**
 * Configures all Service Beans
 */
@Configuration
public class Initialisation {
    @Bean
    public WordStore wordStore() throws IOException {
        return new WordStore();
    }

    @Bean
    public WordDetailRetriever wordDetails(@Value("${application.dictionary_url}") String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(url));
        return new WordDetailRetriever(restTemplate);
    }

    @Bean
    public GameGenerator gameGenerator(WordStore wordStore, WordDetailRetriever wordDetailRetriever) {
        return new GameGenerator(wordStore, wordDetailRetriever);
    }

    @Bean
    public GameDAO gameDAO() {
        return new GameDAO();
    }

    @Bean
    public PlayService playService(GameDAO gameDAO, GameGenerator gameGenerator) {
        return new PlayService(gameDAO, gameGenerator);
    }
}
