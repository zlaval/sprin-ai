package com.zlrx.springaicourse.controller;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/mcp")
public class McpController {

    private final McpSyncClient client;

    public McpController(List<McpSyncClient> clients) {
        this.client = clients.getFirst();
    }

    @GetMapping(value = "/chat-memory", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object chatMemory(@RequestParam(name = "id", required = false) String id) {
        initializeClient();
        Map<String, Object> arguments = id == null ? Map.of() : Map.of("id", id);
        var result = client.callTool(CallToolRequest.builder("get_chat_memory")
                .arguments(arguments)
                .build());

        if (Boolean.TRUE.equals(result.isError()) || result.structuredContent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "MCP chat-memory tool failed");
        }

        return result.structuredContent();
    }

    // Delay the handshake until the first request, after the local server has started.
    private synchronized void initializeClient() {
        if (!client.isInitialized()) {
            client.initialize();
        }
    }
}
