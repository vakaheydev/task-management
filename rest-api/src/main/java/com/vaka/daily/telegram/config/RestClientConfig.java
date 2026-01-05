package com.vaka.daily.telegram.config;

import com.vaka.daily.telegram.error_handling.TelegramResponseErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;

@Configuration
@PropertySource("classpath:application.properties")
@Slf4j
public class RestClientConfig {
    @Value("telegram.bot.url")
    private String url;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader("Content-Type", "application/json")
                .defaultStatusHandler(new TelegramResponseErrorHandler())
                .build();
    }
}
