package com.academic.backend.controller;

import com.academic.backend.model.Deadline;
import com.academic.backend.model.Student;
import com.academic.backend.service.DeadlineService;
import com.academic.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deadlines")
@CrossOrigin(origins = "*")
public class DeadlineController {

    @Autowired
    private DeadlineService deadlineService;

    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/student/{id}")
    public List<Deadline> getDeadlines(@PathVariable Long id) {
        return deadlineService.getDeadlines(id);
    }

    @PostMapping
    public Deadline create(@RequestBody Map<String, Object> body) {
        try {
            Deadline deadline = new Deadline();
            deadline.setTitle((String) body.get("title"));
            deadline.setDescription((String) body.get("description"));
            deadline.setIsCompleted(false);

            // Fix priority
            String priority = (String) body.get("priority");
            if (priority != null) {
                deadline.setPriority(
                    Deadline.Priority.valueOf(priority.toUpperCase())
                );
            }

            // Fix date
            String dueDate = (String) body.get("dueDate");
            if (dueDate != null) {
                dueDate = dueDate.replace(" ", "T");
                if (dueDate.length() == 16) {
                    dueDate = dueDate + ":00";
                }
                deadline.setDueDate(LocalDateTime.parse(dueDate));
            }

            // Fix student
            Object studentObj = body.get("student");
            if (studentObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> studentMap =
                    (Map<String, Object>) studentObj;
                Object idObj = studentMap.get("id");
                Long studentId = null;
                if (idObj instanceof Integer) {
                    studentId = ((Integer) idObj).longValue();
                } else if (idObj instanceof Long) {
                    studentId = (Long) idObj;
                } else if (idObj instanceof String) {
                    studentId = Long.parseLong((String) idObj);
                }
                if (studentId != null) {
                    Student student = studentRepo
                        .findById(studentId).orElse(null);
                    deadline.setStudent(student);
                }
            }

            return deadlineService.createDeadline(deadline);

        } catch (Exception e) {
            System.err.println("Error creating deadline: "
                + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(
                "Failed to create deadline: " + e.getMessage()
            );
        }
    }

    @PutMapping("/{id}/complete")
    public Deadline markComplete(@PathVariable Long id) {
        return deadlineService.markComplete(id);
    }
}