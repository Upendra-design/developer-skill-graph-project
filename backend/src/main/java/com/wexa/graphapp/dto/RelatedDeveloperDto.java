package com.wexa.graphapp.dto;

/**
 * Represents another developer connected to a given developer through a
 * shared technology, plus the technology that connects them and the
 * project where that technology was used. Backs the "graph traversal a
 * relational DB finds awkward" query - a variable-depth "who is connected
 * to whom through what" lookup.
 */
public class RelatedDeveloperDto {
    private String developerId;
    private String developerName;
    private String sharedTechnology;
    private String viaProject;

    public RelatedDeveloperDto() {}

    public RelatedDeveloperDto(String developerId, String developerName, String sharedTechnology, String viaProject) {
        this.developerId = developerId;
        this.developerName = developerName;
        this.sharedTechnology = sharedTechnology;
        this.viaProject = viaProject;
    }

    public String getDeveloperId() { return developerId; }
    public void setDeveloperId(String developerId) { this.developerId = developerId; }

    public String getDeveloperName() { return developerName; }
    public void setDeveloperName(String developerName) { this.developerName = developerName; }

    public String getSharedTechnology() { return sharedTechnology; }
    public void setSharedTechnology(String sharedTechnology) { this.sharedTechnology = sharedTechnology; }

    public String getViaProject() { return viaProject; }
    public void setViaProject(String viaProject) { this.viaProject = viaProject; }
}
