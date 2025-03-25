package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Subject;
import org.springboot.acadybackend.service.impl.SubjectServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectServiceImpl subjectService;

    public SubjectController(SubjectServiceImpl subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<Subject>> currentSubjects(@PathVariable String id) {
        List<Subject> subjects = this.subjectService.findAllByStudentId(id);
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Subject subject) {
        this.subjectService.save(subject);
        return ResponseEntity.ok(Collections.singletonMap("message", "Asignatura creada con éxito"));
    }
}
