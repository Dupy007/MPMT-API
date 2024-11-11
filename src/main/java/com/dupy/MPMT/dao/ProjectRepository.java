package com.dupy.MPMT.dao;

import com.dupy.MPMT.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
