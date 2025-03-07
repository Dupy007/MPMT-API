package com.dupy.MPMT.controller;

import com.dupy.MPMT.exception.EntityUnAuthorizedException;
import com.dupy.MPMT.model.Login;
import com.dupy.MPMT.model.Register;
import com.dupy.MPMT.model.User;
import com.dupy.MPMT.security.JwtUtil;
import com.dupy.MPMT.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController

public class AuthController {
    @Autowired
    private UserService userService;

    private User user;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Register data, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage));
        }
        try {

            String encode = passwordEncoder.encode(data.getPassword());
            data.setPassword(encode);
            user = userService.save(User.parse(data));

            // Si l'authentification réussit, générer un token
            if (user != null) {
                String token = jwtUtil.generateToken(user.getUsername());
                response.setHeader("Authorization", "Bearer " + token);
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Login data, BindingResult bindingResult, HttpServletResponse response) {
        String value = data.getEmail() == null ? data.getUsername() : data.getEmail();
        if (value == null && bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage));
        }
        try {
            user = data.getUsername() == null ? userService.findByEmail(data.getEmail()) : userService.findByUsername(data.getUsername());
            if (user != null) {
                boolean passwordMatch = passwordEncoder.matches(data.getPassword(), user.getPassword());
                if (!passwordMatch) {
                    throw new EntityUnAuthorizedException();
                }
                // Si l'authentification réussit, générer un token
                data.setUsername(user.getUsername());
                String token = jwtUtil.generateToken(user.getUsername());
                response.setHeader("Authorization", "Bearer " + token);
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword()));
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(400).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        user = userService.findByUsername((String) request.getAttribute("username"));
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {
        user = userService.findByUsername((String) request.getAttribute("username"));
        return ResponseEntity.ok(user);
    }
}
