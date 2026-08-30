package com.zlrx.springaicourse.advisors;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.model.tool.ToolCallingChatOptions;

public class ToolLoggingAdviser implements BaseAdvisor {

    private final int order;

    public ToolLoggingAdviser() {
        order = 1000;
    }

    public ToolLoggingAdviser(int order) {
        this.order = order;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest request, AdvisorChain advisorChain) {

        if (request.prompt().getOptions() instanceof ToolCallingChatOptions options && options.getToolCallbacks() != null) {
            var tools = options.getToolCallbacks().stream()
                    .map(t -> t.getToolDefinition().name())
                    .sorted()
                    .toList();


            System.out.printf("%n%s >>> LLM call - %d tools visible to the model %s%s%n", "\u001B[36m", tools.size(), tools, "\u001B[0m");
        }


        return request;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse response, AdvisorChain advisorChain) {
        return response;
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
