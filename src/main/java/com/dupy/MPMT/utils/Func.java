package com.dupy.MPMT.utils;

import com.dupy.MPMT.dao.UserRepository;
import com.dupy.MPMT.exception.EntityUnAuthorizedException;
import com.dupy.MPMT.model.Project;
import com.dupy.MPMT.model.ProjectMember;
import com.dupy.MPMT.model.User;
import com.dupy.MPMT.service.ProjectService;
import com.dupy.MPMT.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class Func {
    public static String generateToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return Encoder.encode(generatedString);
    }
    public static User canAcces(UserService userService,String token) {
        for (User u : userService.findAll()) {
            if (token.equals("Bearer " + u.getToken()))
                return u;
        }
        throw new EntityUnAuthorizedException();
    }
    public static Project canAccesProject(User user, int id) {
        for (ProjectMember member : user.getProjectMembers()){
            if (member.getProject().getId() == id){
                return member.getProject();
            }
        }
        throw new EntityUnAuthorizedException();
    }
    public static Project canEditProject(User user, int id) {
        for (ProjectMember member : user.getProjectMembers()){
            if (!member.getRole().equals("1")){
                continue;
            }
            if (member.getProject().getId() == id){
                return member.getProject();
            }
        }
        throw new EntityUnAuthorizedException();
    }
    public static Project canEditTask(User user, int id) {
        for (ProjectMember member : user.getProjectMembers()){
            if (member.getRole().equals("3")){
                continue;
            }
            if (member.getProject().getId() == id){
                return member.getProject();
            }
        }
        throw new EntityUnAuthorizedException();
    }
}
