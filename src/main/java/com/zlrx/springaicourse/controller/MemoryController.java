package com.zlrx.springaicourse.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/memory")
public class MemoryController {

    private final ChatClient chatClient;
    //private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public MemoryController(
            ChatClient chatClient,
            // ChatModel chatModel,
            VectorStore vectorStore
    ) {
        this.chatClient = chatClient;
        // this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    // Memory example
    // Rag example
    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam String id,
            @RequestParam(required = true) String prompt
    ) {

        var response = chatClient.prompt()
                .advisors(adv ->
                        adv.param(ChatMemory.CONVERSATION_ID, id) //Memory advisor
                ).advisors(
                        QuestionAnswerAdvisor.builder(vectorStore) //RAG advisor
                                .searchRequest(SearchRequest.builder().similarityThreshold(0.8d).topK(3).build())
                                .build(),
                        new SimpleLoggerAdvisor()
                )
                .system("You are a helpful assistant")
                .user(prompt.replace("_", " "))
                .call()
                .content();

        return ResponseEntity.ok(response);
    }


}
