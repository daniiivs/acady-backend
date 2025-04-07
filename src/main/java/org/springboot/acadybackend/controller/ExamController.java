package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Exam;
import org.springboot.acadybackend.service.impl.ExamServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamServiceImpl examService;

    public ExamController(ExamServiceImpl examService) {
        this.examService = examService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Exam exam) {
        this.examService.save(exam);
        return ResponseEntity.ok(Collections.singletonMap("message", "Examen creado con éxito"));
    }

    @GetMapping("/subject/{id}")
    public ResponseEntity<?> getAllBySubjectId(@PathVariable String id) {
        Optional<List<Exam>> exams = this.examService.findAllBySubjectId(id);
        if (exams.isPresent()) {
            return ResponseEntity.ok(exams.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getAllByStudentId(@PathVariable String id) {
        Optional<List<Exam>> exams = this.examService.findAllByStudentId(id);
        if (exams.isPresent()) {
            return ResponseEntity.ok(exams.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        this.examService.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Examen eliminado con éxito"));
    }

    @DeleteMapping("/delete/subject/{id}")
    public ResponseEntity<?> deleteAllBySubjectId(@PathVariable String id) {
        this.examService.deleteAllBySubjectId(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Exámenes eliminados con éxito"));
    }

    @DeleteMapping("/delete/chapter/{id}")
    public ResponseEntity<?> deleteAllByChapterId(@PathVariable String id) {
        this.examService.deleteAllByChapterIdsContaining(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Exámenes eliminados con éxito"));
    }
}