package org.springboot.acadybackend.repository;

import org.springboot.acadybackend.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findAllByStudentId(String studentId);
    List<Task> findAllBySubjectId(String subjectId);
    void deleteAllById(String id);
    void deleteAllBySubjectId(String subjectId);
}
