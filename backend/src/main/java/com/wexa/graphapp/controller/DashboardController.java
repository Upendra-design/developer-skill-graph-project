package com.wexa.graphapp.controller;

import com.wexa.graphapp.dto.DashboardStatsDto;
import com.wexa.graphapp.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public DashboardStatsDto getDashboard() {
        return dashboardService.getStats();
    }

    @GetMapping("/health")
    public Map<String, Object> getHealth() {
        boolean healthy = dashboardService.isDatabaseHealthy();
        return Map.of(
                "status", healthy ? "UP" : "DOWN",
                "database", healthy ? "reachable" : "unreachable"
        );
    }
}
