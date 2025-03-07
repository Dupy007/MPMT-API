package com.dupy.MPMT.model;

import lombok.Data;

@Data
public class UserView {
    private String username;
    private String email;
    private Integer id;

    public static UserView parse(User u) {
        UserView userView = new UserView();
        userView.setUsername(u.getUsername());
        userView.setEmail(u.getEmail());
        userView.setId(u.getId());
        return userView;
    }
}
