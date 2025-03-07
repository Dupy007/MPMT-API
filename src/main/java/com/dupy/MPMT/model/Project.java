package com.dupy.MPMT.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue
    private int id;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
    @NotEmpty(message = "name undefined")
    @Size(message = "150 character max", max = 150)
    private String name;
    @NotEmpty(message = "description undefined")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "start_date undefined")
    private Date start_date;
    @Null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date end_date;
    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private List<ProjectMember> projectMembers = new ArrayList<>();
    ;
    @OneToMany(mappedBy = "project")
    private List<Task> tasks = new ArrayList<>();
    ;

    public List<UserView> getMembers() {
        List<UserView> list = new ArrayList<>();
        for (ProjectMember pm : projectMembers) {
            list.add(pm.getMember());
        }
        return list;
    }

}
