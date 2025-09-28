package com.univ.attendograde2v.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/register-page")
    public String registerPage(){
        return "register";
    }

    @GetMapping("/login-page")
    public String loginPage(){
        return "login";
    }
}
