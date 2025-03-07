package com.dupy.MPMT.service;

import com.dupy.MPMT.dao.TaskHistoryRepository;
import com.dupy.MPMT.dao.TaskRepository;
import com.dupy.MPMT.model.Task;
import com.dupy.MPMT.model.TaskHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;
    @Autowired
    private TaskHistoryRepository historyRepository;


    public List<Task> findAll() {
        return (List<Task>) repository.findAll();
    }

    public Task find(int id) {
        return repository.findById(id).orElse(null);
    }

    public Task save(Task data) {
        return repository.save(data);
    }

    public Task link(TaskHistory t) {
        int id = t.getTask().getId();
        historyRepository.save(t);
        return find(id);
    }
}
