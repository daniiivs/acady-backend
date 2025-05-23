package org.springboot.acadybackend.auth;

import org.springboot.acadybackend.entity.Student;
import org.springboot.acadybackend.repository.StudentRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(StudentRepository studentRepository, BCryptPasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Carga un objeto UserDetails que Spring usa para la autenticación
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByUsernameIgnoreCase(username);
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

    // Comprueba si el email y el nombre de usuario están en uso
    public String checkAndRegister(@RequestBody Student student) {
        String error = "";
        if (existsByEmail(student)) {
            error += "email";
        }
        if (existsByUsername(student)) {
            error += "username";
        }
        if (error.isEmpty()) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            this.studentRepository.save(student);
            return "";
        }
        return error;
    }

    private boolean existsByEmail(Student student) {
        return this.studentRepository.findByEmail(student.getEmail()).isPresent();
    }

    private boolean existsByUsername(Student student) {
        return this.studentRepository.findByUsernameIgnoreCase(student.getUsername()).isPresent();
    }
}
