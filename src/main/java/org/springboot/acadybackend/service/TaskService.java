package org.springboot.acadybackend.service;

import org.springboot.acadybackend.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAllByStudentId(String studentId);
    List<Task> findAllBySubjectId(String subjectId);
    void save(Task task);
    void deleteAllById(String id);
    void deleteAllBySubjectId(String subjectId);
    void deleteAllByStudentId(String studentId);
}
