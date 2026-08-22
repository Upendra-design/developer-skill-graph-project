package com.wexa.graphapp.service;

import com.wexa.graphapp.exception.DatabaseUnavailableException;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Loads a small, realistic demo dataset into CognoDB:
 * 5 developers, 8 skills, 6 technologies, 5 projects, and the
 * relationships between them (HAS_SKILL, WORKED_ON, USES, KNOWS).
 *
 * Everything is inserted with MERGE + parameterized maps (via UNWIND),
 * so re-running the seed is idempotent - it will not create duplicates.
 */
@Service
public class SeedDataService {

    private static final Logger log = LoggerFactory.getLogger(SeedDataService.class);

    private final Driver driver;

    public SeedDataService(Driver driver) {
        this.driver = driver;
    }

    public boolean isGraphEmpty() {
        try (Session session = driver.session()) {
            long count = session.run("MATCH (n) RETURN count(n) AS c").single().get("c").asLong();
            return count == 0;
        } catch (Neo4jException ex) {
            throw new DatabaseUnavailableException("Could not check CognoDB contents", ex);
        }
    }

    public void seed() {
        try (Session session = driver.session()) {
            createConstraints(session);

            List<Map<String, Object>> developers = List.of(
                    Map.of("id", "dev-1", "name", "Aisha Khan", "email", "aisha.khan@example.com"),
                    Map.of("id", "dev-2", "name", "Rohan Mehta", "email", "rohan.mehta@example.com"),
                    Map.of("id", "dev-3", "name", "Sara Ibrahim", "email", "sara.ibrahim@example.com"),
                    Map.of("id", "dev-4", "name", "Wei Zhang", "email", "wei.zhang@example.com"),
                    Map.of("id", "dev-5", "name", "Diego Torres", "email", "diego.torres@example.com"));

            List<Map<String, Object>> skills = List.of(
                    Map.of("id", "skill-1", "name", "Backend Development"),
                    Map.of("id", "skill-2", "name", "Frontend Development"),
                    Map.of("id", "skill-3", "name", "Database Design"),
                    Map.of("id", "skill-4", "name", "DevOps"),
                    Map.of("id", "skill-5", "name", "API Design"),
                    Map.of("id", "skill-6", "name", "UI/UX"),
                    Map.of("id", "skill-7", "name", "Testing & QA"),
                    Map.of("id", "skill-8", "name", "Data Engineering"));

            List<Map<String, Object>> technologies = List.of(
                    Map.of("id", "tech-1", "name", "Java", "category", "Language"),
                    Map.of("id", "tech-2", "name", "Spring Boot", "category", "Framework"),
                    Map.of("id", "tech-3", "name", "React", "category", "Framework"),
                    Map.of("id", "tech-4", "name", "Neo4j / CognoDB", "category", "Database"),
                    Map.of("id", "tech-5", "name", "PostgreSQL", "category", "Database"),
                    Map.of("id", "tech-6", "name", "Docker", "category", "DevOps"));

            List<Map<String, Object>> projects = List.of(
                    Map.of("id", "proj-1", "name", "Talent Graph Explorer",
                            "description", "Internal tool to visualize how developers, skills and projects connect."),
                    Map.of("id", "proj-2", "name", "Customer Portal Revamp",
                            "description", "Rebuilt the customer-facing portal with a new design system."),
                    Map.of("id", "proj-3", "name", "Realtime Analytics Pipeline",
                            "description", "Streaming pipeline that aggregates product usage events."),
                    Map.of("id", "proj-4", "name", "Mobile Onboarding Flow",
                            "description", "New onboarding experience for the mobile app."),
                    Map.of("id", "proj-5", "name", "Internal Admin Dashboard",
                            "description", "Dashboard for support staff to manage accounts and tickets."));

            // ---- nodes ----
            session.run("UNWIND $rows AS row MERGE (d:Developer {id: row.id}) " +
                    "SET d.name = row.name, d.email = row.email", Map.of("rows", developers));

            session.run("UNWIND $rows AS row MERGE (s:Skill {id: row.id}) SET s.name = row.name",
                    Map.of("rows", skills));

            session.run("UNWIND $rows AS row MERGE (t:Technology {id: row.id}) " +
                    "SET t.name = row.name, t.category = row.category", Map.of("rows", technologies));

            session.run("UNWIND $rows AS row MERGE (p:Project {id: row.id}) " +
                    "SET p.name = row.name, p.description = row.description", Map.of("rows", projects));

            // ---- relationships ----

            List<Map<String, Object>> hasSkill = List.of(
                    Map.of("dev", "dev-1", "skill", "skill-1"),
                    Map.of("dev", "dev-1", "skill", "skill-5"),
                    Map.of("dev", "dev-1", "skill", "skill-3"),
                    Map.of("dev", "dev-2", "skill", "skill-2"),
                    Map.of("dev", "dev-2", "skill", "skill-6"),
                    Map.of("dev", "dev-3", "skill", "skill-1"),
                    Map.of("dev", "dev-3", "skill", "skill-4"),
                    Map.of("dev", "dev-3", "skill", "skill-8"),
                    Map.of("dev", "dev-4", "skill", "skill-2"),
                    Map.of("dev", "dev-4", "skill", "skill-7"),
                    Map.of("dev", "dev-5", "skill", "skill-1"),
                    Map.of("dev", "dev-5", "skill", "skill-4"));

            session.run("UNWIND $rows AS row " +
                    "MATCH (d:Developer {id: row.dev}), (s:Skill {id: row.skill}) " +
                    "MERGE (d)-[:HAS_SKILL]->(s)", Map.of("rows", hasSkill));

            List<Map<String, Object>> workedOn = List.of(
                    Map.of("dev", "dev-1", "proj", "proj-1"),
                    Map.of("dev", "dev-1", "proj", "proj-3"),
                    Map.of("dev", "dev-2", "proj", "proj-2"),
                    Map.of("dev", "dev-2", "proj", "proj-4"),
                    Map.of("dev", "dev-3", "proj", "proj-1"),
                    Map.of("dev", "dev-3", "proj", "proj-5"),
                    Map.of("dev", "dev-4", "proj", "proj-2"),
                    Map.of("dev", "dev-4", "proj", "proj-4"),
                    Map.of("dev", "dev-5", "proj", "proj-3"),
                    Map.of("dev", "dev-5", "proj", "proj-5"));

            session.run("UNWIND $rows AS row " +
                    "MATCH (d:Developer {id: row.dev}), (p:Project {id: row.proj}) " +
                    "MERGE (d)-[:WORKED_ON]->(p)", Map.of("rows", workedOn));

            List<Map<String, Object>> uses = List.of(
                    Map.of("proj", "proj-1", "tech", "tech-1"),
                    Map.of("proj", "proj-1", "tech", "tech-2"),
                    Map.of("proj", "proj-1", "tech", "tech-4"),
                    Map.of("proj", "proj-1", "tech", "tech-3"),
                    Map.of("proj", "proj-2", "tech", "tech-3"),
                    Map.of("proj", "proj-2", "tech", "tech-5"),
                    Map.of("proj", "proj-3", "tech", "tech-1"),
                    Map.of("proj", "proj-3", "tech", "tech-2"),
                    Map.of("proj", "proj-3", "tech", "tech-5"),
                    Map.of("proj", "proj-4", "tech", "tech-3"),
                    Map.of("proj", "proj-4", "tech", "tech-6"),
                    Map.of("proj", "proj-5", "tech", "tech-2"),
                    Map.of("proj", "proj-5", "tech", "tech-5"),
                    Map.of("proj", "proj-5", "tech", "tech-6"));

            session.run("UNWIND $rows AS row " +
                    "MATCH (p:Project {id: row.proj}), (t:Technology {id: row.tech}) " +
                    "MERGE (p)-[:USES]->(t)", Map.of("rows", uses));

            List<Map<String, Object>> knows = List.of(
                    Map.of("dev", "dev-1", "tech", "tech-1"),
                    Map.of("dev", "dev-1", "tech", "tech-2"),
                    Map.of("dev", "dev-1", "tech", "tech-4"),
                    Map.of("dev", "dev-2", "tech", "tech-3"),
                    Map.of("dev", "dev-2", "tech", "tech-6"),
                    Map.of("dev", "dev-3", "tech", "tech-4"),
                    Map.of("dev", "dev-3", "tech", "tech-5"),
                    Map.of("dev", "dev-3", "tech", "tech-6"),
                    Map.of("dev", "dev-4", "tech", "tech-3"),
                    Map.of("dev", "dev-4", "tech", "tech-1"),
                    Map.of("dev", "dev-5", "tech", "tech-2"),
                    Map.of("dev", "dev-5", "tech", "tech-6"),
                    Map.of("dev", "dev-5", "tech", "tech-5"));

            session.run("UNWIND $rows AS row " +
                    "MATCH (d:Developer {id: row.dev}), (t:Technology {id: row.tech}) " +
                    "MERGE (d)-[:KNOWS]->(t)", Map.of("rows", knows));

            log.info("Seed data loaded: {} developers, {} skills, {} technologies, {} projects",
                    developers.size(), skills.size(), technologies.size(), projects.size());

        } catch (Neo4jException ex) {
            throw new DatabaseUnavailableException("Failed to seed CognoDB", ex);
        }
    }

    private void createConstraints(Session session) {
        session.run("CREATE CONSTRAINT developer_id IF NOT EXISTS FOR (d:Developer) REQUIRE d.id IS UNIQUE");
        session.run("CREATE CONSTRAINT skill_id IF NOT EXISTS FOR (s:Skill) REQUIRE s.id IS UNIQUE");
        session.run("CREATE CONSTRAINT project_id IF NOT EXISTS FOR (p:Project) REQUIRE p.id IS UNIQUE");
        session.run("CREATE CONSTRAINT technology_id IF NOT EXISTS FOR (t:Technology) REQUIRE t.id IS UNIQUE");
    }
}
