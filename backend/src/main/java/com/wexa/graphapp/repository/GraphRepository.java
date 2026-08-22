package com.wexa.graphapp.repository;

import com.wexa.graphapp.dto.DashboardStatsDto;
import com.wexa.graphapp.dto.RelatedDeveloperDto;
import com.wexa.graphapp.exception.DatabaseUnavailableException;
import com.wexa.graphapp.model.Developer;
import com.wexa.graphapp.model.Project;
import com.wexa.graphapp.model.Skill;
import com.wexa.graphapp.model.Technology;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.neo4j.driver.types.Node;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All Cypher access to CognoDB lives here. Every query is parameterized
 * (see the `Map<String, Object> params` passed to session.run) - no user
 * input is ever concatenated into a query string.
 *
 * Each public method opens a Session, runs one query, maps the records to
 * plain model/DTO objects, and closes the session (try-with-resources).
 * Any driver-level failure (network, auth, instance down) is caught and
 * re-thrown as a DatabaseUnavailableException so the controller layer can
 * return a clean 503 instead of a raw stack trace.
 */
@Repository
public class GraphRepository {

    private final Driver driver;

    public GraphRepository(Driver driver) {
        this.driver = driver;
    }

    private Session session() {
        return driver.session();
    }

    private <T> T runSafely(java.util.function.Function<Session, T> work) {
        try (Session session = session()) {
            return work.apply(session);
        } catch (Neo4jException ex) {
            throw new DatabaseUnavailableException("Failed to reach CognoDB", ex);
        }
    }

    // ---------- mapping helpers ----------

    private Developer toDeveloper(Node n) {
        return new Developer(
                n.get("id").asString(),
                n.get("name").asString(),
                n.get("email").asString(""));
    }

    private Skill toSkill(Node n) {
        return new Skill(n.get("id").asString(), n.get("name").asString());
    }

    private Project toProject(Node n) {
        return new Project(
                n.get("id").asString(),
                n.get("name").asString(),
                n.get("description").asString(""));
    }

    private Technology toTechnology(Node n) {
        return new Technology(
                n.get("id").asString(),
                n.get("name").asString(),
                n.get("category").asString(""));
    }

    // ---------- basic listing / lookup queries ----------

    public List<Developer> findAllDevelopers() {
        return runSafely(session -> {
            Result result = session.run("MATCH (d:Developer) RETURN d ORDER BY d.name");
            List<Developer> developers = new ArrayList<>();
            for (Record r : result.list()) {
                developers.add(toDeveloper(r.get("d").asNode()));
            }
            return developers;
        });
    }

