package com.janseva.backend.controller;

import com.janseva.backend.service.AnalyticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AnalyticsController {


@Autowired
private AnalyticsService analyticsService;

@GetMapping("/analytics")
public Map<String, Object> getAnalytics() {

    return analyticsService
            .getSystemAnalytics();
}


}
