package com.academic.backend.service;

import com.academic.backend.model.Deadline;
import com.academic.backend.model.Marks;
import com.academic.backend.repository.DeadlineRepository;
import com.academic.backend.repository.MarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private MarksRepository marksRepo;

    @Autowired
    private DeadlineRepository deadlineRepo;

    public Map<String, Object> getStudentAnalytics(Long studentId) {
        List<Marks> marksList = marksRepo.findByStudentId(studentId);

        double avg = 0, highest = 0, lowest = 100;

        if (!marksList.isEmpty()) {
            double total = 0;
            for (Marks m : marksList) {
                double percent = (m.getScore() / m.getMaxScore()) * 100;
                total += percent;
                if (percent > highest) highest = percent;
                if (percent < lowest) lowest = percent;
            }
            avg = total / marksList.size();
        }

        // ⭐ Fix: Filter by STUDENT ID and within 24 hours
        List<Deadline> urgentDeadlines = deadlineRepo
            .findByStudentIdAndIsCompletedFalse(studentId)
            .stream()
            .filter(d -> d.getDueDate() != null &&
                d.getDueDate().isBefore(
                    LocalDateTime.now().plusHours(24)
                ) &&
                d.getDueDate().isAfter(LocalDateTime.now()))
            .toList();

        String performance;
        if (avg >= 75) performance = "GOOD";
        else if (avg >= 50) performance = "AVERAGE";
        else performance = "NEEDS IMPROVEMENT";

        Map<String, Object> result = new HashMap<>();
        result.put("averageScore", Math.round(avg));
        result.put("highestScore", Math.round(highest));
        result.put("lowestScore", Math.round(lowest));
        result.put("totalSubjects", marksList.size());
        result.put("urgentDeadlines", urgentDeadlines.size());
        result.put("performanceLevel", performance);

        return result;
    }
}