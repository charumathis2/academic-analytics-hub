package com.academic.backend.service;

import com.academic.backend.model.Deadline;
import com.academic.backend.repository.DeadlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeadlineService {

    @Autowired
    private DeadlineRepository deadlineRepo;

    public List<Deadline> getDeadlines(Long studentId) {
        try {
            return deadlineRepo
                .findByStudentIdAndIsCompletedFalse(studentId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Deadline createDeadline(Deadline deadline) {
        return deadlineRepo.save(deadline);
    }

    public Deadline markComplete(Long id) {
        Deadline d = deadlineRepo.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Deadline not found"));
        d.setIsCompleted(true);
        return deadlineRepo.save(d);
    }
}