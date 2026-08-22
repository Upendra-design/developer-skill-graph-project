package com.wexa.graphapp.dto;

import com.wexa.graphapp.model.Developer;
import com.wexa.graphapp.model.Project;
import com.wexa.graphapp.model.Skill;
import com.wexa.graphapp.model.Technology;

import java.util.List;

/**
 * Aggregated view of a developer used by the "developer details" screen:
 * their own info plus everything reachable via HAS_SKILL, WORKED_ON and
 * the derived (multi-hop) connected-technologies traversal.
 */
public class DeveloperDetailsDto {
    private Developer developer;
    private List<Skill> skills;
    private List<Project> projects;
    private List<Technology> connectedTechnologies;
    private List<Technology> knownTechnologies;

    public DeveloperDetailsDto() {}

    public DeveloperDetailsDto(Developer developer, List<Skill> skills, List<Project> projects,
                                List<Technology> connectedTechnologies, List<Technology> knownTechnologies) {
        this.developer = developer;
        this.skills = skills;
        this.projects = projects;
        this.connectedTechnologies = connectedTechnologies;
        this.knownTechnologies = knownTechnologies;
    }

    public Developer getDeveloper() { return developer; }
    public void setDeveloper(Developer developer) { this.developer = developer; }

    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }

    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }

    public List<Technology> getConnectedTechnologies() { return connectedTechnologies; }
    public void setConnectedTechnologies(List<Technology> connectedTechnologies) { this.connectedTechnologies = connectedTechnologies; }

    public List<Technology> getKnownTechnologies() { return knownTechnologies; }
    public void setKnownTechnologies(List<Technology> knownTechnologies) { this.knownTechnologies = knownTechnologies; }
}
