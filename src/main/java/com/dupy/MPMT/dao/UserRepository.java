package com.dupy.MPMT.dao;

import com.dupy.MPMT.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByUsernameOrEmail(String username, String email);
}
