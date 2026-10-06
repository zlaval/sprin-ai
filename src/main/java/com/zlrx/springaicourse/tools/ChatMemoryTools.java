package com.zlrx.springaicourse.tools;

import com.zlrx.springaicourse.model.ChatMemoryEntry;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@Component
public class ChatMemoryTools {

    private static final String SELECT_MESSAGES = """
            SELECT conversation_id, content, type, "timestamp", sequence_id
            FROM spring_ai_chat_memory
            """;
    private static final String ORDER_MESSAGES = " ORDER BY conversation_id, sequence_id";
    private static final RowMapper<ChatMemoryEntry> MESSAGE_MAPPER = (row, rowNumber) -> new ChatMemoryEntry(
            row.getString("conversation_id"),
            row.getString("content"),
            row.getString("type"),
            row.getTimestamp("timestamp").toLocalDateTime(),
            row.getLong("sequence_id")
    );

    private final JdbcTemplate jdbcTemplate;
    private final JsonMapper jsonMapper;

    public ChatMemoryTools(JdbcTemplate jdbcTemplate, JsonMapper jsonMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jsonMapper = jsonMapper;
    }

    @McpTool(name = "get_chat_memory", description = "Read persisted Spring AI chat-memory messages as JSON")
    public CallToolResult getChatMemory(
            @McpToolParam(description = "Conversation ID; omit to return all conversations", required = false)
            String id
    ) {
        var messages = id == null
                ? jdbcTemplate.query(SELECT_MESSAGES + ORDER_MESSAGES, MESSAGE_MAPPER)
                : jdbcTemplate.query(SELECT_MESSAGES + " WHERE conversation_id = ?" + ORDER_MESSAGES,
                        MESSAGE_MAPPER, id);
        var payload = Map.of("messages", messages);

        // MCP exposes structured JSON and a text representation for compatible clients.
        return CallToolResult.builder()
                .structuredContent(payload)
                .addTextContent(jsonMapper.writeValueAsString(payload))
                .build();
    }
}
