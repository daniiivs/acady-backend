package org.springboot.acadybackend.service.impl;

import org.springboot.acadybackend.entity.Student;
import org.springboot.acadybackend.repository.StudentRepository;
import org.springboot.acadybackend.service.AuthService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final StudentRepository studentRepository;

    public AuthServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return List.of((Student) this.studentRepository.findAll());
    }

    @Override
    public Optional<Student> findByUsername(String username) {
        return this.studentRepository.findByUsername(username);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return this.studentRepository.findByEmail(email);
    }

    @Override
    public Student save(Student student) {
        return this.studentRepository.save(student);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByUsername(username);
        if (student.isPresent()) {
            Student studentObject = student.get();
            return User.builder()
                    .username(studentObject.getUsername())
                    .password(studentObject.getPassword())
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
