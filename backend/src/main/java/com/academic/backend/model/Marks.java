package com.academic.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "marks")
public class Marks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;
    private Double maxScore;
    private LocalDate examDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    // Manual getters since Lombok might not work
    public Double getScore() { return score; }
    public Double getMaxScore() { return maxScore; }
    public void setScore(Double score) { this.score = score; }
    public void setMaxScore(Double maxScore) { this.maxScore = maxScore; }
}