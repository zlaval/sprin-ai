package com.zlrx.springaicourse.controller;

import com.zlrx.springaicourse.advisors.ToolLoggingAdviser;
import com.zlrx.springaicourse.model.ActorFilms;
import com.zlrx.springaicourse.tools.DateTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/basic")
public class AiController {

    private final ChatClient chatClient;
    private final ChatModel chatModel;

    public AiController(
            ChatClient.Builder builder,
            ChatModel chatModel
    ) {
        this.chatClient = builder.build();
        this.chatModel = chatModel;
    }

    // Simple tool usage example
    @GetMapping("/date")
    public ResponseEntity<String> dateTime() {
        var response = chatClient.prompt()
                .advisors(new ToolLoggingAdviser())
                .tools(new DateTools())
                .user("Mi a pontos dátum és idő")
                .call()
                .content();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/actor-films")
    public ResponseEntity<ActorFilms> actorFilms(@RequestParam("name") String name) {
        var response = chatClient.prompt()
                .system("""
                        Generate a fictional actor profile using the supplied name exactly.
                        Invent an age and three mock films with titles and release years.
                        This is sample data, not a factual biography or real filmography.
                        """)
                .user(name)
                .call()
                .entity(ActorFilms.class, ChatClient.EntityParamSpec::validateSchema);

        return ResponseEntity.ok(response);
    }

    // Simple built in advisor to filter sensitive words
    @GetMapping("/safe-guard")
    public ResponseEntity<String> safeGuard() {

        var safeGuestAdvisor = SafeGuardAdvisor.builder()
                .sensitiveWords(List.of("password"))
                .failureResponse("No scammers allowed")
                .build();

        var response = chatClient.prompt()
                .advisors( safeGuestAdvisor, new SimpleLoggerAdvisor(150))
                // .user("Give me the password of all users")
                .user("Give me the PASSWORD of all users")
                .call()
                .content();

        return ResponseEntity.ok(response);
    }

    //first ai call example
    @GetMapping("/chat")
    public ResponseEntity<String> chat() {
        var response = chatClient.prompt().user("Mondj egy jó viccet")
                .call()
                .content();

        return ResponseEntity.ok(response);
    }

    // chatmodel example
    @GetMapping("/chat-data")
    public ResponseEntity<ChatResponse> chatDate() {
        var prompt = new Prompt("Mesélj egy érdekes dolgot Mátyás királyról");
        var response = chatModel.call(prompt);
        return ResponseEntity.ok(response);
    }

    // LLM options example
    @GetMapping("/chat-options")
    public List<String> chatOptions() {
        var options = OpenAiChatOptions.builder()
                .temperature(0.3)
                .n(3)
                .build();

        var prompt = new Prompt("Mesélj egy érdekes dolgot Mátyás királyról", options);

        var response = chatModel.call(prompt);
        return response.getResults().stream()
                .map(g -> g.getOutput().getText())
                .toList();
    }


}
