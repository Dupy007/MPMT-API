package com.dupy.MPMT.service;

import com.dupy.MPMT.dao.TaskHistoryRepository;
import com.dupy.MPMT.dao.TaskRepository;
import com.dupy.MPMT.model.Task;
import com.dupy.MPMT.model.TaskHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskHistoryRepository taskHistoryRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskHistory taskHistory;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1);
        task.setName("Test Task");

        taskHistory = new TaskHistory();
        taskHistory.setId(10);
        taskHistory.setTask(task);
    }

    @Test
    void testFindAll() {
        List<Task> tasks = Arrays.asList(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.findAll();

        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getName());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testFind() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Task result = taskService.find(1);

        assertNotNull(result);
        assertEquals("Test Task", result.getName());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void testFind_NotFound() {
        when(taskRepository.findById(2)).thenReturn(Optional.empty());

        Task result = taskService.find(2);

        assertNull(result);
        verify(taskRepository, times(1)).findById(2);
    }

    @Test
    void testSave() {
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.save(task);

        assertNotNull(result);
        assertEquals("Test Task", result.getName());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testLink() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(taskHistoryRepository.save(taskHistory)).thenReturn(taskHistory);

        Task result = taskService.link(taskHistory);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(taskHistoryRepository, times(1)).save(taskHistory);
        verify(taskRepository, times(1)).findById(1);
    }
}
