package com.dupy.MPMT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "task_history")
public class TaskHistory {
    @Id
    @GeneratedValue
    private int id;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fk_task")
    private Task task;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String action;
}
