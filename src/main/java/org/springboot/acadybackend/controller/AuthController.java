package org.springboot.acadybackend.controller;

import org.springboot.acadybackend.entity.Student;
import org.springboot.acadybackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        if (this.authService.findByEmail(student.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este email ya está asociado a una cuenta");
        } else if (this.authService.findByUsername(student.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este nombre de usuario ya está en uso");
        } else {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.save(student));
        }
    }
}
