package com.wexa.graphapp.config;

import jakarta.annotation.PreDestroy;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Builds a single, shared Neo4j Driver instance pointed at CognoDB.
 *
 * Connection details (URI, username, password) are read ONLY from
 * environment variables - never hard-coded and never committed to git.
 * The driver internally manages a connection pool, so we only need
 * one Driver bean for the whole application's lifetime.
 */
@Configuration
public class Neo4jDriverConfig {

    private static final Logger log = LoggerFactory.getLogger(Neo4jDriverConfig.class);

    @Value("${cognodb.uri}")
    private String uri;

    @Value("${cognodb.username}")
    private String username;

    @Value("${cognodb.password}")
    private String password;

    private Driver driver;

    @Bean
    public Driver neo4jDriver() {
        log.info("Connecting to CognoDB at {}", uri);
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        return this.driver;
    }

    @PreDestroy
    public void close() {
        if (driver != null) {
            driver.close();
        }
    }
}
