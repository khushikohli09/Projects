package com.univ.attendograde.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Main menu
    @GetMapping("/")
    public String index() {
        return "index";  // loads index.html
    }

    // Student menu
    @GetMapping("/student")
    public String studentMenu() {
        return "student";  // loads student.html
    }

    // Faculty menu
    @GetMapping("/faculty")
    public String facultyMenu() {
        return "faculty";  // loads faculty.html
    }
}
