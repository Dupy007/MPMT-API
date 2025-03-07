package com.dupy.MPMT.controller;

import com.dupy.MPMT.model.Project;
import com.dupy.MPMT.model.ProjectMember;
import com.dupy.MPMT.model.ProjectMemberCreate;
import com.dupy.MPMT.model.User;
import com.dupy.MPMT.service.ProjectService;
import com.dupy.MPMT.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {


    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;


    private Project project1;
    private Project project2;
    private User user;
    private User user2;
    private ProjectMember projectMember1;
    private ProjectMember projectMember2;

    @InjectMocks
    private ProjectController controller;

    private MockMvc mockMvc;
    private MockHttpServletRequest request;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer mockToken"); // Simuler un token

        // Mock data for projects
        project1 = new Project();
        project2 = new Project();
        project1.setId(1);
        project2.setId(2);
        project1.setName("Project 1");
        project2.setName("Project 2");
        project1.setDescription("Project 1");
        project2.setDescription("Project 2");
        project1.setStart_date(new Date());
        project2.setStart_date(new Date());
        project1.setEnd_date(new Date());
        project2.setEnd_date(new Date());
        user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setUsername("user");
        user.setId(1);
//        User2
        user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password");
        user2.setUsername("user2");
        user2.setId(2);
        projectMember1 = new ProjectMember();
        projectMember1.setId(1);
        projectMember1.setProject(project1);
        projectMember1.setRole("1");
        projectMember1.setUser(user);
        projectMember2 = new ProjectMember();
        projectMember2.setId(2);
        projectMember2.setProject(project2);
        projectMember2.setRole("1");
        projectMember2.setUser(user2);
    }

    @Test
    public void testGetAllProjects() throws Exception {
        List<Project> list = Arrays.asList(project1, project2);
        when(projectService.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/project/all")
                        .header("Authorization", "Bearer mockToken")
                        .requestAttr("username", "user")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(list.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Project 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Project 2"));
    }

    @Test
    public void testGetMyProjects() throws Exception {
        user.setProjectMembers(Collections.singletonList(projectMember1));
        when(userService.findByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/project")
                        .header("Authorization", "Bearer mockToken")
                        .requestAttr("username", "user")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void testGetProjectById() throws Exception {
        user.setProjectMembers(Collections.singletonList(projectMember1));
        when(userService.findByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/project/1")
                        .header("Authorization", "Bearer mockToken")
                        .requestAttr("username", "user")
                        .accept("application/json")
                        .requestAttr("username", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Project 1"));
    }

    @Test
    public void testCreateProject() throws Exception {
        Project newProject = new Project();
        newProject.setId(1);
        newProject.setName("New Project");
        newProject.setDescription("New Description");
        newProject.setStart_date(new Date());

        when(userService.findByUsername(anyString())).thenReturn(user);
        when(projectService.save(any(Project.class))).thenReturn(newProject);
        newProject.setProjectMembers(Collections.singletonList(projectMember1));
        when(projectService.find(anyInt())).thenReturn(newProject);

        mockMvc.perform(post("/api/project")
                        .header("Authorization", "Bearer mockToken")
                        .requestAttr("username", "user")
                        .accept("application/json")
                        .requestAttr("username", "user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Project\",\"description\":\"New Description\",\"start_date\":\"2000-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("New Project"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    public void testLinkMemberToProject() throws Exception {
        user.setProjectMembers(Collections.singletonList(projectMember1));
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(userService.find(anyInt())).thenReturn(user);
        when(projectService.find(anyInt())).thenReturn(project1);
        mockMvc.perform(post("/api/project/1/link")
                        .header("Authorization", "Bearer mockToken")
                        .requestAttr("username", "user")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user\":{\"id\":1},\"project\":{\"id\":1},\"role\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Project 1"));
    }

    @Test
    public void testEditProject() throws Exception {
        Project updatedProject = new Project();
        updatedProject.setId(1);
        updatedProject.setName("Updated Project");
        updatedProject.setDescription("Updated Description");
        updatedProject.setStart_date(new Date());
        updatedProject.setEnd_date(new Date());
        user.setProjectMembers(Collections.singletonList(projectMember1));
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(projectService.save(any(Project.class))).thenReturn(updatedProject);

        mockMvc.perform(put("/api/project/1")
                        .header("Authorization", "Bearer mockToken")
                        .requestAttr("username", "user")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Project\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Project"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

}
