package com.example.todo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {

    private List<Task> tasks = new ArrayList<>(List.of(
            new Task(1L, "Learn Spring Boot", false),
            new Task(2L, "Build my first API", true)
    ));
    private Long nextId = 3L;


    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return tasks;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task newTask) {
        newTask.setId(nextId);
        nextId++;
        tasks.add(newTask);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setTitle(updatedTask.getTitle());
                task.setCompleted(updatedTask.isCompleted());
                return ResponseEntity.ok(task);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        boolean removed = tasks.removeIf(task -> task.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}