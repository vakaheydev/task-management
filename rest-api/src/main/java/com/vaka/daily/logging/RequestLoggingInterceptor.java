package com.vaka.daily.logging;

import com.vaka.daily.security.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class RequestLoggingInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String path = request.getRequestURI();
        String username = "anonymous";
        try {
            username = SecurityUtils.currentUser().getUsername();
        } catch (Exception ignored) {
            // no authenticated user found
        }

        log.info("Request {} {} by user='{}'", method, path, username);
        return true;
    }
}
