package com.academic.backend.repository;

import com.academic.backend.model.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
    List<Deadline> findByStudentIdAndIsCompletedFalse(Long studentId);
    List<Deadline> findByDueDateBeforeAndIsCompletedFalse(LocalDateTime time);
}