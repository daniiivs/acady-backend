package org.springboot.acadybackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springboot.acadybackend.auth.AuthService;
import org.springboot.acadybackend.dto.LoginRequest;
import org.springboot.acadybackend.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        String message = authService.checkAndRegister(student);
        if (!message.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", message));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            // Se crea un token de autenticación
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            // Se autentica al usuario
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Si la autenticación es exitosa, se establece el contexto y se crea la sesión
            // (La cookie de sesión se envía automáticamente)
            request.getSession(true);

            return ResponseEntity.ok(Collections.singletonMap("message", "Login exitoso"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Credenciales inválidas"));
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // Invalidar la sesión
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "Logout successful";
    }
}
