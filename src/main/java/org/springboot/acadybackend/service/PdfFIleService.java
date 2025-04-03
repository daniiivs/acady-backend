package org.springboot.acadybackend.service;

import org.springboot.acadybackend.entity.PdfFile;

import java.util.List;
import java.util.Optional;

public interface PdfFIleService {
    List<PdfFile> findAllBySubjectId(String subjectId);
    Optional<PdfFile> findById(String pdfId);
    void save(PdfFile pdfFile);
    void deleteById(String id);
    void deleteAllByChapterId(String chapterId);
    void deleteAllBySubjectId(String subjectId);
    void deleteAllByStudentId(String studentId);
}
