package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Subject;
import org.springboot.acadybackend.entity.Task;
import org.springboot.acadybackend.service.impl.TaskServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all/student/{id}")
    public ResponseEntity<List<Task>> currentTasksByStudentId(@PathVariable String id) {
        List<Task> tasks = this.taskService.findAllByStudentId(id);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/all/subject/{id}")
    public ResponseEntity<List<Task>> currentTasksBySubjectId(@PathVariable String id) {
        List<Task> tasks = this.taskService.findAllBySubjectId(id);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Task task) {
        this.taskService.save(task);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tarea creada con éxito"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        this.taskService.deleteAllById(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tarea eliminada con éxito"));
    }

    @DeleteMapping("/delete/subject/{id}")
    public ResponseEntity<?> deleteBySubjectId(@PathVariable String id) {
        this.taskService.deleteAllBySubjectId(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tareas eliminadas con éxito"));
    }

    @DeleteMapping("/delete/student/{id}")
    public ResponseEntity<?> deleteByStudentId(@PathVariable String id) {
        this.taskService.deleteAllByStudentId(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tareas eliminadas con éxito"));
    }
}
