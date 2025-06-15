package com.arka.micro_catalog.adapters.driven.security;

import com.arka.micro_catalog.domain.spi.IJwtPersistencePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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
            log.debug("Initializing JWT Adapter with base64 secret.");
            this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Key));
            log.info("JWT secret key initialized using base64 decoding.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT secret is not valid base64, using raw byte array instead.");
            this.secretKey = Keys.hmacShaKeyFor(base64Key.getBytes());
        }
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                log.debug("Validating JWT token...");
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);
                log.info("JWT token validated successfully.");
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                log.error("JWT token validation failed: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    @Override
    public Mono<String> getUserIdFromToken(String token) {
        log.debug("Extracting user ID from token...");
        return Mono.fromCallable(() -> getClaims(token).getSubject());
    }

    @Override
    public Mono<String> getEmailFromToken(String token) {
        log.debug("Extracting email from token...");
        return Mono.fromCallable(() -> getClaims(token).get(NAME_CLAIM_EMAIL, String.class));
    }

    @Override
    public Mono<String> getRoleFromToken(String token) {
        log.debug("Extracting role from token...");
        return Mono.fromCallable(() -> getClaims(token).get(NAME_CLAIM_ROLE, String.class));
    }

    @Override
    public Long getExpirationTime() {
        log.debug("Returning JWT expiration time: {}", expirationTime);
        return expirationTime;
    }

    private Claims getClaims(String token) {
        try {
            log.debug("Parsing claims from JWT token.");
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Error parsing JWT claims: {}", e.getMessage(), e);
            throw e;
        }
    }
}
