package com.vaka.daily.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TelegramClient {
    @Value("${telegram.bot.url}")
    private String URL;
    RestClient restClient;

    public TelegramClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void sendMessage(long chatId, String message) {
        restClient.post()
                .uri(URL + "/message/" + chatId)
                .body(message)
                .retrieve()
                .toBodilessEntity();
    }
}
