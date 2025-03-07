package com.dupy.MPMT.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    private int id;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
    private String name;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date due_date;
    private int priority;
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date end_date;
    @OneToMany(mappedBy = "task")
    private List<TaskHistory> taskHistory = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "fk_project", nullable = false)
    @JsonIgnore
    private Project project;
//    @JsonIgnore
    @ManyToOne
    @Nullable
    @JoinColumn(name = "assigned")
    private User assigned;

    public static Task parse(TaskCreate data) {
        Task task = new Task();
        task.setName(data.getName());
        task.setDescription(data.getDescription());
        task.setDue_date(data.getDue_date());
        task.setEnd_date(data.getEnd_date());
        task.setPriority(data.getPriority());
        task.setStatus(data.getStatus());
        task.setProject(data.getProject());
        task.setAssigned(data.getAssigned());
        return task;
    }

    public String myString() {
        return "Task{" +
                "end_date=" + end_date +
                ", status=" + status +
                ", priority=" + priority +
                ", due_date=" + due_date +
                ", name='" + name + '\'' +
                '}';
    }

    public int getProjectId() {
        return project.getId();
    }

    public UserView getAssigned() {
        if (assigned != null) {
            return UserView.parse(assigned);
        }
        return null;
    }
}
