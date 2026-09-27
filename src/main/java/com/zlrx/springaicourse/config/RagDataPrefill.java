package com.zlrx.springaicourse.config;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RagDataPrefill {

    private final VectorStore vectorStore;
    private JdbcTemplate jdbcTemplate;

    public RagDataPrefill(VectorStore vectorStore, JdbcTemplate jdbcTemplate) {
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    void postConstruct() {
        jdbcTemplate.execute("TRUNCATE TABLE vector_store");

        List<Document> documents = List.of(
                new Document("Joe has a 100 m² house with a 15m² terrace attached to it.", Map.of("topic", "housing")),
                new Document("Elza likes fruit tea"),
                new Document("Starcraft is the most popular e-sport in Korea", Map.of("topic", "sport")),
                new Document("Warcraft was reimagined to MMO", Map.of("topic", "game")),
                new Document("Brandon Sanderson is the best contemporary fantasy writer", Map.of("topic", "sport")),
                new Document("Spring AI is the best AI framework for Java", Map.of("topic", "sport"))
        );


        vectorStore.add(documents);

    }


}
