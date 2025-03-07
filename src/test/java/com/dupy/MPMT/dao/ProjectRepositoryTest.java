package com.dupy.MPMT.dao;

import com.dupy.MPMT.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setName("Test Project");
        testProject.setDescription("This is a test project");

        projectRepository.save(testProject);
    }

    @Test
    void testSaveProject() {
        Project newProject = new Project();
        newProject.setName("New Project");
        newProject.setDescription("Another project description");

        Project savedProject = projectRepository.save(newProject);

        assertNotNull(savedProject.getId());
        assertEquals("New Project", savedProject.getName());
    }

    @Test
    void testFindById_Success() {
        Optional<Project> foundProject = projectRepository.findById(testProject.getId());

        assertTrue(foundProject.isPresent());
        assertEquals("Test Project", foundProject.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Project> foundProject = projectRepository.findById(999);

        assertFalse(foundProject.isPresent());
    }

    @Test
    void testDeleteProject() {
        projectRepository.delete(testProject);
        Optional<Project> deletedProject = projectRepository.findById(testProject.getId());

        assertFalse(deletedProject.isPresent());
    }
}
