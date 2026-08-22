package com.wexa.graphapp.service;

import com.wexa.graphapp.model.Skill;
import com.wexa.graphapp.repository.GraphRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final GraphRepository repository;

    public SkillService(GraphRepository repository) {
        this.repository = repository;
    }

    public List<Skill> getAllSkills() {
        return repository.findAllSkills();
    }
}
