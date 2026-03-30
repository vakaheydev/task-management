package com.vaka.daily_notifier.service.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ConversationMemoryService {

    private final Map<Integer, String> morningMessages = new ConcurrentHashMap<>();

    public void saveMorningMessage(Integer userId, String message) {
        morningMessages.put(userId, message);
        log.debug("Saved morning message for user {}", userId);
    }

    public Optional<String> getMorningMessage(Integer userId) {
        return Optional.ofNullable(morningMessages.get(userId));
    }

    public void clearMemory(Integer userId) {
        morningMessages.remove(userId);
        log.debug("Cleared memory for user {}", userId);
    }
}
