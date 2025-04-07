package org.springboot.acadybackend.repository;

import org.springboot.acadybackend.entity.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends MongoRepository<Exam, String> {
    Optional<List<Exam>> findAllByStudentId(String studentId);
    Optional<List<Exam>> findAllBySubjectId(String subjectId);
    void deleteAllBySubjectId(String subjectId);
    void deleteAllByChapterIdsContaining(String chapterId);
}
