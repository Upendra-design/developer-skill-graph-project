package com.wexa.graphapp.service;

import com.wexa.graphapp.exception.ResourceNotFoundException;
import com.wexa.graphapp.model.Developer;
import com.wexa.graphapp.repository.GraphRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeveloperServiceTest {

    @Test
    void getAllDevelopers_returnsListFromRepository() {
        GraphRepository repository = mock(GraphRepository.class);
        List<Developer> developers = List.of(new Developer("dev-1", "Aisha Khan", "aisha@example.com"));
        when(repository.findAllDevelopers()).thenReturn(developers);

        DeveloperService service = new DeveloperService(repository);

        assertEquals(developers, service.getAllDevelopers());
    }

    @Test
    void getDeveloperById_throwsWhenNotFound() {
        GraphRepository repository = mock(GraphRepository.class);
        when(repository.findDeveloperById("missing")).thenReturn(null);

        DeveloperService service = new DeveloperService(repository);

        assertThrows(ResourceNotFoundException.class, () -> service.getDeveloperById("missing"));
    }

    @Test
    void searchDevelopers_blankQueryReturnsAll() {
        GraphRepository repository = mock(GraphRepository.class);
        List<Developer> developers = List.of(new Developer("dev-1", "Aisha Khan", "aisha@example.com"));
        when(repository.findAllDevelopers()).thenReturn(developers);

        DeveloperService service = new DeveloperService(repository);

        assertEquals(developers, service.searchDevelopers("  "));
    }
}
