package com.janseva.backend.controller;

import com.janseva.backend.model.User;

import com.janseva.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class AdminUserController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/all")
    public List<User> allUsers() {

        return repository.findAll();
    }
}