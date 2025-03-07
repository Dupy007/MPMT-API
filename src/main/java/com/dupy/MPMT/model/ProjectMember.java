package com.dupy.MPMT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "project_members")
public class ProjectMember {
    @Id
    @GeneratedValue
    private int id;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_project")
    private Project project;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_user")
    private User user;

    @NotNull(message = "Role undefined")
    @Size(min = 1, max = 3, message = "1=admin,2=membre,3=observateur")
    private String role;

    public static ProjectMember parse(ProjectMemberCreate data) {
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(data.getProject());
        projectMember.setUser(data.getUser());
        projectMember.setRole(data.getRole());
        return projectMember;
    }

    public UserView getMember() {
        return UserView.parse(user);
    }

    public String getRoleAttribute() {
        return switch (role) {
            case "1" -> "admin";
            case "2" -> "member";
            case "3" -> "observateur";
            default -> role;
        };
    }
}
