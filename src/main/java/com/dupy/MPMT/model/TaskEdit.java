package com.dupy.MPMT.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class TaskEdit {
    @NotBlank(message = "Name undefined")
    @Size(message = "Name 3 character min", min = 3)
    private String name;
    private String description;
    @NotNull(message = "due_date undefined")
    private Date due_date;
    @NotEmpty(message = "priority undefined")
    private int priority;
    @NotEmpty(message = "status undefined")
    private int status;
    private Date end_date;
    @NotNull(message = "project undefined")
    private Project project;
    private User assigned;
}
