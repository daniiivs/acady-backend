package org.springboot.acadybackend.service.impl;

import org.springboot.acadybackend.entity.Student;
import org.springboot.acadybackend.repository.StudentRepository;
import org.springboot.acadybackend.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return this.studentRepository.findAll();
    }

    @Override
    public Optional<Student> findByUsernameIgnoreCase(String username) {
        return this.studentRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public Optional<Student> findByEmailIgnoreCase(String email) {
        return this.studentRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public Student register(Student student) {
        return this.studentRepository.save(student);
    }
}
