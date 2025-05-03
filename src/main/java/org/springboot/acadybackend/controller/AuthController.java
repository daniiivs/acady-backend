package org.springboot.acadybackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springboot.acadybackend.auth.AuthService;
import org.springboot.acadybackend.entity.Student;
import org.springboot.acadybackend.service.impl.StudentServiceImpl;
import org.springboot.acadybackend.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtils) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        String message = authService.checkAndRegister(student);
        if (!message.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", message));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("token", jwtUtils.generateToken(userDetails.getUsername())));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidar el token actual
        String token = extractTokenFromRequest(request);
        if (token != null) {
            jwtUtils.invalidateToken(token);
        }

        // Limpiar el contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return ResponseEntity.ok().body(Collections.singletonMap("message", "Logout successful"));
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
