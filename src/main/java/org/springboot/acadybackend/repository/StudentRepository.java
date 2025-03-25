package org.springboot.acadybackend.repository;

import org.springboot.acadybackend.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByUsernameIgnoreCase(String username);
    Optional<Student> findByEmail(String email);
}