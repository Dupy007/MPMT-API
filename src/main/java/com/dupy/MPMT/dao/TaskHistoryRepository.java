package com.dupy.MPMT.dao;

import com.dupy.MPMT.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Integer> {
}
