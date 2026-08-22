package com.wexa.graphapp.dto;

public class DashboardStatsDto {
    private long developerCount;
    private long skillCount;
    private long projectCount;
    private long technologyCount;

    public DashboardStatsDto() {}

    public DashboardStatsDto(long developerCount, long skillCount, long projectCount, long technologyCount) {
        this.developerCount = developerCount;
        this.skillCount = skillCount;
        this.projectCount = projectCount;
        this.technologyCount = technologyCount;
    }

    public long getDeveloperCount() { return developerCount; }
    public void setDeveloperCount(long developerCount) { this.developerCount = developerCount; }

    public long getSkillCount() { return skillCount; }
    public void setSkillCount(long skillCount) { this.skillCount = skillCount; }

    public long getProjectCount() { return projectCount; }
    public void setProjectCount(long projectCount) { this.projectCount = projectCount; }

    public long getTechnologyCount() { return technologyCount; }
    public void setTechnologyCount(long technologyCount) { this.technologyCount = technologyCount; }
}
