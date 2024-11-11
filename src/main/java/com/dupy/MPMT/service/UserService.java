package com.dupy.MPMT.service;

import com.dupy.MPMT.dao.UserRepository;
import com.dupy.MPMT.model.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    public User find(int id) {
        return repository.findById(id).orElse(null);
    }

    public User save(User data) {
        return repository.save(data);
    }
    public User findByMailOrUsername(String value){
        for (User u : findAll()) {
            if (u.getEmail().equals(value) || u.getUsername().equals(value)) {
                return u;
            }
        }
        return null;
    }
}
