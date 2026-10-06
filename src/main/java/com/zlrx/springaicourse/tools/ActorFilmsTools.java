package com.zlrx.springaicourse.tools;

import com.zlrx.springaicourse.model.ActorFilms;
import com.zlrx.springaicourse.model.Film;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActorFilmsTools {

    private static final ActorFilms ACTOR_FILMS = new ActorFilms("Alex Example", 35, List.of(
            new Film("Moonlit Station", 2020),
            new Film("The Glass Harbor", 2022),
            new Film("Tomorrow's Lantern", 2025)
    ));

    @McpTool(name = "get_actor_films", description = "Return a static fictional actor profile and mock films",
            generateOutputSchema = true)
    public ActorFilms getActorFilms() {
        return ACTOR_FILMS;
    }
}
