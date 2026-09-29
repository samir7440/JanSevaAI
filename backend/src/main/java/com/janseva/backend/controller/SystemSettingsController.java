package com.janseva.backend.controller;

import com.janseva.backend.model.SystemSettings;

import com.janseva.backend.repository.SystemSettingsRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/settings")
@CrossOrigin(origins = "*")
public class SystemSettingsController {

    @Autowired
    private SystemSettingsRepository repository;

    @GetMapping
    public List<SystemSettings> getSettings() {

        return repository.findAll();
    }

    @PostMapping("/save")
    public SystemSettings saveSettings(

            @RequestBody
            SystemSettings settings

    ) {

        return repository.save(
                settings
        );
    }
}