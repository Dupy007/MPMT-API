package com.dupy.MPMT.service;

import com.dupy.MPMT.dao.ProjectMemberRepository;
import com.dupy.MPMT.dao.ProjectRepository;
import com.dupy.MPMT.model.Project;
import com.dupy.MPMT.model.ProjectMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProjectMember projectMember;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1);
        project.setName("Test Project");

        projectMember = new ProjectMember();
        projectMember.setId(100);
        projectMember.setProject(project);
    }

    @Test
    void testFindAll() {
        List<Project> projects = Arrays.asList(project);
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.findAll();

        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).getName());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void testFind() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Project result = projectService.find(1);

        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void testFind_NotFound() {
        when(projectRepository.findById(2)).thenReturn(Optional.empty());

        Project result = projectService.find(2);

        assertNull(result);
        verify(projectRepository, times(1)).findById(2);
    }

    @Test
    void testSave() {
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.save(project);

        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testLink() {
        when(projectMemberRepository.save(projectMember)).thenReturn(projectMember);

        Project result = projectService.link(projectMember);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(projectMemberRepository, times(1)).save(projectMember);
    }

    @Test
    void testUnlink() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Project result = projectService.unlink(projectMember);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(projectMemberRepository, times(1)).delete(projectMember);
        verify(projectRepository, times(1)).findById(1);
    }
}
