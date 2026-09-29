package com.janseva.backend.controller;

import com.janseva.backend.model.Category;
import com.janseva.backend.repository.CategoryRepository;
import com.janseva.backend.service.AIService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class ComplaintCategoryController {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private AIService aiService;

    @GetMapping("/search")
    public List<Category> smartSearch(@RequestParam String query) {

        List<Category> allCategories = repository.findAll();

        // AI Smart Match
        String matchedName = aiService.getSmartCategoryMatch(query, allCategories);

        return allCategories.stream()
                .filter(c -> c.getMainCategory().equalsIgnoreCase(matchedName))
                .toList();
    }

    @GetMapping
    public List<Category> getAll() {
        return repository.findAll();
    }
}