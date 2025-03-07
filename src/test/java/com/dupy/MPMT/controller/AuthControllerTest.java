package com.dupy.MPMT.controller;

import com.dupy.MPMT.model.Login;
import com.dupy.MPMT.model.Register;
import com.dupy.MPMT.model.User;
import com.dupy.MPMT.security.JwtUtil;
import com.dupy.MPMT.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        user = new User();
        user.setId(1);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void testRegister() throws Exception {
        Register register = new Register("user1", "password", "user1@example.com");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString())).thenReturn("mockToken");

        mockMvc.perform(post("/api/register")
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user1\",\"password\":\"password\",\"email\":\"user1@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void testLogin() throws Exception {
        Login login = new Login("user1", "password", null);
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("mockToken");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        mockMvc.perform(post("/api/login")
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user1\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void testLoginUnauthorized() throws Exception {
        Login login = new Login("user1", "wrongPassword", null);
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json")
                .content("{\"username\":\"user1\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogout() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(user);

        mockMvc.perform(post("/api/logout")
                .accept("application/json")
                .header("Authorization", "Bearer mockToken")
                .requestAttr("username", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));
    }

    @Test
    void testProfile() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/profile")
                .accept("application/json")
                .header("Authorization", "Bearer mockToken")
                .requestAttr("username", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }
}
