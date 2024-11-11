package com.dupy.MPMT.controller;

import com.dupy.MPMT.exception.EntityUnAuthorizedException;
import com.dupy.MPMT.model.*;
import com.dupy.MPMT.service.ProjectService;
import com.dupy.MPMT.service.TaskService;
import com.dupy.MPMT.service.UserService;
import com.dupy.MPMT.utils.Encoder;
import com.dupy.MPMT.utils.Func;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController
public class Index {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;
    private User user;

    @GetMapping("/ajout")
    public void projects() {
        ajout();
    }

    private void ajout() {
        for (int i = 1; i < 11; i++) {
            User user = new User();
            user.setEmail("email" + i + "@test.test");
            user.setUsername("User " + i);
            user.setPassword(Encoder.encode("Password"));
            userService.save(user);
        }
        for (int i = 1; i < 5; i++) {
            Project p = new Project();
            p.setName("Project " + i);
            p.setDescription("Description Project " + i);
            p.setStart_date(new Date());
            p = projectService.save(p);
            if (p.getId() > 0) {
                ProjectMember member = new ProjectMember();
                member.setUser(userService.findAll().getFirst());
                member.setProject(p);
                member.setRole("1");
                projectService.link(member);
            }
        }
        for (int i = 1; i < 20; i++) {
            Task task = new Task();
            task.setName("Task " + i);
            task.setDescription("Desc Task " + i);
            task.setProject(projectService.findAll().getFirst());
            task.setPriority(1);
            task.setStatus(1);
            task.setDue_date(new Date(2024, 10, i));
            task.setEnd_date(new Date(2025, 1, i));
            task = taskService.save(task);
            if (task.getId() > 0) {
                TaskHistory t = new TaskHistory();
                t.setDescription("Desc TaskHistory " + i);
                t.setAction("create");
                t.setTask(task);
                taskService.link(t);
            }

        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Register data, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage));
        }

        String encode = Encoder.encode(data.getPassword());
        data.setPassword(encode);
        user = userService.save(User.parse(data));
        if (user != null) {
            user.setToken(Func.generateToken());
            userService.save(user);
            return ResponseEntity.ok(user);
        }
        throw new EntityUnAuthorizedException();
    }

    @PostMapping("/login")
    public Object login(@RequestBody @Valid Login data, BindingResult bindingResult) {
        String value = data.getEmail() == null ? data.getUsername() : data.getEmail();
        if (value == null && bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage));
        }
        user = userService.findByMailOrUsername(value);
        if (user == null) {
            throw new EntityUnAuthorizedException();
        }
        String encode = Encoder.encode(data.getPassword());
        boolean passwordMatch = user.getPassword().equals(encode);
        if (!passwordMatch) {
            throw new EntityUnAuthorizedException();
        }
        if (user.getToken()==null) {
            user.setToken(Func.generateToken());
            userService.save(user);
        }
        return user;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        user = Func.canAcces(userService, auth);
        user.setToken(null);
        userService.save(user);
        return ResponseEntity.ok("");
    }
    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        user = Func.canAcces(userService, auth);
        return ResponseEntity.ok(user);
    }
}
