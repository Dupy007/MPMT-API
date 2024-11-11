package com.dupy.MPMT.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Login {
    @NotBlank(message = "Username non défini")
    @Size(message = "50 character max", max = 50)
    @Column(unique = true)
    private String username;
    @NotBlank(message = "email non défini")
    @Email
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Mot de passe non défini")
    private String password;
}
