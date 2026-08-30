package com.zlrx.springaicourse.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
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
    // private final VectorStore vectorStore;


    public MemoryController(
            ChatClient chatClient//,
            // ChatModel chatModel,
            //VectorStore vectorStore
    ) {
        this.chatClient = chatClient;
        // this.chatModel = chatModel;
        //this.vectorStore = vectorStore;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam String id,
            @RequestParam(required = true) String prompt
    ) {
        var response = chatClient.prompt()
                .advisors(adv ->
                        adv.param(ChatMemory.CONVERSATION_ID, id)
                )
                .system("You are a helpful assistant")
                .user(prompt.replace("_", " "))
                .call()
                .content();

        return ResponseEntity.ok(response);
    }


}
