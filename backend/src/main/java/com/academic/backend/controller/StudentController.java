package com.academic.backend.controller;

import com.academic.backend.model.Student;
import com.academic.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // ✅ Insert sample data (VERY IMPORTANT)
    @GetMapping("/quick-add")
    public String quickAdd() {

        studentRepository.deleteAll(); // optional clean reset

        studentRepository.save(new Student("Charumathi S", "charumathis2006@gmail.com",
                passwordEncoder.encode("pass123"), "AIDS", 85));

        studentRepository.save(new Student("Arun Kumar", "arun@gmail.com",
                passwordEncoder.encode("pass123"), "CSE", 72));

        studentRepository.save(new Student("Priya Sharma", "priya@gmail.com",
                passwordEncoder.encode("pass123"), "CSE", 91));

        studentRepository.save(new Student("Rahul Singh", "rahul@gmail.com",
                passwordEncoder.encode("pass123"), "CSE", 65));

        studentRepository.save(new Student("Sneha Patel", "sneha@gmail.com",
                passwordEncoder.encode("pass123"), "CSE", 78));

        return "Inserted Successfully";
    }

    // ✅ Dashboard Stats API
    @GetMapping("/stats")
    public Object getStats() {

        List<Student> students = studentRepository.findAll();

        int total = students.size();

        int highest = students.stream().mapToInt(Student::getMarks).max().orElse(0);
        int lowest = students.stream().mapToInt(Student::getMarks).min().orElse(0);
        double average = students.stream().mapToInt(Student::getMarks).average().orElse(0);

        long pass = students.stream().filter(s -> s.getMarks() >= 40).count();
        long fail = total - pass;

        return new Object() {
            public int totalStudents = total;
            public int highestScore = highest;
            public int lowestScore = lowest;
            public double averageScore = average;
            public long passCount = pass;
            public long failCount = fail;
        };
    }

    // ✅ Leaderboard
    @GetMapping("/leaderboard")
    public List<Student> getLeaderboard() {
        return studentRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getMarks() - a.getMarks())
                .limit(10)
                .toList();
    }
}