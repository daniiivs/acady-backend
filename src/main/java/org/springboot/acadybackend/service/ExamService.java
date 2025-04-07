package org.springboot.acadybackend.service;

import org.springboot.acadybackend.entity.Exam;

import java.util.List;
import java.util.Optional;

public interface ExamService {
    Exam save(Exam exam);
    void deleteById(String id);
    void deleteAllBySubjectId(String subjectId);
    void deleteAllByChapterIdsContaining(String chapterId);
    Optional<List<Exam>> findAllByStudentId(String studentId);
    Optional<List<Exam>> findAllBySubjectId(String subjectId);
}
