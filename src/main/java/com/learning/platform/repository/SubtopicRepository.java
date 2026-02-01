package com.learning.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event.ID;

import com.learning.platform.entity.Subtopic;

@Repository
public interface SubtopicRepository extends JpaRepository<Subtopic, String> {
	
List<Subtopic> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
    
    @Query("SELECT COUNT(s) FROM Subtopic s WHERE s.topic.course.id = :courseId")
    long countByCourseId(String courseId);
}


