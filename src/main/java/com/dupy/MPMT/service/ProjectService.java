package com.dupy.MPMT.service;

import com.dupy.MPMT.dao.ProjectMemberRepository;
import com.dupy.MPMT.dao.ProjectRepository;
import com.dupy.MPMT.model.Project;
import com.dupy.MPMT.model.ProjectMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository repository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public List<Project> findAll() {
        return (List<Project>) repository.findAll();
    }

    public Project find(int id) {
        return repository.findById(id).orElse(null);
    }

    public Project save(Project data) {
        return repository.save(data);
    }

    public Project link(ProjectMember data) {
        return projectMemberRepository.save(data).getProject();
    }

    public Project unlink(ProjectMember data) {
        int idproject = data.getProject().getId();
        projectMemberRepository.delete(data);
        return find(idproject);
    }
}
