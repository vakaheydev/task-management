package com.vaka.daily.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OAuthProtectedResourceController {

    @Value("${oauth2.issuer:https://vaka-daily.ru}")
    private String issuer;

    @GetMapping(value = "/.well-known/oauth-protected-resource",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> protectedResourceMetadata() {
        return Map.of(
                "resource", issuer,
                "authorization_servers", new String[]{issuer},
                "bearer_methods_supported", new String[]{"header"},
                "scopes_supported", new String[]{"openid", "profile", "claudeai"}
        );
    }
}
