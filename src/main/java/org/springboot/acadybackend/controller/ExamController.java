package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Exam;
import org.springboot.acadybackend.service.impl.ExamAIServiceImpl;
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
    private final ExamAIServiceImpl examAIService;

    public ExamController(ExamServiceImpl examService, ExamAIServiceImpl examAIService) {
        this.examService = examService;
        this.examAIService = examAIService;
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
        this.examAIService.deleteAllByExamId(id);
        this.examService.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Examen eliminado con éxito"));
    }
}