    public Developer findDeveloperById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return runSafely(session -> {
            Result result = session.run("MATCH (d:Developer {id: $id}) RETURN d", params);
            if (!result.hasNext()) return null;
            return toDeveloper(result.single().get("d").asNode());
        });
    }

    public List<Developer> searchDevelopers(String query) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", "(?i).*" + query + ".*");
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (d:Developer) WHERE d.name =~ $query RETURN d ORDER BY d.name", params);
            List<Developer> developers = new ArrayList<>();
            for (Record r : result.list()) {
                developers.add(toDeveloper(r.get("d").asNode()));
            }
            return developers;
        });
    }

    public List<Skill> findAllSkills() {
        return runSafely(session -> {
            Result result = session.run("MATCH (s:Skill) RETURN s ORDER BY s.name");
            List<Skill> skills = new ArrayList<>();
            for (Record r : result.list()) {
                skills.add(toSkill(r.get("s").asNode()));
            }
            return skills;
        });
    }

    public List<Project> findAllProjects() {
        return runSafely(session -> {
            Result result = session.run("MATCH (p:Project) RETURN p ORDER BY p.name");
            List<Project> projects = new ArrayList<>();
            for (Record r : result.list()) {
                projects.add(toProject(r.get("p").asNode()));
            }
            return projects;
        });
    }

    public Project findProjectById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return runSafely(session -> {
            Result result = session.run("MATCH (p:Project {id: $id}) RETURN p", params);
            if (!result.hasNext()) return null;
            return toProject(result.single().get("p").asNode());
        });
    }

    public List<Technology> findAllTechnologies() {
        return runSafely(session -> {
            Result result = session.run("MATCH (t:Technology) RETURN t ORDER BY t.name");
            List<Technology> technologies = new ArrayList<>();
            for (Record r : result.list()) {
                technologies.add(toTechnology(r.get("t").asNode()));
            }
            return technologies;
        });
    }

    // ---------- 1-hop: developer -> skills ----------

    public List<Skill> findSkillsOfDeveloper(String developerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", developerId);
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (d:Developer {id: $id})-[:HAS_SKILL]->(s:Skill) RETURN s ORDER BY s.name", params);
            List<Skill> skills = new ArrayList<>();
            for (Record r : result.list()) {
                skills.add(toSkill(r.get("s").asNode()));
            }
            return skills;
        });
    }

    // ---------- 1-hop: developer -> projects ----------

    public List<Project> findProjectsOfDeveloper(String developerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", developerId);
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (d:Developer {id: $id})-[:WORKED_ON]->(p:Project) RETURN p ORDER BY p.name", params);
            List<Project> projects = new ArrayList<>();
            for (Record r : result.list()) {
                projects.add(toProject(r.get("p").asNode()));
            }
            return projects;
        });
    }

    public List<Technology> findKnownTechnologiesOfDeveloper(String developerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", developerId);
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (d:Developer {id: $id})-[:KNOWS]->(t:Technology) RETURN t ORDER BY t.name", params);
            List<Technology> technologies = new ArrayList<>();
            for (Record r : result.list()) {
                technologies.add(toTechnology(r.get("t").asNode()));
            }
            return technologies;
        });
    }

    // ---------- MULTI-HOP (2 hops): developer -> WORKED_ON -> project -> USES -> technology ----------
    // This is the traversal a relational join could also express, but it
    // demonstrates the core graph pattern: walk from a developer, through
    // every project they worked on, out to every technology those
    // projects use, de-duplicated automatically by the DISTINCT.

    public List<Technology> findConnectedTechnologiesOfDeveloper(String developerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", developerId);
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (d:Developer {id: $id})-[:WORKED_ON]->(:Project)-[:USES]->(t:Technology) " +
                            "RETURN DISTINCT t ORDER BY t.name", params);
            List<Technology> technologies = new ArrayList<>();
            for (Record r : result.list()) {
                technologies.add(toTechnology(r.get("t").asNode()));
            }
            return technologies;
        });
    }

    // ---------- Project -> Technology network + Project -> Developers ----------

    public List<Technology> findTechnologiesOfProject(String projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", projectId);
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (p:Project {id: $id})-[:USES]->(t:Technology) RETURN t ORDER BY t.name", params);
            List<Technology> technologies = new ArrayList<>();
            for (Record r : result.list()) {
                technologies.add(toTechnology(r.get("t").asNode()));
            }
            return technologies;
        });
    }

    public List<Developer> findDevelopersOfProject(String projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", projectId);
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (d:Developer)-[:WORKED_ON]->(p:Project {id: $id}) RETURN d ORDER BY d.name", params);
            List<Developer> developers = new ArrayList<>();
            for (Record r : result.list()) {
                developers.add(toDeveloper(r.get("d").asNode()));
            }
            return developers;
        });
    }

    // ---------- 3-hop query that a relational DB finds awkward ----------
    // "Which developers who did NOT work on this project already KNOW a
    // technology this project USES?" - i.e. good candidates to staff onto
    // the project. In SQL this needs a chain of joins across at least four
    // tables plus a NOT EXISTS anti-join; in Cypher it is one readable
    // pattern plus one WHERE NOT clause.

    public List<Developer> findCandidateDevelopersForProject(String projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", projectId);
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (p:Project {id: $id})-[:USES]->(t:Technology)<-[:KNOWS]-(d:Developer) " +
                            "WHERE NOT (d)-[:WORKED_ON]->(p) " +
                            "RETURN DISTINCT d ORDER BY d.name", params);
            List<Developer> developers = new ArrayList<>();
            for (Record r : result.list()) {
                developers.add(toDeveloper(r.get("d").asNode()));
            }
            return developers;
        });
    }

    // ---------- Related developers through shared technology or project ----------
    // Another "graph-shaped" query: for a given developer, find other
    // developers connected to them either because they both worked on the
    // same project, or because they both know/use the same technology.
    // Expressing "connected via ANY of several relationship types, 1-2
    // hops away, without duplicating rows per path" is exactly what graph
    // traversal is good at and relational JOINs get verbose and slow for.

    public List<RelatedDeveloperDto> findRelatedDevelopers(String developerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", developerId);
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (d:Developer {id: $id})-[:WORKED_ON]->(p:Project)<-[:WORKED_ON]-(other:Developer) " +
                            "WHERE other.id <> $id " +
                            "RETURN DISTINCT other.id AS id, other.name AS name, p.name AS viaProject, 'Project' AS via " +
                            "UNION " +
                            "MATCH (d:Developer {id: $id})-[:KNOWS]->(t:Technology)<-[:KNOWS]-(other:Developer) " +
                            "WHERE other.id <> $id " +
                            "RETURN DISTINCT other.id AS id, other.name AS name, t.name AS viaProject, 'Technology' AS via",
                    params);
            List<RelatedDeveloperDto> related = new ArrayList<>();
            for (Record r : result.list()) {
                related.add(new RelatedDeveloperDto(
                        r.get("id").asString(),
                        r.get("name").asString(),
                        r.get("via").asString(),
                        r.get("viaProject").asString()));
            }
            return related;
        });
    }

    // ---------- developers who know technologies used in a project ----------

    public List<Developer> findDevelopersWhoKnowProjectTechnologies(String projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", projectId);
        return runSafely(session -> {
            Result result = session.run(
                    "MATCH (p:Project {id: $id})-[:USES]->(t:Technology)<-[:KNOWS]-(d:Developer) " +
                            "RETURN DISTINCT d ORDER BY d.name", params);
            List<Developer> developers = new ArrayList<>();
            for (Record r : result.list()) {
                developers.add(toDeveloper(r.get("d").asNode()));
            }
            return developers;
        });
    }

    // ---------- dashboard stats ----------

    public DashboardStatsDto getDashboardStats() {
        return runSafely(session -> {
            long developers = session.run("MATCH (d:Developer) RETURN count(d) AS c").single().get("c").asLong();
            long skills = session.run("MATCH (s:Skill) RETURN count(s) AS c").single().get("c").asLong();
            long projects = session.run("MATCH (p:Project) RETURN count(p) AS c").single().get("c").asLong();
            long technologies = session.run("MATCH (t:Technology) RETURN count(t) AS c").single().get("c").asLong();
            return new DashboardStatsDto(developers, skills, projects, technologies);
        });
    }

    // ---------- health check ----------

    public boolean isDatabaseReachable() {
        try (Session session = session()) {
            session.run("RETURN 1").consume();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
