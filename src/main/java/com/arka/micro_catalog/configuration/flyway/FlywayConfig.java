package com.arka.micro_catalog.configuration.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.arka.micro_catalog.configuration.util.ConstantsConfiguration.MIGRATE;

@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfig {

    @Bean(initMethod = MIGRATE)
    public Flyway flyway(FlywayProperties flywayProperties) {
        return Flyway.configure()
                .dataSource(
                        flywayProperties.getUrl(),
                        flywayProperties.getUser(),
                        flywayProperties.getPassword()
                )
                .schemas(flywayProperties.getSchemas().toArray(new String[0]))
                .locations(flywayProperties.getLocations().toArray(String[]::new))
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate())
                .load();
    }
}