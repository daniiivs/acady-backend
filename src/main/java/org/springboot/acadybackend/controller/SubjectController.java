package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Subject;
import org.springboot.acadybackend.service.impl.SubjectServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<Subject> findById(@PathVariable String id) {
        Optional<Subject> subject = this.subjectService.findById(id);
        return subject.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Subject subject) {
        this.subjectService.save(subject);
        return ResponseEntity.ok(Collections.singletonMap("message", "Asignatura creada con éxito"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        this.subjectService.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Asignatura eliminada con éxito"));
    }
}
