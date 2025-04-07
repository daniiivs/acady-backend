package org.springboot.acadybackend.service.impl;

import org.springboot.acadybackend.entity.Exam;
import org.springboot.acadybackend.repository.ExamRepository;
import org.springboot.acadybackend.service.ExamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public Exam save(Exam exam) {
        return this.examRepository.save(exam);
    }

    @Override
    public void deleteById(String id) {
        this.examRepository.deleteById(id);
    }

    @Override
    public void deleteAllBySubjectId(String subjectId) {
        this.examRepository.deleteAllBySubjectId(subjectId);
    }

    @Override
    public void deleteAllByChapterIdsContaining(String chapterId) {
        this.examRepository.deleteAllByChapterIdsContaining(chapterId);
    }

    @Override
    public Optional<List<Exam>> findAllByStudentId(String studentId) {
        return this.examRepository.findAllByStudentId(studentId);
    }

    @Override
    public Optional<List<Exam>> findAllBySubjectId(String subjectId) {
        return this.examRepository.findAllBySubjectId(subjectId);
    }
}
