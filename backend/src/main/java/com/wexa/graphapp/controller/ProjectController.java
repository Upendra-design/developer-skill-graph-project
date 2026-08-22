package com.wexa.graphapp.controller;

import com.wexa.graphapp.dto.ProjectDetailsDto;
import com.wexa.graphapp.model.Developer;
import com.wexa.graphapp.model.Project;
import com.wexa.graphapp.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectDetailsDto getProjectDetails(@PathVariable String id) {
        return projectService.getProjectDetails(id);
    }

    @GetMapping("/{id}/technology-network")
    public ProjectDetailsDto getTechnologyNetwork(@PathVariable String id) {
        return projectService.getProjectDetails(id);
    }

    @GetMapping("/{id}/candidate-developers")
    public List<Developer> getCandidateDevelopers(@PathVariable String id) {
        return projectService.getCandidateDevelopers(id);
    }

    @GetMapping("/{id}/qualified-developers")
    public List<Developer> getQualifiedDevelopers(@PathVariable String id) {
        return projectService.getDevelopersWhoKnowProjectTechnologies(id);
    }
}
