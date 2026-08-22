package com.wexa.graphapp.service;

import com.wexa.graphapp.dto.ProjectDetailsDto;
import com.wexa.graphapp.exception.ResourceNotFoundException;
import com.wexa.graphapp.model.Developer;
import com.wexa.graphapp.model.Project;
import com.wexa.graphapp.repository.GraphRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final GraphRepository repository;

    public ProjectService(GraphRepository repository) {
        this.repository = repository;
    }

    public List<Project> getAllProjects() {
        return repository.findAllProjects();
    }

    public Project getProjectById(String id) {
        Project project = repository.findProjectById(id);
        if (project == null) {
            throw new ResourceNotFoundException("Project not found: " + id);
        }
        return project;
    }

    public ProjectDetailsDto getProjectDetails(String id) {
        Project project = getProjectById(id);
        return new ProjectDetailsDto(
                project,
                repository.findTechnologiesOfProject(id),
                repository.findDevelopersOfProject(id));
    }

    public List<Developer> getCandidateDevelopers(String id) {
        getProjectById(id);
        return repository.findCandidateDevelopersForProject(id);
    }

    public List<Developer> getDevelopersWhoKnowProjectTechnologies(String id) {
        getProjectById(id);
        return repository.findDevelopersWhoKnowProjectTechnologies(id);
    }
}
