package com.learning.platform.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.learning.platform.entity.Course;
import com.learning.platform.entity.Enrollment;
import com.learning.platform.entity.Subtopic;
import com.learning.platform.entity.SubtopicProgress;
import com.learning.platform.entity.User;
import com.learning.platform.repository.CourseRepository;
import com.learning.platform.repository.EnrollmentRepository;
import com.learning.platform.repository.SubtopicProgressRepository;
import com.learning.platform.repository.SubtopicRepository;
import com.learning.platform.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningService {
    private final EnrollmentRepository enrollmentRepository;
    private final SubtopicProgressRepository progressRepository;
    private final CourseRepository courseRepository;
    private final SubtopicRepository subtopicRepository;
    private final UserRepository userRepository;

    @Transactional
    public EnrollmentResponseDTO enroll(String email, String courseId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found"));

        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Already enrolled");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());

        Enrollment saved = enrollmentRepository.save(enrollment);

        return new EnrollmentResponseDTO(
                saved.getId(),
                course.getId(),
                course.getTitle(),
                saved.getEnrolledAt()
        );
    }

    @Transactional
    public SubtopicProgress markComplete(String email, String subtopicId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtopic not found"));
        
        Enrollment enrollment = enrollmentRepository.findByUserAndCourse(user, subtopic.getTopic().getCourse())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled in this course"));

        if (progressRepository.existsByEnrollmentAndSubtopic(enrollment, subtopic)) {
            // Idempotent: return existing record or just create new if not exists logic is preferred
            return progressRepository.findByEnrollment(enrollment).stream()
                    .filter(p -> p.getSubtopic().getId().equals(subtopicId))
                    .findFirst().orElseThrow();
        }

        SubtopicProgress progress = new SubtopicProgress();
        progress.setEnrollment(enrollment);
        progress.setSubtopic(subtopic);
        progress.setCompletedAt(LocalDateTime.now());
        return progressRepository.save(progress);
    }

    public ProgressResponseDTO getProgress(Long enrollmentId, String email) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found"));

        if (!enrollment.getUser().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        long total = subtopicRepository.countByCourseId(enrollment.getCourse().getId());
        List<SubtopicProgress> completedList = progressRepository.findByEnrollment(enrollment);
        long completedCount = completedList.size();
        
        double percentage = total == 0 ? 0 : ((double) completedCount / total) * 100;

        return new ProgressResponseDTO(
            enrollment.getId(),
            enrollment.getCourse().getId(),
            enrollment.getCourse().getTitle(),
            total,
            completedCount,
            percentage,
            completedList.stream().map(p -> new CompletedItemDTO(p.getSubtopic().getId(), p.getSubtopic().getTitle(), p.getCompletedAt())).collect(Collectors.toList())
        );
    }
}