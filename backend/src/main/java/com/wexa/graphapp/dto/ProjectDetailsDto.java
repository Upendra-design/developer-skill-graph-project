package com.wexa.graphapp.dto;

import com.wexa.graphapp.model.Developer;
import com.wexa.graphapp.model.Project;
import com.wexa.graphapp.model.Technology;

import java.util.List;

public class ProjectDetailsDto {
    private Project project;
    private List<Technology> technologies;
    private List<Developer> developers;

    public ProjectDetailsDto() {}

    public ProjectDetailsDto(Project project, List<Technology> technologies, List<Developer> developers) {
        this.project = project;
        this.technologies = technologies;
        this.developers = developers;
    }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public List<Technology> getTechnologies() { return technologies; }
    public void setTechnologies(List<Technology> technologies) { this.technologies = technologies; }

    public List<Developer> getDevelopers() { return developers; }
    public void setDevelopers(List<Developer> developers) { this.developers = developers; }
}
