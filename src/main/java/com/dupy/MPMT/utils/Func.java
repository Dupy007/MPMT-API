package com.dupy.MPMT.utils;

import com.dupy.MPMT.exception.EntityUnAuthorizedException;
import com.dupy.MPMT.model.Project;
import com.dupy.MPMT.model.ProjectMember;
import com.dupy.MPMT.model.User;

public class Func {

    public static Project canAccesProject(User user, int id) {
        for (ProjectMember member : user.getProjectMembers()) {
            if (member.getProject().getId() == id) {
                return member.getProject();
            }
        }
        throw new EntityUnAuthorizedException();
    }

    public static Project canEditProject(User user, int id) {
        for (ProjectMember member : user.getProjectMembers()) {
            if (!member.getRole().equals("1")) {
                continue;
            }
            if (member.getProject().getId() == id) {
                return member.getProject();
            }
        }
        throw new EntityUnAuthorizedException();
    }

    public static Project canEditTask(User user, int id) {
        for (ProjectMember member : user.getProjectMembers()) {
            if (member.getRole().equals("3")) {
                continue;
            }
            if (member.getProject().getId() == id) {
                return member.getProject();
            }
        }
        throw new EntityUnAuthorizedException();
    }
}
