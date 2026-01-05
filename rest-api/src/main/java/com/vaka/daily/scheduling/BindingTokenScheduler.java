package com.vaka.daily.scheduling;

import com.vaka.daily.service.domain.BindingTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnProperty(value = "telegram.binding.enabled", havingValue = "true")
public class BindingTokenScheduler {
    private final BindingTokenService bindingTokenService;

    public BindingTokenScheduler(BindingTokenService bindingTokenService) {
        this.bindingTokenService = bindingTokenService;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void deleteExpiredTokens() {
        log.debug("Started binding token scheduler");
        int tokensDeleted = bindingTokenService.deleteExpiredTasks();
    }
}