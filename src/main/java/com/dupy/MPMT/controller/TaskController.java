package com.dupy.MPMT.controller;

import com.dupy.MPMT.exception.EntityNotFoundException;
import com.dupy.MPMT.model.*;
import com.dupy.MPMT.service.EmailService;
import com.dupy.MPMT.service.TaskService;
import com.dupy.MPMT.service.UserService;
import com.dupy.MPMT.utils.Func;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    private Task task;
    private User u;

    @GetMapping("/task/all")
    public List<Task> tasks(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        Func.canAcces(userService, auth);
        return taskService.findAll();
    }

    @GetMapping("/task")
    public List<Task> myTasks(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        u = Func.canAcces(userService, auth);
        return u.getTasks();
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<?> task(@PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        u = Func.canAcces(userService, auth);
        task = taskService.find(id);
        if (task != null) {
            Func.canAccesProject(u, task.getProject().getId());
            return ResponseEntity.ok(task);
        }
        throw new EntityNotFoundException();
    }

    @PostMapping("/task")
    public ResponseEntity<?> save(@Valid @RequestBody TaskCreate data, BindingResult bindingResult, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        u = Func.canAcces(userService, auth);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage));
        }
        Project project = Func.canEditTask(u, data.getProject().getId());
        data.setProject(project);
        task = taskService.save(Task.parse(data));
        if (task != null) {
            if (task.getId() > 0) {
                TaskHistory t = new TaskHistory();
                t.setAction("Create");
                t.setTask(task);
                taskService.link(t);
            }
            String msg = "A new task (" + task.getName() + ") has been added to the " + project.getName() + " project ";
            String obj = "New Task";
            for (ProjectMember pm : project.getProjectMembers()) {
                try {
                    emailService.sendEmail(pm.getUser().getEmail(), obj, msg);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.internalServerError().body("Unsaved");
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody TaskEdit data, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        u = Func.canAcces(userService, auth);
        task = taskService.find(id);
        Func.canEditTask(u, task.getProject().getId());
        if (task != null) {
            String desc = task.myString();
            if (data.getStatus() > 0) {
                task.setStatus(data.getStatus());
            }
            if (data.getPriority() > 0) {
                task.setPriority(data.getPriority());
            }
            if (data.getName() != null) {
                task.setName(data.getName());
            }
            if (data.getDescription() != null) {
                task.setDescription(data.getDescription());
            }
            if (data.getDue_date() != null) {
                task.setDue_date(data.getDue_date());
            }
            if (data.getEnd_date() != null) {
                task.setEnd_date(data.getEnd_date());
            }
            if (data.getAssigned() != null) {
                task.setAssigned(data.getAssigned());
            }
            task = taskService.save(task);
            if (task.getId() > 0) {
                TaskHistory t = new TaskHistory();
                t.setAction("Update");
                desc += " => " + task.myString();
                t.setDescription(desc);
                t.setTask(task);
                taskService.link(t);
            }
            return ResponseEntity.ok(task);
        }
        throw new EntityNotFoundException();
    }

    @PutMapping("/task/{id}/assign")
    public ResponseEntity<?> assign(@PathVariable int id, @RequestBody TaskEdit data, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        u = Func.canAcces(userService, auth);
        task = taskService.find(id);
        Func.canEditTask(u, task.getProject().getId());
        if (task != null) {
            String desc = task.myString();
            if (data.getAssigned() != null) {
                task.setAssigned(data.getAssigned());
            }
            task = taskService.save(task);
            if (task.getId() > 0) {
                TaskHistory t = new TaskHistory();
                t.setAction("Update");
                desc += " => " + task.myString();
                t.setDescription(desc);
                t.setTask(task);
                taskService.link(t);
            }
            return ResponseEntity.ok(task);
        }
        throw new EntityNotFoundException();
    }


}
