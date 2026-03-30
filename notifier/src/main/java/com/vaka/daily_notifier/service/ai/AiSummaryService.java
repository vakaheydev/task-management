package com.vaka.daily_notifier.service.ai;

import com.vaka.daily_notifier.domain.Task;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AiSummaryService {

    private final Map<String, ChatLanguageModel> chatModelCache = new ConcurrentHashMap<>();
    private final Map<String, ChatAssistant> assistantCache = new ConcurrentHashMap<>();
    private final Map<String, ChatMemory> memoryStore = new ConcurrentHashMap<>();

    private final String apiKey;
    private final String modelId;
    private final String baseUrl;

    public AiSummaryService(
            @Value("${app.ai.api-key}") String apiKey,
            @Value("${app.ai.model-id}") String modelId,
            @Value("${app.ai.base-url}") String baseUrl) {
        this.apiKey = apiKey;
        this.modelId = modelId;
        this.baseUrl = baseUrl;
    }

    public String generateSummary(Integer userId, String promptTemplate, Map<String, String> placeholders) {
        String prompt = applyPlaceholders(promptTemplate, placeholders);
        log.info("Sending prompt to AI for user {} (placeholders: {})", userId, placeholders.keySet());
        return getOrCreateAssistant(modelId).chat(userId, prompt);
    }

    public void clearMemory(Integer userId) {
        memoryStore.remove(userId.toString());
        log.debug("Cleared AI memory for user {}", userId);
    }

    public String formatTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "нет задач";
        }
        return tasks.stream()
                .map(t -> String.format("- %s: %s (дедлайн: %s)", t.getName(), t.getDescription(), t.getDeadline()))
                .collect(Collectors.joining("\n"));
    }

    private ChatAssistant getOrCreateAssistant(String modelId) {
        return assistantCache.computeIfAbsent(modelId, this::buildChatAssistant);
    }

    private ChatLanguageModel getOrCreateChatModel(String modelId) {
        return chatModelCache.computeIfAbsent(modelId, id ->
                OpenAiChatModel.builder()
                        .baseUrl(baseUrl)
                        .apiKey(apiKey)
                        .modelName(id)
                        .build()
        );
    }

    private ChatAssistant buildChatAssistant(String modelId) {
        return AiServices.builder(ChatAssistant.class)
                .chatLanguageModel(getOrCreateChatModel(modelId))
                .chatMemoryProvider(userId -> memoryStore.computeIfAbsent(
                        userId.toString(),
                        id -> MessageWindowChatMemory.withMaxMessages(10)
                ))
                .build();
    }

    private String applyPlaceholders(String template, Map<String, String> placeholders) {
        String result = template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
