package com.vaka.daily_notifier.security;

import com.vaka.daily_notifier.service.client.ApiClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final ApiClientService apiClientService;

    public AdminAuthInterceptor(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header");
            return false;
        }

        String token = authHeader.substring(7);
        if (!apiClientService.isAdmin(token)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: admin role required");
            return false;
        }

        return true;
    }
}
