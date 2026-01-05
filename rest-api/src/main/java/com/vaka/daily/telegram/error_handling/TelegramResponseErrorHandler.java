package com.vaka.daily.telegram.error_handling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class TelegramResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("Error with TG client: {} | {}", response.getStatusCode(), response.getStatusText());
        throw new ResourceAccessException("Error with TG client " + response.getStatusCode());
    }
}
