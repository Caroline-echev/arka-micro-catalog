package com.arka.micro_catalog.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/webjars/**",
                                "/favicon.ico"
                        ).permitAll()

                        .pathMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        .pathMatchers(HttpMethod.POST, "/api/categories/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.PUT, "/api/categories/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.DELETE, "/api/categories/**").hasAnyRole("ADMIN", "LOGISTIC")

                        .pathMatchers(HttpMethod.POST, "/api/brands/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.PUT, "/api/brands/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.DELETE, "/api/brands/**").hasAnyRole("ADMIN", "LOGISTIC")

                        .pathMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.PUT, "/api/products/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole("ADMIN", "LOGISTIC")

                        .anyExchange().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
