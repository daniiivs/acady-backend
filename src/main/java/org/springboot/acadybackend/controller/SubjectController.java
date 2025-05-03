package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Subject;
import org.springboot.acadybackend.service.impl.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectServiceImpl subjectService;
    private final ChapterServiceImpl chapterService;
    private final PdfFileServiceImpl pdfFileService;
    private final TaskServiceImpl taskService;
    private final ExamServiceImpl examService;
    private final ExamAIServiceImpl examAIService;

    public SubjectController(SubjectServiceImpl subjectService, ChapterServiceImpl chapterService, PdfFileServiceImpl pdfFileService, TaskServiceImpl taskService, ExamServiceImpl examService, ExamAIServiceImpl examAIService) {
        this.subjectService = subjectService;
        this.chapterService = chapterService;
        this.pdfFileService = pdfFileService;
        this.taskService = taskService;
        this.examService = examService;
        this.examAIService = examAIService;
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
        this.pdfFileService.deleteAllBySubjectId(id);
        this.examAIService.deleteAllBySubjectId(id);
        this.examService.deleteAllBySubjectId(id);
        this.chapterService.deleteAllBySubjectId(id);
        this.taskService.deleteAllBySubjectId(id);
        this.subjectService.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Asignatura eliminada con éxito"));
    }
}
