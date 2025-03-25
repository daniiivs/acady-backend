package org.springboot.acadybackend.service.impl;

import org.springboot.acadybackend.entity.Subject;
import org.springboot.acadybackend.repository.SubjectRepository;
import org.springboot.acadybackend.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Subject save(Subject subject) {
        return this.subjectRepository.save(subject);
    }
}
