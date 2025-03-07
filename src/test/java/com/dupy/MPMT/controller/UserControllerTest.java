package com.dupy.MPMT.controller;

import com.dupy.MPMT.exception.EntityNotFoundException;
import com.dupy.MPMT.model.User;
import com.dupy.MPMT.model.UserView;
import com.dupy.MPMT.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;
    private MockHttpServletRequest request;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer mockToken"); // Simuler un token
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Préparer des données mockées
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password");
        user1.setUsername("user1");
        user1.setId(1);
        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password");
        user2.setUsername("user2");
        user2.setId(2);

        List<UserView> userViews = Arrays.asList(
                UserView.parse(user1),
                UserView.parse(user2)
        );
        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/user")
                        .requestAttr("username", "user1")
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(userViews.size()))
        ;
    }

    @Test
    public void testGetUserById() throws Exception {
        // Préparer une donnée mockée
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setUsername("user");
        user.setId(1);

        when(userService.find(1)).thenReturn(user);

        mockMvc.perform(get("/api/user/1")
                        .requestAttr("username", "user1")
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
        ;
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        when(userService.find(1)).thenReturn(null);

        mockMvc.perform(get("/api/user/1")
                        .requestAttr("username", "user1")
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assert (result.getResolvedException() instanceof EntityNotFoundException);
                })
        ;
    }

    @Test
    public void testGetUsersExcludingCurrentUser() throws Exception {
        // Préparer des données mockées
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password");
        user1.setUsername("user1");
        user1.setId(1);
        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password");
        user2.setUsername("user2");
        user2.setId(2);

        when(userService.findByUsername("user1")).thenReturn(user1);
        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users")
                        .requestAttr("username", "user1")
                        .header("Authorization", "Bearer mockToken")
                        .accept("application/json"))
                .andExpect(status().isOk())
        ;
    }
}
