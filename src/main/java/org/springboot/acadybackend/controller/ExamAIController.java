package org.springboot.acadybackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springboot.acadybackend.entity.Chapter;
import org.springboot.acadybackend.entity.Exam;
import org.springboot.acadybackend.entity.ExamAI;
import org.springboot.acadybackend.entity.PdfFile;
import org.springboot.acadybackend.service.impl.ExamAIServiceImpl;
import org.springboot.acadybackend.service.impl.PdfFileServiceImpl;
import org.springboot.acadybackend.utils.PdfUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/aiexam")
public class ExamAIController {

    private final ExamAIServiceImpl examAIService;
    private final PdfFileServiceImpl pdfFileService;
    private final GridFsTemplate gridFsTemplate;

    public ExamAIController(ExamAIServiceImpl examAIService, PdfFileServiceImpl pdfFileService, GridFsTemplate gridFsTemplate) {
        this.examAIService = examAIService;
        this.pdfFileService = pdfFileService;
        this.gridFsTemplate = gridFsTemplate;
    }

    @GetMapping("/exam/{id}")
    public ResponseEntity<?> getAllByExamId(@PathVariable String id) {
        Optional<List<ExamAI>> exams = this.examAIService.findAllByExamId(id);
        if (exams.isPresent()) {
            return ResponseEntity.ok(exams.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getAllByStudentId(@PathVariable String id) {
        Optional<List<ExamAI>> exams = this.examAIService.findAllByStudentId(id);
        if (exams.isPresent()) {
            return ResponseEntity.ok(exams.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/generate/{id}")
    public Mono<ResponseEntity<Map<String, String>>> generateExam(@RequestBody List<String> chapterIds, @PathVariable String id) throws IOException {
        StringBuilder fullText = new StringBuilder();

        AtomicReference<String> studentIdRef = new AtomicReference<>("");
        AtomicReference<String> subjectIdRef = new AtomicReference<>("");
        boolean first = true;

        for (String chapterId : chapterIds) {
            List<PdfFile> pdfFiles = pdfFileService.findAllByChapterId(chapterId);

            for (PdfFile pdfFile : pdfFiles) {
                GridFSFile gridFsFile = gridFsTemplate.findOne(
                        new Query(Criteria.where("_id").is(pdfFile.getGridFsId()))
                );

                InputStream inputStream = gridFsTemplate.getResource(gridFsFile).getInputStream();
                String text = PdfUtils.exactText(inputStream);
                fullText.append(text).append("\n");

                if (first) {
                    studentIdRef.set(pdfFile.getStudentId());
                    subjectIdRef.set(pdfFile.getSubjectId());
                    first = false;
                }
            }
        }

        Mono<String> examMono = this.examAIService.requestExamFromGemini(fullText.toString());

        return examMono.flatMap(examJson -> {
            try {
                // Limpieza más robusta
                String jsonContent = examJson
                        .replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();

                ExamAI examAI = this.examAIService.parseExamAI(jsonContent);
                examAI.setStudentId(studentIdRef.get());
                examAI.setSubjectId(subjectIdRef.get());
                examAI.setExamId(id);
                examAI.setGrade((double) -1);

                this.examAIService.saveExamAI(examAI);

                return Mono.just(ResponseEntity.ok(Collections.singletonMap("message", "Examen generado con éxito")));
            } catch (JsonProcessingException e) {
                return Mono.error(new RuntimeException("Error al procesar el JSON: " + e.getMessage()));
            }
        });
    }
}
