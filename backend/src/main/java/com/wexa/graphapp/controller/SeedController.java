package com.wexa.graphapp.controller;

import com.wexa.graphapp.service.SeedDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Manual seed trigger, useful for demos: POST /api/seed re-runs the
 * idempotent seed script (MERGE-based, so it is safe to call repeatedly).
 */
@RestController
@RequestMapping("/api/seed")
public class SeedController {

    private final SeedDataService seedDataService;

    public SeedController(SeedDataService seedDataService) {
        this.seedDataService = seedDataService;
    }

    @PostMapping
    public Map<String, String> seed() {
        seedDataService.seed();
        return Map.of("status", "seeded");
    }
}
