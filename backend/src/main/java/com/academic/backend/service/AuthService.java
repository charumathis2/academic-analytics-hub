package com.academic.backend.service;

import com.academic.backend.model.Student;
import com.academic.backend.repository.StudentRepository;
import com.academic.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> register(Student student) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (studentRepo.existsByEmail(student.getEmail())) {
                response.put("success", false);
                response.put("message", "Email already exists!");
                return response;
            }
            student.setPassword(
                passwordEncoder.encode(student.getPassword())
            );
            student.setRole("STUDENT");
            Student saved = studentRepo.save(student);
            String token = jwtUtil.generateToken(saved.getEmail());
            response.put("success", true);
            response.put("token", token);
            response.put("studentId", saved.getId());
            response.put("name", saved.getName());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }

    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            Student student = studentRepo.findByEmail(email).orElse(null);
            if (student == null) {
                response.put("success", false);
                response.put("message", "Student not found!");
                return response;
            }
            if (!passwordEncoder.matches(password, student.getPassword())) {
                response.put("success", false);
                response.put("message", "Wrong password!");
                return response;
            }
            String token = jwtUtil.generateToken(email);
            response.put("success", true);
            response.put("token", token);
            response.put("studentId", student.getId());
            response.put("name", student.getName());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }
}