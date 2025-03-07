package com.dupy.MPMT.config;


import com.dupy.MPMT.dao.ProjectMemberRepository;
import com.dupy.MPMT.dao.ProjectRepository;
import com.dupy.MPMT.dao.TaskRepository;
import com.dupy.MPMT.dao.UserRepository;
import com.dupy.MPMT.model.Project;
import com.dupy.MPMT.model.ProjectMember;
import com.dupy.MPMT.model.Task;
import com.dupy.MPMT.model.User;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Configuration
@AllArgsConstructor
public class DataGeneratorConfig {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private ProjectMemberRepository projectMemberRepository;

    @Bean
    CommandLineRunner generateFakeData() {
        return args -> {
            List<User> users = userRepository.findAll();
            if (!(users.isEmpty())) {
                return;
            }
//            User
            User user = new User();
            user.setEmail("user1@mpmt.com");
            user.setPassword(passwordEncoder.encode("Password1234"));
            user.setUsername("User1");
            user.setCreated_at(LocalDateTime.now());
            user.setUpdated_at(LocalDateTime.now());
            user = userRepository.save(user);

//            Project
            Project project = new Project();
            project.setName("Project 1");
            project.setDescription("Description 1");
            project.setEnd_date(new Date());
            project.setStart_date(new Date());
            project.setCreated_at(LocalDateTime.now());
            project.setUpdated_at(LocalDateTime.now());
            project = projectRepository.save(project);

//            ProjectMember
            ProjectMember projectMember = new ProjectMember();
            projectMember.setProject(project);
            projectMember.setUser(user);
            projectMember.setRole("1");
            projectMember.setCreated_at(LocalDateTime.now());
            projectMember.setUpdated_at(LocalDateTime.now());
            projectMember = projectMemberRepository.save(projectMember);

//            Task
            Task task = new Task();
            task.setName("Task 1");
            task.setDescription("Description 1");
            task.setAssigned(user);
            task.setProject(project);
            task.setStatus(1);
            task.setPriority(1);
            task.setDue_date(new Date());
            task.setEnd_date(new Date());
            task.setCreated_at(LocalDateTime.now());
            task.setUpdated_at(LocalDateTime.now());
            taskRepository.save(task);
        };
    }
}