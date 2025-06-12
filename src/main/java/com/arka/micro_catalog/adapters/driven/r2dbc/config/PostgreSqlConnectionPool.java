package com.arka.micro_catalog.adapters.driven.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.arka.micro_catalog.adapters.driven.r2dbc.util.ConstantsR2DBC.*;

@Configuration
@EnableConfigurationProperties(PostgresqlConnectionProperties.class)
public class PostgreSqlConnectionPool {
    @Bean
    public ConnectionPool getConnectionConfig(PostgresqlConnectionProperties properties) {

        PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration.builder()
                .host(properties.host())
                .port(properties.port())
                .database(properties.database())
                .schema(properties.schema())
                .username(properties.username())
                .password(properties.password())
                .build();

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder()
                .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
                .name(CONNECTION_POOL_NAME)
                .initialSize(INITIAL_SIZE)
                .maxSize(MAX_SIZE)
                .maxIdleTime(java.time.Duration.ofMinutes(MAX_IDLE_TIME))
                .validationQuery(VALIDATION_QUERY)
                .build();

        return new ConnectionPool(poolConfiguration);
    }
}