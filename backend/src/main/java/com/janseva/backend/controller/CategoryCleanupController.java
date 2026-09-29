package com.janseva.backend.controller;

import com.janseva.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/debug")
public class CategoryCleanupController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/clear-categories")
    public String clearCategories() {

        categoryRepository.deleteAll();

        return "Categories Deleted";
    }
}