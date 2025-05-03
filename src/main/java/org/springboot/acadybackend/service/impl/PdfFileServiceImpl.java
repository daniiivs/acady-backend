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
        // Busca todos los archivos asociados al tema
        List<PdfFile> files = pdfFileRepository.findAllByChapterId(chapterId);

        // Elimina cada archivo de GridFS (si las ids coinciden)
        for (PdfFile file : files) {
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(file.getGridFsId())));
        }

        // Eliminar los metadatos de la base de datos
        pdfFileRepository.deleteAllByChapterId(chapterId);
    }

    @Override
    public void deleteAllBySubjectId(String subjectId) {
        // Busca todos los archivos asociados a la asignatura
        List<PdfFile> files = pdfFileRepository.findAllBySubjectId(subjectId);

        // Eliminar archivos de GridFS (si las ids coinciden)
        for (PdfFile file : files) {
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(file.getGridFsId())));
        }

        // Eliminar los metadatos de la base de datos
        pdfFileRepository.deleteAllBySubjectId(subjectId);
    }
}
