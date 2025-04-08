package org.springboot.acadybackend.service;

import org.springboot.acadybackend.entity.ExamAI;

import java.util.List;
import java.util.Optional;

public interface ExamAIService {
    Optional<List<ExamAI>> findAllByExamId(String examId);
    Optional<List<ExamAI>> findAllByStudentId(String studentId);
    void saveExamAI(ExamAI examAI);
}
