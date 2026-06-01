package com.academic.backend.controller;

import com.academic.backend.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/student/{id}")
    public Map<String, Object> getAnalytics(@PathVariable Long id) {
        return analyticsService.getStudentAnalytics(id);
    }
}