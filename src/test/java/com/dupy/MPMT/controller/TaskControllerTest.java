package com.dupy.MPMT.controller;

import com.dupy.MPMT.model.*;
import com.dupy.MPMT.service.EmailService;
import com.dupy.MPMT.service.TaskService;
import com.dupy.MPMT.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TaskController taskController;

    // Objets d'exemple
    private Task task;
    private User user;
    private Project project;
    private ProjectMember pm;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        // Création d'un utilisateur d'exemple
        user = new User();
        user.setId(1);
        user.setUsername("user1");
        user.setEmail("user1@example.com");

        // Création d'un projet d'exemple
        project = new Project();
        project.setId(1);
        project.setName("Project 1");

        // Ajout d'un membre pour simuler l'envoi d'email
        pm = new ProjectMember();
        pm.setUser(user);
        pm.setProject(project);
        pm.setRole("1");


        // Création d'une tâche d'exemple
        task = new Task();
        task.setId(1);
        task.setName("Task 1");
        task.setDescription("Task description");
        task.setProject(project);
        task.setAssigned(user);
        task.setPriority(1);

        project.setProjectMembers(List.of(pm));
        user.setProjectMembers(List.of(pm));
        user.setTasks(Collections.singletonList(task));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        List<Task> allTasks = Collections.singletonList(task);
        when(taskService.findAll()).thenReturn(allTasks);

        mockMvc.perform(get("/api/task/all")
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(task.getId()))
                .andExpect(jsonPath("$[0].name").value(task.getName()));
    }

    @Test
    public void testGetMyTasks() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/task")
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json")
                        .requestAttr("username", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Vérification de la présence de la tâche dans la liste de l'utilisateur
                .andExpect(jsonPath("$[0].id").value(task.getId()))
                .andExpect(jsonPath("$[0].name").value(task.getName()));
    }

    @Test
    public void testGetTaskById() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(taskService.find(task.getId())).thenReturn(task);

        mockMvc.perform(get("/api/task/{id}", task.getId())
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json")
                        .requestAttr("username", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.name").value(task.getName()));
    }

    @Test
    public void testSaveTask() throws Exception {
        // Représentation JSON de TaskCreate
        String taskCreateJson = "{\"name\":\"Task 1\",\"description\":\"Task description\",\"due_date\":\"2000-01-01\",\"project\":{\"id\":1}}";

        when(userService.findByUsername(anyString())).thenReturn(user);
        when(taskService.save(any(Task.class))).thenReturn(task);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/task")
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskCreateJson)
                        .requestAttr("username", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.name").value(task.getName()));
    }

    @Test
    public void testEditTask() throws Exception {
        String taskEditJson = "{\"name\":\"Task 1 Edited\",\"description\":\"Edited description\",\"status\":2,\"priority\":1}";
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(taskService.find(task.getId())).thenReturn(task);

        // Création d'une tâche mise à jour pour simuler le retour du save
        Task updatedTask = new Task();
        updatedTask.setId(task.getId());
        updatedTask.setName("Task 1 Edited");
        updatedTask.setDescription("Edited description");
        updatedTask.setProject(project);
        when(taskService.save(any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/task/{id}", task.getId())
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskEditJson)
                        .requestAttr("username", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.name").value("Task 1 Edited"))
                .andExpect(jsonPath("$.description").value("Edited description"));
    }

    @Test
    public void testAssignTask() throws Exception {
        String taskAssignJson = "{\"assigned\":{\"id\":\"1\"}}";
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(taskService.find(anyInt())).thenReturn(task);
        when(taskService.link(any(TaskHistory.class))).thenReturn(task);
        when(taskService.save(any(Task.class))).thenReturn(task);


        mockMvc.perform(put("/api/task/{id}/assign", task.getId())
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskAssignJson)
                        .requestAttr("username", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    System.out.println(contentAsString);
                })
                .andExpect(jsonPath("$.assigned.id").value("1"));
    }
}
