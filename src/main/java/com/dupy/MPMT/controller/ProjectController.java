package com.dupy.MPMT.controller;

import com.dupy.MPMT.exception.EntityNotFoundException;
import com.dupy.MPMT.model.Project;
import com.dupy.MPMT.model.ProjectMember;
import com.dupy.MPMT.model.ProjectMemberCreate;
import com.dupy.MPMT.model.User;
import com.dupy.MPMT.service.EmailService;
import com.dupy.MPMT.service.ProjectService;
import com.dupy.MPMT.service.UserService;
import com.dupy.MPMT.utils.Func;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    private Project project;
    private User u;
    @Autowired
    private EmailService emailService;

    @GetMapping("/project/all")
    public List<Project> projects(HttpServletRequest request) {
        return projectService.findAll();
    }

    @GetMapping("/project")
    public List<Project> myProjects(HttpServletRequest request) {
        u = userService.findByUsername((String) request.getAttribute("username"));
        return u.getProjects();
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<?> project(@PathVariable int id, HttpServletRequest request) {
        u = userService.findByUsername((String) request.getAttribute("username"));
        project = Func.canAccesProject(u, id);

        if (project != null) {
            return ResponseEntity.ok(project);
        }
        throw new EntityNotFoundException();
    }

    @PostMapping("/project")
    public ResponseEntity<?> save(@Valid @RequestBody Project data, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage));
        }
        u = userService.findByUsername((String) request.getAttribute("username"));
        project = projectService.save(data);
        if (project != null) {
            ProjectMember member = new ProjectMember();
            member.setProject(project);
            member.setUser(u);
            member.setRole("1");
            createMember(member);
            return ResponseEntity.ok(projectService.find(project.getId()));
        }
        return ResponseEntity.internalServerError().body("Unsaved");
    }

    @PutMapping("/project/{id}")
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody Project data, HttpServletRequest request) {
        u = userService.findByUsername((String) request.getAttribute("username"));
        project = Func.canEditProject(u, id);
        if (project != null) {
            if (data.getName() != null) {
                project.setName(data.getName());
            }
            if (data.getDescription() != null) {
                project.setDescription(data.getDescription());
            }
            if (data.getStart_date() != null) {
                project.setStart_date(data.getStart_date());
            }
            if (data.getEnd_date() != null) {
                project.setEnd_date(data.getEnd_date());
            }
            projectService.save(project);
            return ResponseEntity.ok(project);
        }
        throw new EntityNotFoundException();
    }

    @PostMapping("/project/{id}/link")
    public ResponseEntity<?> link(@PathVariable int id, @RequestBody @Valid ProjectMemberCreate data, BindingResult bindingResult, HttpServletRequest request) {
        u = userService.findByUsername((String) request.getAttribute("username"));
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage));
        }
        project = Func.canAccesProject(u, id);
        if (project != null) {
            User user = null;
            if (data.getUser().getId() > 0) {
                user = userService.find(data.getUser().getId());
            } else if (data.getUser().getEmail() != null) {
                user = userService.findByEmail(data.getUser().getEmail());
            }
            if (user == null) {
                List<String> errors = new ArrayList<>();
                errors.add("User not found");
                return ResponseEntity.badRequest().body(errors);
            }
            ProjectMember member = new ProjectMember();
            member.setProject(project);
            member.setUser(user);
            member.setRole(data.getRole());
            createMember(member);
            return ResponseEntity.ok(projectService.find(project.getId()));
        }
        throw new EntityNotFoundException();
    }

    public void createMember(ProjectMember member) {
        Project p = projectService.find(member.getProject().getId());
        ProjectMember projectMember = null;
        if (p.getProjectMembers() != null) {
            for (ProjectMember pm : p.getProjectMembers()) {
                if (pm.getUser().getId() == member.getUser().getId() && pm.getProject().getId() == member.getProject().getId()) {
                    projectMember = pm;
                    break;
                }
            }
        }
        if (projectMember == null) projectMember = member;
        projectMember.setRole(member.getRole());
        projectService.link(projectMember);
        try {
            String msg = "You have been added to " + project.getName() + " project";
            String obj = "New Project";
            emailService.sendEmail(projectMember.getUser().getEmail(), obj, msg);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
