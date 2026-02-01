package com.learning.platform.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.learning.platform.entity.Course;
import com.learning.platform.entity.SubtopicProgress;
import com.learning.platform.repository.CourseRepository;
import com.learning.platform.repository.SubtopicRepository;
import com.learning.platform.service.LearningService;
import com.learning.platform.service.SearchService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;
    private final SubtopicRepository subtopicRepository;
    private final SearchService searchService;
    private final LearningService learningService;

    // ---------------- PUBLIC ----------------

    @GetMapping("/courses")
    public Map<String, List<Course>> getAllCourses() {
        return Map.of("courses", courseRepository.findAll());
    }

    @GetMapping("/courses/{id}")
    public Course getCourse(@PathVariable String id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }

    @GetMapping("/search")
    public Object search(@RequestParam String q) {
        return searchService.search(q);
    }

    // ---------------- AUTHENTICATED ----------------

    @PostMapping("/courses/{courseId}/enroll")
    @SecurityRequirement(name = "bearerAuth")
    public Object enroll(
            @PathVariable String courseId,
            Authentication authentication
    ) {
        return learningService.enroll(authentication.getName(), courseId);
    }

    // ✅ ADD THIS METHOD HERE
    @PostMapping("/subtopics/{subtopicId}/complete")
    @SecurityRequirement(name = "bearerAuth")
    public Map<String, Object> complete(
            @PathVariable String subtopicId,
            Authentication authentication
    ) {
        SubtopicProgress progress =
                learningService.markComplete(authentication.getName(), subtopicId);

        return Map.of(
                "subtopicId", progress.getSubtopic().getId(),
                "completed", true,
                "completedAt", progress.getCompletedAt()
        );
    }

    @GetMapping("/enrollments/{id}/progress")
    @SecurityRequirement(name = "bearerAuth")
    public Object getProgress(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return learningService.getProgress(id, authentication.getName());
    }
}
