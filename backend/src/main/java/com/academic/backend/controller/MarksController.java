package com.academic.backend.controller;

import com.academic.backend.model.Marks;
import com.academic.backend.repository.MarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = "http://localhost:3000")
public class MarksController {

    @Autowired
    private MarksRepository marksRepo;

    @GetMapping("/student/{id}")
    public List<Marks> getMarks(@PathVariable Long id) {
        return marksRepo.findByStudentId(id);
    }

    @PostMapping
    public Marks addMarks(@RequestBody Marks marks) {
        return marksRepo.save(marks);
    }
}
