package com.vaka.daily.security;

import com.vaka.daily.domain.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityUtils {
    private static RoleHierarchy roleHierarchy;
    private static UserDetailsService userDetailsService;

    @Autowired
    public SecurityUtils(RoleHierarchy roleHierarchy, UserDetailsService userDetailsService) {
        SecurityUtils.roleHierarchy = roleHierarchy;
        SecurityUtils.userDetailsService = userDetailsService;
    }

    public static SecurityUser currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return securityUser;
        }
        if (principal instanceof Jwt jwt) {
            String username = jwt.getSubject();
            return (SecurityUser) userDetailsService.loadUserByUsername(username);
        }
        throw new IllegalStateException("No authenticated user found");
    }

    public static boolean currentUserHasRole(String role) {
        Collection<? extends GrantedAuthority> authorities =
                roleHierarchy.getReachableGrantedAuthorities(currentUser().getAuthorities());
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }

    public static boolean currentUserHasAnyRole(String... roles) {
        Collection<? extends GrantedAuthority> authorities =
                roleHierarchy.getReachableGrantedAuthorities(currentUser().getAuthorities());
        for (String role : roles) {
            if (authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role))) {
                return true;
            }
        }
        return false;
    }
}
