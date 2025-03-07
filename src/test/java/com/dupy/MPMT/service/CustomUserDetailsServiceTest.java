package com.dupy.MPMT.service;

import com.dupy.MPMT.dao.UserRepository;
import com.dupy.MPMT.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());

        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("unknownUser"));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findByUsername("unknownUser");
    }
}
