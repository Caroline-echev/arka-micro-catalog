package com.arka.micro_catalog.domain.spi;

import reactor.core.publisher.Mono;

public  interface IJwtPersistencePort {

    Mono<Boolean> validateToken(String token);
    Long getExpirationTime();


    Mono<String> getUserIdFromToken(String token);

    Mono<String> getEmailFromToken(String token);

    Mono<String> getRoleFromToken(String token);
}
