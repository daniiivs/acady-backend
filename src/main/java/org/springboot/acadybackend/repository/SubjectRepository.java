package org.springboot.acadybackend.repository;

import org.springboot.acadybackend.entity.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    List<Subject> findAllByStudentId(String studentId);
}
