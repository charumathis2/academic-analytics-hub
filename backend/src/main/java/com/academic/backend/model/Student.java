package com.academic.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String department;
    private String role;
    private int marks; // ⭐ IMPORTANT

    public Student() {
        this.role = "STUDENT";
    }

    public Student(String name, String email, String password, String department, int marks) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.marks = marks;
        this.role = "STUDENT";
    }

    // Getters & Setters

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public int getMarks() { return marks; }

    public void setMarks(int marks) { this.marks = marks; }
}