package com.zlrx.springaicourse.model;

import java.time.LocalDateTime;

public record ChatMemoryEntry(
        String conversationId,
        String content,
        String type,
        LocalDateTime timestamp,
        long sequenceId
) {
}
