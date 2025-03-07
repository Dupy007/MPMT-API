package com.dupy.MPMT.dao;

import com.dupy.MPMT.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("securePassword");

        userRepository.save(testUser);
    }

    @Test
    void testFindByUsername_Success() {
        Optional<User> foundUser = userRepository.findByUsername("testUser");

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<User> foundUser = userRepository.findByUsername("unknownUser");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindByEmail_Success() {
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<User> foundUser = userRepository.findByEmail("unknown@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindUserByUsernameOrEmail_FoundByUsername() {
        Optional<User> foundUser = userRepository.findUserByUsernameOrEmail("testUser", "wrong@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
    }

    @Test
    void testFindUserByUsernameOrEmail_FoundByEmail() {
        Optional<User> foundUser = userRepository.findUserByUsernameOrEmail("wrongUser", "test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindUserByUsernameOrEmail_NotFound() {
        Optional<User> foundUser = userRepository.findUserByUsernameOrEmail("wrongUser", "wrong@example.com");

        assertFalse(foundUser.isPresent());
    }
}
