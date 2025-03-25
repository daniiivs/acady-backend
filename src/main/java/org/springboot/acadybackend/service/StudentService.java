package org.springboot.acadybackend.service;

import org.springboot.acadybackend.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> findAll();
    Optional<Student> findByUsernameIgnoreCase(String username);
    Optional<Student> findByEmail(String email);
}
