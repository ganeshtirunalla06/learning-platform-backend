package com.learning.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.platform.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
	
	List<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
	

}
