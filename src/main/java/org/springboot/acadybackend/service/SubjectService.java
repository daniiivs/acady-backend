package org.springboot.acadybackend.service;

import org.springboot.acadybackend.entity.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAllByStudentId(String studentId);
    Subject save(Subject subject);
}
