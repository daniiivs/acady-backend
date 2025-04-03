package org.springboot.acadybackend.service;

import org.springboot.acadybackend.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    Optional<Subject> findById(String id);
    List<Subject> findAllByStudentId(String studentId);
    void save(Subject subject);
    void deleteById(String id);
}
