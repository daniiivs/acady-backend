package org.springboot.acadybackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springboot.acadybackend.entity.Chapter;
import org.springboot.acadybackend.entity.Exam;
import org.springboot.acadybackend.entity.ExamAI;
import org.springboot.acadybackend.entity.PdfFile;
import org.springboot.acadybackend.service.ChapterService;
import org.springboot.acadybackend.service.impl.ExamAIServiceImpl;
import org.springboot.acadybackend.service.impl.PdfFileServiceImpl;
import org.springboot.acadybackend.utils.PdfUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/aiexam")
public class ExamAIController {

    private final ExamAIServiceImpl examAIService;
    private final PdfFileServiceImpl pdfFileService;
    private final GridFsTemplate gridFsTemplate;
    private final ChapterService chapterService;

    public ExamAIController(ExamAIServiceImpl examAIService, PdfFileServiceImpl pdfFileService, GridFsTemplate gridFsTemplate, ChapterService chapterService) {
        this.examAIService = examAIService;
        this.pdfFileService = pdfFileService;
        this.gridFsTemplate = gridFsTemplate;
        this.chapterService = chapterService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<ExamAI> exam = this.examAIService.findById(id);
        if (exam.isPresent()) {
            return ResponseEntity.ok(exam.get());
        } else {
            return ResponseEntity.notFound().build();
        }
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteExamAI(@PathVariable String id) {
        this.examAIService.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Examen de IA eliminado con éxito"));
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveExamAI(@RequestBody ExamAI examAI) {
        this.examAIService.saveExamAI(examAI);
        return ResponseEntity.ok(Collections.singletonMap("message", "Examen de IA guardado"));
    }

    @PostMapping("/generate/{id}")
    public Mono<ResponseEntity<Map<String, String>>> generateExam(@RequestBody List<String> chapterIds, @PathVariable String id) throws IOException {
        StringBuilder fullText = new StringBuilder(); // Texto del examen

        // Variables de referencia (para dentro del return)
        AtomicReference<String> studentIdRef = new AtomicReference<>("");
        AtomicReference<String> subjectIdRef = new AtomicReference<>("");
        boolean first = true;

        for (String chapterId : chapterIds) {
            List<PdfFile> pdfFiles = pdfFileService.findAllByChapterId(chapterId); // Obtener metadatos de pdfs

            if (pdfFiles.isEmpty()) { // Si un tema no tiene pdfs, dar un error indicándolo
                Optional<Chapter> emptyChapter = chapterService.getById(chapterId);
                return emptyChapter.map(chapter -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "\"Tema " + chapter.getNumber() + ": " + chapter.getName() + "\" no tiene documentos."))))
                        .orElseGet(() -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Hay un tema sin documentos."))));
            }

            for (PdfFile pdfFile : pdfFiles) {
                GridFSFile gridFsFile = gridFsTemplate.findOne( // Obtener los pdfs por los metadatos
                        new Query(Criteria.where("_id").is(pdfFile.getGridFsId()))
                );

                // Extraemos el texto del pdf
                InputStream inputStream = gridFsTemplate.getResource(gridFsFile).getInputStream();
                String text = PdfUtils.exactText(inputStream);
                fullText.append(text).append("\n");

                if (first) { // Establecemos las variables de referencia (solo una vez)
                    studentIdRef.set(pdfFile.getStudentId());
                    subjectIdRef.set(pdfFile.getSubjectId());
                    first = false;
                }
            }
        }

        // Generamos el examen IA en formato JSON
        Mono<String> examMono = this.examAIService.requestExamFromGemini(fullText.toString());

        return examMono.flatMap(examJson -> {
            try {
                // Limpiamos caracteres innecesarios
                String jsonContent = examJson
                        .replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();

                // Creamos el objeto ExamAI
                ExamAI examAI = this.examAIService.parseExamAI(jsonContent);
                examAI.setStudentId(studentIdRef.get());
                examAI.setSubjectId(subjectIdRef.get());
                examAI.setExamId(id);
                examAI.setChapterIds(chapterIds);
                examAI.setGrade((double) -1); // Nota -1 para indicar que no se ha realizado

                this.examAIService.saveExamAI(examAI);

                return Mono.just(ResponseEntity.ok(Collections.singletonMap("message", "Examen generado con éxito")));
            } catch (JsonProcessingException e) {
                return Mono.error(new RuntimeException("Error al procesar el JSON: " + e.getMessage()));
            }
        });
    }
}
