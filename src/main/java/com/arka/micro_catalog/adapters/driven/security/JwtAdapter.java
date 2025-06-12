package com.arka.micro_catalog.adapters.driven.security;

import com.arka.micro_catalog.domain.spi.IJwtPersistencePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

import static com.arka.micro_catalog.adapters.driven.security.util.constants.SecurityAdapterConstants.*;

@Slf4j
@Component
public class JwtAdapter implements IJwtPersistencePort {

    @Value("${jwt.secret}")
    private String base64Key;

    private SecretKey secretKey;

    @Value("${jwt.expiration-time}")
    private Long expirationTime;

    @PostConstruct
    public void init() {
        try {
            this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Key));
        } catch (IllegalArgumentException e) {
            log.warn("JWT secret is not valid base64, treating as plain text");
            this.secretKey = Keys.hmacShaKeyFor(base64Key.getBytes());
        }
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
                return false;
            }
        });
    }

    @Override
    public Mono<String> getUserIdFromToken(String token) {
        return Mono.fromCallable(() -> getClaims(token).getSubject());
    }

    @Override
    public Mono<String> getEmailFromToken(String token) {
        return Mono.fromCallable(() -> getClaims(token).get(NAME_CLAIM_EMAIL, String.class));
    }

    @Override
    public Mono<String> getRoleFromToken(String token) {
        return Mono.fromCallable(() -> getClaims(token).get(NAME_CLAIM_ROLE, String.class));
    }

    @Override
    public Long getExpirationTime() {
        return expirationTime;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}