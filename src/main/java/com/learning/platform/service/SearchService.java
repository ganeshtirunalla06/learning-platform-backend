package com.learning.platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.learning.platform.entity.Course;
import com.learning.platform.entity.Subtopic;
import com.learning.platform.entity.Topic;
import com.learning.platform.repository.CourseRepository;
import com.learning.platform.repository.SubtopicRepository;
import com.learning.platform.repository.TopicRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;

    public SearchResponseDTO search(String query) {
        String q = query.toLowerCase();
        Map<String, SearchResultDTO> resultMap = new HashMap<>();

        // 1. Search Subtopics (Content & Title)
        List<Subtopic> subtopics = subtopicRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(q, q);
        for (Subtopic s : subtopics) {
            SearchResultDTO res = getOrCreateResult(resultMap, s.getTopic().getCourse());
            
            // Match in Title
            if (s.getTitle().toLowerCase().contains(q)) {
                res.getMatches().add(new MatchDTO("subtopic", s.getTopic().getTitle(), s.getId(), s.getTitle(), null));
            }
            // Match in Content (Generate Snippet)
            if (s.getContent().toLowerCase().contains(q)) {
                String snippet = extractSnippet(s.getContent(), q);
                res.getMatches().add(new MatchDTO("content", s.getTopic().getTitle(), s.getId(), s.getTitle(), snippet));
            }
        }

        // 2. Search Topics
        List<Topic> topics = topicRepository.findByTitleContainingIgnoreCase(q);
        for (Topic t : topics) {
            SearchResultDTO res = getOrCreateResult(resultMap, t.getCourse());
            res.getMatches().add(new MatchDTO("topic", t.getTitle(), null, null, null));
        }

        // 3. Search Courses
        List<Course> courses = courseRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q);
        for (Course c : courses) {
            SearchResultDTO res = getOrCreateResult(resultMap, c);
            if (res.getMatches().isEmpty()) { 
                res.getMatches().add(new MatchDTO("course", null, null, null, c.getDescription()));
            }
        }

        return new SearchResponseDTO(query, new ArrayList<>(resultMap.values()));
    }

    private SearchResultDTO getOrCreateResult(Map<String, SearchResultDTO> map, Course c) {
        return map.computeIfAbsent(c.getId(), k -> new SearchResultDTO(c.getId(), c.getTitle()));
    }

    private String extractSnippet(String content, String query) {
        int index = content.toLowerCase().indexOf(query);
        if (index == -1) return "";

        int start = Math.max(0, index - 20);
        int end = Math.min(content.length(), index + query.length() + 20);
        
        String snippet = content.substring(start, end).replace("\n", " ");
        return "..." + snippet + "...";
    }
}