package org.springboot.acadybackend.repository;

import org.springboot.acadybackend.entity.ExamAI;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAIRepository extends MongoRepository<ExamAI, String> {
    Optional<List<ExamAI>> findAllByExamId(String examId);
    Optional<List<ExamAI>> findAllByStudentId(String studentId);
    void deleteAllByExamId(String examId);
    void deleteAllByChapterIdsContaining(String chapterId);
    void deleteAllBySubjectId(String subjectId);
}
