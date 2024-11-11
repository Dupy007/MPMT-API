package com.dupy.MPMT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class TaskCreate {
    @NotBlank(message = "Name undefined")
    @Size(message = "Name 3 character min", min = 3)
    private String name;
    private String description;
    @NotNull(message = "due_date undefined")
    private Date due_date;
    private int priority;
    private int status;
    private Date end_date;
    @NotNull(message = "project undefined")
    private Project project;
    private User assigned;
}
