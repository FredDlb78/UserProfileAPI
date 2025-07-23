package com.example.todo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {

    private List<Task> tasks = new ArrayList<>(List.of(
            new Task(1L, "Learn Spring Boot","Learn Spring boot and API with ChatGPT", false),
            new Task(2L, "Build my first API","Learn how to build my first API", true)
    ));
    private Long nextId = 3L;


    @GetMapping("/tasks")
    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "This is the list of all tasks",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))),
    })
    public List<Task> getAllTasks() {
        return tasks;
    }

    @PostMapping("/tasks")
    @Operation(summary = "Create a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content) // 👈 aucun corps
    })
    public ResponseEntity<Task> createTask(@RequestBody Task newTask) {
        newTask.setId(nextId);
        nextId++;
        tasks.add(newTask);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Update a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content), // 👈 aucun corps
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content) // 👈 aucun corps
    })
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setTitle(updatedTask.getTitle());
                task.setDescription(updatedTask.getDescription());
                task.setCompleted(updatedTask.isCompleted());
                return ResponseEntity.ok(task);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "Delete a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content), // 👈 aucun corps
    })
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        boolean removed = tasks.removeIf(task -> task.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}