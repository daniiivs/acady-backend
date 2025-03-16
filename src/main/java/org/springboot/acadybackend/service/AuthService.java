package org.springboot.acadybackend.service;

import org.springboot.acadybackend.entity.Student;

import java.util.List;
import java.util.Optional;

public interface AuthService {
    List<Student> findAll();
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    Student save(Student student);
}
