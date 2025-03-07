package com.dupy.MPMT.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class Register {
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
    @NotBlank(message = "Username undefined")
    @Size(message = "50 character max", max = 50)
    @Column(unique = true)
    private String username;
    @NotBlank(message = "email undefined")
    @Email
    @Column(unique = true)
    private String email;
    @NotBlank(message = "password undefined")
    private String password;
    private String token;

    public Register(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
