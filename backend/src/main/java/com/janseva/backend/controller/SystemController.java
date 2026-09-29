package com.janseva.backend.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*")
public class SystemController {

    @GetMapping("/health")
    public Map<String, Object> health() {

        return Map.of(

                "success", true,

                "status", "RUNNING",

                "time",
                LocalDateTime.now()
        );
    }
}