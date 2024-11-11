package com.dupy.MPMT.dao;

import com.dupy.MPMT.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
