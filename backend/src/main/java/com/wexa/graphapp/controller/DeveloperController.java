package com.wexa.graphapp.controller;

import com.wexa.graphapp.dto.DeveloperDetailsDto;
import com.wexa.graphapp.dto.RelatedDeveloperDto;
import com.wexa.graphapp.model.Developer;
import com.wexa.graphapp.model.Project;
import com.wexa.graphapp.model.Skill;
import com.wexa.graphapp.model.Technology;
import com.wexa.graphapp.service.DeveloperService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {

    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping
    public List<Developer> getAllDevelopers(@RequestParam(required = false) String search) {
        return developerService.searchDevelopers(search);
    }

    @GetMapping("/{id}")
    public DeveloperDetailsDto getDeveloperDetails(@PathVariable String id) {
        return developerService.getDeveloperDetails(id);
    }

    @GetMapping("/{id}/skills")
    public List<Skill> getSkills(@PathVariable String id) {
        return developerService.getSkillsOfDeveloper(id);
    }

    @GetMapping("/{id}/projects")
    public List<Project> getProjects(@PathVariable String id) {
        return developerService.getProjectsOfDeveloper(id);
    }

    @GetMapping("/{id}/connected-technologies")
    public List<Technology> getConnectedTechnologies(@PathVariable String id) {
        return developerService.getConnectedTechnologiesOfDeveloper(id);
    }

    @GetMapping("/{id}/related-developers")
    public List<RelatedDeveloperDto> getRelatedDevelopers(@PathVariable String id) {
        return developerService.getRelatedDevelopers(id);
    }
}
