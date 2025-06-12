package com.arka.micro_catalog.configuration.security;

import com.arka.micro_catalog.configuration.security.JwtAuthenticationFilter;
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
                .authorizeExchange(exchanges -> exchanges
                        // Swagger y documentación - COMPLETAMENTE PÚBLICAS
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/swagger-ui/index.html"
                        ).permitAll()

                        // Rutas públicas GET
                        .pathMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // Rutas protegidas POST/PUT/DELETE
                        .pathMatchers(HttpMethod.POST, "/api/categories/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.PUT, "/api/categories/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.DELETE, "/api/categories/**").hasAnyRole("ADMIN", "LOGISTIC")

                        .pathMatchers(HttpMethod.POST, "/api/brands/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.PUT, "/api/brands/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.DELETE, "/api/brands/**").hasAnyRole("ADMIN", "LOGISTIC")

                        .pathMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.PUT, "/api/products/**").hasAnyRole("ADMIN", "LOGISTIC")
                        .pathMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole("ADMIN", "LOGISTIC")

                        // Solo las rutas API requieren autenticación
                        .pathMatchers("/api/**").authenticated()

                        // Todo lo demás es público
                        .anyExchange().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())
                // NO agregamos el filtro JWT para las rutas públicas
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}