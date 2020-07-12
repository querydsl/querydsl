package com.querydsl.example.config;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
class R2DBCConfiguration extends AbstractR2dbcConfiguration {

    @Bean
    public H2ConnectionFactory connectionFactory() {
        return new H2ConnectionFactory(H2ConnectionConfiguration
                .builder()
                .url("mem:testdb;DB_CLOSE_DELAY=-1;INIT=runscript from 'src/main/sql/001_schema.sql'")
                .username("sa")
                .build()
        );
    }

}
