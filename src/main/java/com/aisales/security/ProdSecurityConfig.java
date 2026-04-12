package com.aisales.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("prod")
@EnableConfigurationProperties(SecurityTokenProperties.class)
public class ProdSecurityConfig {

    @Bean
    SecurityFilterChain prodSecurityFilterChain(HttpSecurity http, TokenAuthenticationFilter tokenAuthenticationFilter)
            throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/actuator/health").permitAll()
                        .requestMatchers("/actuator/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/read", "/conversations/**")
                        .hasAnyAuthority("READ", "WRITE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/messages/**")
                        .hasAnyAuthority("WRITE", "ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpStatus.UNAUTHORIZED.value()))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpStatus.FORBIDDEN.value()))
                )
                .build();
    }
}
