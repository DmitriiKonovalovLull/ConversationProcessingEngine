package com.aisales.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Profile("prod")
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityTokenProperties tokenProperties;

    public TokenAuthenticationFilter(SecurityTokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            UsernamePasswordAuthenticationToken authentication = buildAuthentication(token);

            if (authentication != null) {
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken buildAuthentication(String token) {
        if (token.equals(tokenProperties.getAdmin())) {
            return new UsernamePasswordAuthenticationToken(
                    "admin-token",
                    null,
                    List.of(new SimpleGrantedAuthority("ADMIN"))
            );
        }

        if (token.equals(tokenProperties.getWrite())) {
            return new UsernamePasswordAuthenticationToken(
                    "write-token",
                    null,
                    List.of(new SimpleGrantedAuthority("WRITE"))
            );
        }

        if (token.equals(tokenProperties.getRead())) {
            return new UsernamePasswordAuthenticationToken(
                    "read-token",
                    null,
                    List.of(new SimpleGrantedAuthority("READ"))
            );
        }

        return null;
    }
}
