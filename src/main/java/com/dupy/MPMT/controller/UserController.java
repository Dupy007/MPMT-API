package com.dupy.MPMT.controller;

import com.dupy.MPMT.exception.EntityNotFoundException;
import com.dupy.MPMT.model.User;
import com.dupy.MPMT.model.UserView;
import com.dupy.MPMT.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    private User user;

    @GetMapping("/user")
    public List<?> user() {
        return userService.findAll().stream().map(UserView::parse).toList();
    }

    @GetMapping("/users")
    public List<?> users(HttpServletRequest request) {
        user = userService.findByUsername((String) request.getAttribute("username"));
        return user().stream()
                .filter(u -> ((UserView) u).getId() != user.getId())
                .map(u -> (UserView) u)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{id}")
    public User user(@PathVariable int id) {
        user = userService.find(id);
        if (user != null) {
            return user;
        }
        throw new EntityNotFoundException();
    }


}
