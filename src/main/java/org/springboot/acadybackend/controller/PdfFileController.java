package org.springboot.acadybackend.controller;

import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springboot.acadybackend.entity.PdfFile;
import org.springboot.acadybackend.service.impl.PdfFileServiceImpl;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class PdfFileController {

    private final GridFsTemplate gridFsTemplate;
    private final PdfFileServiceImpl pdfFileService;

    public PdfFileController(GridFsTemplate gridFsTemplate, PdfFileServiceImpl pdfFileService) {
        this.gridFsTemplate = gridFsTemplate;
        this.pdfFileService = pdfFileService;
    }

    @PostMapping("/upload/{chapterId}&&{subjectId}&&{studentId}")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String chapterId, @PathVariable String subjectId, @PathVariable String studentId) throws IOException {
        // 1. Almacenar en GridFS
        ObjectId fileId = gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        );

        // 2. Guardar metadatos
        PdfFile pdfFile = new PdfFile();
        pdfFile.setFilename(file.getOriginalFilename());
        pdfFile.setContentType(file.getContentType());
        pdfFile.setGridFsId(fileId.toString());
        pdfFile.setStudentId(studentId);
        pdfFile.setSubjectId(subjectId);
        pdfFile.setChapterId(chapterId);
        pdfFileService.save(pdfFile);

        return ResponseEntity.ok(Collections.singletonMap("message", "Documento almacenado exitosamente"));
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<PdfFile> getPdfMetadata(@PathVariable String id) {
        PdfFile file = pdfFileService.findById(id)
                .orElseThrow(() -> new RuntimeException("PDF no encontrado"));
        return ResponseEntity.ok(file);
    }

    @GetMapping("/subject/{id}")
    public ResponseEntity<List<PdfFile>> getPdfMetadataByChapter(@PathVariable String id) {
        List<PdfFile> files = pdfFileService.findAllBySubjectId(id);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String id) throws IOException {
        // 1. Obtener metadatos
        PdfFile pdfFile = pdfFileService.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        // 2. Buscar en GridFS
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(pdfFile.getGridFsId())));

        // 3. Obtener el InputStream del archivo
        InputStream inputStream = gridFsTemplate.getResource(file).getInputStream();

        // 4. Devolver el PDF como recurso descargable
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfFile.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id) {
        PdfFile pdfFile = pdfFileService.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        gridFsTemplate.delete(new Query(Criteria.where("_id").is(pdfFile.getGridFsId())));
        pdfFileService.deleteById(id);

        return ResponseEntity.ok(Collections.singletonMap("message", "Documento eliminado exitosamente"));
    }
}
