package com.janseva.backend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping
    public Map<String, Object> test() {

        return Map.of(

                "success", true,

                "message",
                "Backend Running Successfully"
        );
    }
}