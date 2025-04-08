package org.springboot.acadybackend.service.impl;

import org.springboot.acadybackend.entity.PdfFile;
import org.springboot.acadybackend.repository.PdfFileRepository;
import org.springboot.acadybackend.service.PdfFIleService;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PdfFileServiceImpl implements PdfFIleService {

    private final PdfFileRepository pdfFileRepository;
    private final GridFsTemplate gridFsTemplate;

    public PdfFileServiceImpl(PdfFileRepository pdfFileRepository, GridFsTemplate gridFsTemplate) {
        this.pdfFileRepository = pdfFileRepository;
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public List<PdfFile> findAllBySubjectId(String subjectId) {
        return this.pdfFileRepository.findAllBySubjectId(subjectId);
    }

    @Override
    public List<PdfFile> findAllByChapterId(String chapterId) {
        return this.pdfFileRepository.findAllByChapterId(chapterId);
    }

    @Override
    public Optional<PdfFile> findById(String pdfId) {
        return this.pdfFileRepository.findById(pdfId);
    }

    @Override
    public void save(PdfFile pdfFile) {
        this.pdfFileRepository.save(pdfFile);
    }

    @Override
    public void deleteById(String id) {
        this.pdfFileRepository.deleteById(id);
    }

    @Override
    public void deleteAllByChapterId(String chapterId) {
        // 1. Buscar todos los archivos asociados al chapter
        List<PdfFile> files = pdfFileRepository.findAllByChapterId(chapterId);

        // 2. Eliminar cada archivo de GridFS
        for (PdfFile file : files) {
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(file.getGridFsId())));
        }

        // 3. Eliminar los metadatos de la base de datos
        pdfFileRepository.deleteAllByChapterId(chapterId);
    }

    @Override
    public void deleteAllBySubjectId(String subjectId) {
        List<PdfFile> files = pdfFileRepository.findAllBySubjectId(subjectId);

        // Eliminar archivos de GridFS
        for (PdfFile file : files) {
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(file.getGridFsId())));
        }

        // Eliminar metadatos de la base de datos
        pdfFileRepository.deleteAllBySubjectId(subjectId);
    }

    @Override
    public void deleteAllByStudentId(String studentId) {
        List<PdfFile> files = pdfFileRepository.findAllByStudentId(studentId);

        // Eliminar archivos de GridFS
        for (PdfFile file : files) {
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(file.getGridFsId())));
        }

        // Eliminar metadatos de la base de datos
        pdfFileRepository.deleteAllByStudentId(studentId);
    }
}
