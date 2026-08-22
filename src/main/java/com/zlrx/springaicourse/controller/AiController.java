package com.zlrx.springaicourse.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
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

    @GetMapping("/chat")
    public ResponseEntity<String> chat() {
        var response = chatClient.prompt().user("Mondj egy jó viccet")
                .call()
                .content();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat-data")
    public ResponseEntity<ChatResponse> chatDate() {
        var prompt = new Prompt("Mesélj egy érdekes dolgot Mátyás királyról");
        var response = chatModel.call(prompt);
        return ResponseEntity.ok(response);
    }

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
