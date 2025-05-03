package org.springboot.acadybackend.repository;

import org.springboot.acadybackend.entity.PdfFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdfFileRepository extends MongoRepository<PdfFile, String> {
    List<PdfFile> findAllByChapterId(String chapterId);
    List<PdfFile> findAllBySubjectId(String subjectId);
    List<PdfFile> findAllByStudentId(String studentId);
    void deleteAllByChapterId(String chapterId);
    void deleteAllBySubjectId(String subjectId);
}
