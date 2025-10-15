package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/add")
    public String showAddUserForm() {
        return "add-user";
    }
}