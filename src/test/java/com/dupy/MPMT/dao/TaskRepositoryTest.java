package com.dupy.MPMT.dao;

import com.dupy.MPMT.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setName("Test Task");
        testTask.setDescription("This is a test task");

        taskRepository.save(testTask);
    }

    @Test
    void testSaveTask() {
        Task newTask = new Task();
        newTask.setName("New Task");
        newTask.setDescription("Another task description");

        Task savedTask = taskRepository.save(newTask);

        assertNotNull(savedTask.getId());
        assertEquals("New Task", savedTask.getName());
    }

    @Test
    void testFindById_Success() {
        Optional<Task> foundTask = taskRepository.findById(testTask.getId());

        assertTrue(foundTask.isPresent());
        assertEquals("Test Task", foundTask.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Task> foundTask = taskRepository.findById(999);

        assertFalse(foundTask.isPresent());
    }

    @Test
    void testDeleteTask() {
        taskRepository.delete(testTask);
        Optional<Task> deletedTask = taskRepository.findById(testTask.getId());

        assertFalse(deletedTask.isPresent());
    }
}
