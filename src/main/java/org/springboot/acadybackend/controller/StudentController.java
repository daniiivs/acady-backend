package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Student;
import org.springboot.acadybackend.service.impl.StudentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentServiceImpl studentService;

    public StudentController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/all")
    public List<Student> students() {
        return studentService.findAll();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        Optional<Student> student = this.studentService.findByEmailIgnoreCase(email);
        if (student.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(student.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No se encontró un estudiante con email: " + email));
        }
    }
}
