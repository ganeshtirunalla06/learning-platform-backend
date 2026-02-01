package com.learning.platform.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.platform.entity.Course;
import com.learning.platform.entity.Subtopic;
import com.learning.platform.entity.Topic;
import com.learning.platform.repository.CourseRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (courseRepository.count() == 0) {
            ClassPathResource resource = new ClassPathResource("course.json");
            CourseContainer container = objectMapper.readValue(resource.getInputStream(), CourseContainer.class);
            
            for (Course course : container.getCourses()) {
                // Bi-directional relationships need to be set manually before save
                for (Topic topic : course.getTopics()) {
                    topic.setCourse(course);
                    for (Subtopic sub : topic.getSubtopics()) {
                        sub.setTopic(topic);
                    }
                }
                courseRepository.save(course);
            }
            System.out.println("Seed data loaded successfully.");
        }
    }
}
