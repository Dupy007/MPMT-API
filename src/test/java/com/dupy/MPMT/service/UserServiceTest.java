package com.dupy.MPMT.service;

import com.dupy.MPMT.dao.UserRepository;
import com.dupy.MPMT.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
    }

    @Test
    void testFindAll() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFind() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.find(1);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testFind_NotFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        User result = userService.find(2);

        assertNull(result);
        verify(userRepository, times(1)).findById(2);
    }

    @Test
    void testSave() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testFindByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

}