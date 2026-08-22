package com.wexa.graphapp.service;

import com.wexa.graphapp.dto.DashboardStatsDto;
import com.wexa.graphapp.repository.GraphRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final GraphRepository repository;

    public DashboardService(GraphRepository repository) {
        this.repository = repository;
    }

    public DashboardStatsDto getStats() {
        return repository.getDashboardStats();
    }

    public boolean isDatabaseHealthy() {
        return repository.isDatabaseReachable();
    }
}
