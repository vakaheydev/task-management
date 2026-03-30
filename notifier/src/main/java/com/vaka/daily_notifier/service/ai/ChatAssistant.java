package com.vaka.daily_notifier.service.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface ChatAssistant {
    String chat(@MemoryId Integer userId, @UserMessage String message);
}
