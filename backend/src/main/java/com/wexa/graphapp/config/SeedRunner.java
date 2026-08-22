package com.wexa.graphapp.config;

import com.wexa.graphapp.service.SeedDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * On startup, seeds CognoDB with demo data if AUTO_SEED=true (the default)
 * and the graph is currently empty. This means the application works
 * immediately after a fresh CognoDB instance is connected, with no manual
 * step required - but it will never overwrite or duplicate existing data.
 *
 * If CognoDB is unreachable at startup, this logs a warning instead of
 * crashing the application, so the app can still start and report a
 * graceful "database unavailable" error on API calls.
 */
@Component
public class SeedRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedRunner.class);

    private final SeedDataService seedDataService;

    @Value("${app.seed.auto-seed:true}")
    private boolean autoSeed;

    public SeedRunner(SeedDataService seedDataService) {
        this.seedDataService = seedDataService;
    }

    @Override
    public void run(String... args) {
        if (!autoSeed) {
            log.info("AUTO_SEED is disabled - skipping automatic seed check.");
            return;
        }
        try {
            if (seedDataService.isGraphEmpty()) {
                log.info("CognoDB graph is empty - loading demo seed data...");
                seedDataService.seed();
            } else {
                log.info("CognoDB already contains data - skipping seed.");
            }
        } catch (Exception ex) {
        	log.warn("Could not verify/seed CognoDB at startup (it may be unreachable): {}", ex.getMessage(), ex);
        }
    }
}
