package org.springboot.acadybackend.repository;

import org.springboot.acadybackend.entity.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends MongoRepository<Chapter, String> {
    Optional<List<Chapter>> getAllByStudentId(String studentId);
    Optional<List<Chapter>> getAllBySubjectId(String subjectId);
    void deleteAllBySubjectId(String subjectId);
}
