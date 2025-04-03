package org.springboot.acadybackend.service.impl;

import org.springboot.acadybackend.entity.Subject;
import org.springboot.acadybackend.repository.SubjectRepository;
import org.springboot.acadybackend.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> findAllByStudentId(String studentId) {
        return this.subjectRepository.findAllByStudentId(studentId);
    }

    @Override
    public void save(Subject subject) {
        this.subjectRepository.save(subject);
    }

    @Override
    public void deleteById(String id) {
        this.subjectRepository.deleteById(id);
    }

    @Override
    public Optional<Subject> findById(String id) {
        return subjectRepository.findById(id);
    }
}
