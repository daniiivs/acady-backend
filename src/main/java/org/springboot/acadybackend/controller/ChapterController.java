package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Chapter;
import org.springboot.acadybackend.service.impl.ChapterServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/chapters")
public class ChapterController {

    private final ChapterServiceImpl chapterService;

    public ChapterController(ChapterServiceImpl chapterService) {
        this.chapterService = chapterService;
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        this.chapterService.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tema eliminado con éxito"));
    }

    @DeleteMapping("/delete/subject/{id}")
    public ResponseEntity<?> deleteAllBySubjectId(@PathVariable String id) {
        this.chapterService.deleteAllBySubjectId(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Temas eliminados con éxito"));
    }
}
