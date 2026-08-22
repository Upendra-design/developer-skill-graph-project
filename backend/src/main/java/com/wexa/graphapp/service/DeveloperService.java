package com.wexa.graphapp.service;

import com.wexa.graphapp.dto.DeveloperDetailsDto;
import com.wexa.graphapp.dto.RelatedDeveloperDto;
import com.wexa.graphapp.exception.ResourceNotFoundException;
import com.wexa.graphapp.model.Developer;
import com.wexa.graphapp.model.Project;
import com.wexa.graphapp.model.Skill;
import com.wexa.graphapp.model.Technology;
import com.wexa.graphapp.repository.GraphRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperService {

    private final GraphRepository repository;

    public DeveloperService(GraphRepository repository) {
        this.repository = repository;
    }

    public List<Developer> getAllDevelopers() {
        return repository.findAllDevelopers();
    }

    public List<Developer> searchDevelopers(String query) {
        if (query == null || query.isBlank()) {
            return repository.findAllDevelopers();
        }
        return repository.searchDevelopers(query.trim());
    }

    public Developer getDeveloperById(String id) {
        Developer developer = repository.findDeveloperById(id);
        if (developer == null) {
            throw new ResourceNotFoundException("Developer not found: " + id);
        }
        return developer;
    }

    public DeveloperDetailsDto getDeveloperDetails(String id) {
        Developer developer = getDeveloperById(id);
        return new DeveloperDetailsDto(
                developer,
                repository.findSkillsOfDeveloper(id),
                repository.findProjectsOfDeveloper(id),
                repository.findConnectedTechnologiesOfDeveloper(id),
                repository.findKnownTechnologiesOfDeveloper(id));
    }

    public List<RelatedDeveloperDto> getRelatedDevelopers(String id) {
        getDeveloperById(id);
        return repository.findRelatedDevelopers(id);
    }

    public List<Skill> getSkillsOfDeveloper(String id) {
        getDeveloperById(id);
        return repository.findSkillsOfDeveloper(id);
    }

    public List<Project> getProjectsOfDeveloper(String id) {
        getDeveloperById(id);
        return repository.findProjectsOfDeveloper(id);
    }

    public List<Technology> getConnectedTechnologiesOfDeveloper(String id) {
        getDeveloperById(id);
        return repository.findConnectedTechnologiesOfDeveloper(id);
    }
}
