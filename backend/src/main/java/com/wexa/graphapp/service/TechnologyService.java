package com.wexa.graphapp.service;

import com.wexa.graphapp.model.Technology;
import com.wexa.graphapp.repository.GraphRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyService {

    private final GraphRepository repository;

    public TechnologyService(GraphRepository repository) {
        this.repository = repository;
    }

    public List<Technology> getAllTechnologies() {
        return repository.findAllTechnologies();
    }
}
