package com.dupy.MPMT.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectMemberCreate {
    private Project project;
    private User user;
    @NotNull(message = "Role undefined")
    @Size(min = 1, max = 3, message = "1=admin,2=member,3=observateur")
    private String role;
}
