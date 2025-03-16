package org.springboot.acadybackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/user/home")
    public String userHome() {
        return "home_user";
    }
}
