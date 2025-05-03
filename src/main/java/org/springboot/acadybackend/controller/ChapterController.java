package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Chapter;
import org.springboot.acadybackend.service.impl.ChapterServiceImpl;
import org.springboot.acadybackend.service.impl.ExamAIServiceImpl;
import org.springboot.acadybackend.service.impl.ExamServiceImpl;
import org.springboot.acadybackend.service.impl.PdfFileServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    private final ChapterServiceImpl chapterService;
    private final PdfFileServiceImpl pdfFileService;
    private final ExamServiceImpl examService;
    private final ExamAIServiceImpl examAIService;

    public ChapterController(ChapterServiceImpl chapterService, PdfFileServiceImpl pdfFileService, ExamServiceImpl examService, ExamAIServiceImpl examAIService) {
        this.chapterService = chapterService;
        this.pdfFileService = pdfFileService;
        this.examService = examService;
        this.examAIService = examAIService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Chapter chapter) {
        this.chapterService.save(chapter);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tema creado con éxito"));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAllBySubjectId(@PathVariable String id) {
        Optional<List<Chapter>> chapters = this.chapterService.getAllBySubjectId(id);
        if (chapters.isPresent()) {
            return ResponseEntity.ok(chapters.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getAllByStudentId(@PathVariable String id) {
        Optional<List<Chapter>> chapters = this.chapterService.getAllByStudentId(id);
        if (chapters.isPresent()) {
            return ResponseEntity.ok(chapters.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        this.pdfFileService.deleteAllByChapterId(id);
        this.examAIService.deleteAllByChapterIdsContaining(id);
        this.examService.deleteAllByChapterIdsContaining(id);
        this.chapterService.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tema eliminado con éxito"));
    }
}
