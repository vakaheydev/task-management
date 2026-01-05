package com.vaka.daily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingConfig {
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);      // Логирует IP-адрес клиента и сессию
        loggingFilter.setIncludeQueryString(true);     // Логирует строку запроса (query string)
        loggingFilter.setIncludePayload(true);         // Логирует тело запроса
        loggingFilter.setMaxPayloadLength(10000);      // Максимальная длина тела запроса для логирования
        loggingFilter.setIncludeHeaders(false);        // Можно включить, если хотите логировать заголовки
        return loggingFilter;
    }
}
