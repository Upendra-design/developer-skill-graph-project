package com.wexa.graphapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Developer Skill & Project Network application.
 *
 * This app is backed by CognoDB (a managed graph database that speaks
 * openCypher over the Bolt protocol) and uses the official Neo4j Java
 * Driver directly relational database is used
 * for the graph functionality.
 */
@SpringBootApplication
public class GraphAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(GraphAppApplication.class, args);
    }
}
