package com.dupy.MPMT.controller;

import com.dupy.MPMT.exception.EntityNotFoundException;
import com.dupy.MPMT.model.User;
import com.dupy.MPMT.model.UserView;
import com.dupy.MPMT.service.UserService;
import com.dupy.MPMT.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    private User user;

    @GetMapping("/user")
    public List<User> user(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        Func.canAcces(userService, auth);
        return userService.findAll();
    }
    @GetMapping("/users")
    public List<UserView> users(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        user = Func.canAcces(userService, auth);
        List<User> users = user(auth);
        List<UserView> array = new ArrayList<>();
        for (User u : users){
            if (u.getId()== user.getId()){
                continue;
            }
            array.add(UserView.parse(u));
        }
        return array;

    }

    @GetMapping("/user/{id}")
    public User user(@PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        Func.canAcces(userService, auth);
        user = userService.find(id);
        if (user != null) {
            return user;
        }
        throw new EntityNotFoundException();
    }


}
