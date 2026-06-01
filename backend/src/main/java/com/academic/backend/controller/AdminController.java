package com.academic.backend.controller;

import com.academic.backend.model.Marks;
import com.academic.backend.model.Student;
import com.academic.backend.model.Deadline;
import com.academic.backend.repository.MarksRepository;
import com.academic.backend.repository.StudentRepository;
import com.academic.backend.repository.DeadlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private MarksRepository marksRepo;

    @Autowired
    private DeadlineRepository deadlineRepo;

    // Get all students
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    // Add marks
    @PostMapping("/marks")
    public Marks addMarks(@RequestBody Marks marks) {
        return marksRepo.save(marks);
    }

    // Get all deadlines
    @GetMapping("/deadlines")
    public List<Deadline> getAllDeadlines() {
        return deadlineRepo.findAll();
    }

    // Leaderboard
    @GetMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderboard() {
        List<Student> students = studentRepo.findAll();
        List<Map<String, Object>> leaderboard = new ArrayList<>();

        for (Student student : students) {
            List<Marks> marksList = marksRepo
                .findByStudentId(student.getId());

            if (!marksList.isEmpty()) {
                double avg = 0;
                for (Marks m : marksList) {
                    avg += (m.getScore() / m.getMaxScore()) * 100;
                }
                avg = avg / marksList.size();

                Map<String, Object> entry = new HashMap<>();
                entry.put("studentId", student.getId());
                entry.put("name", student.getName());
                entry.put("department", student.getDepartment());
                entry.put("averageScore", Math.round(avg));
                entry.put("totalSubjects", marksList.size());
                entry.put("grade",
                    avg >= 75 ? "A" :
                    avg >= 60 ? "B" :
                    avg >= 50 ? "C" : "D");
                leaderboard.add(entry);
            }
        }

        // Sort by highest score
        leaderboard.sort((a, b) ->
            ((Long) b.get("averageScore"))
                .compareTo((Long) a.get("averageScore")));

        // Add rank
        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).put("rank", i + 1);
        }

        return leaderboard;
    }

    // Class overall analytics
    @GetMapping("/analytics")
    public Map<String, Object> getClassAnalytics() {
        List<Student> students = studentRepo.findAll();
        List<Double> allAverages = new ArrayList<>();

        for (Student student : students) {
            List<Marks> marksList = marksRepo
                .findByStudentId(student.getId());
            if (!marksList.isEmpty()) {
                double avg = 0;
                for (Marks m : marksList) {
                    avg += (m.getScore() / m.getMaxScore()) * 100;
                }
                allAverages.add(avg / marksList.size());
            }
        }

        double classAvg = allAverages.stream()
            .mapToDouble(Double::doubleValue)
            .average().orElse(0);

        double highest = allAverages.stream()
            .mapToDouble(Double::doubleValue)
            .max().orElse(0);

        double lowest = allAverages.stream()
            .mapToDouble(Double::doubleValue)
            .min().orElse(0);

        long passCount = allAverages.stream()
            .filter(a -> a >= 50).count();

        Map<String, Object> result = new HashMap<>();
        result.put("totalStudents", students.size());
        result.put("classAverage", Math.round(classAvg));
        result.put("highestScore", Math.round(highest));
        result.put("lowestScore", Math.round(lowest));
        result.put("passCount", passCount);
        result.put("failCount", allAverages.size() - passCount);

        return result;
    }
}