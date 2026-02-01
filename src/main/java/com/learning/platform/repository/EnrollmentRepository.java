package com.learning.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.platform.entity.Course;
import com.learning.platform.entity.Enrollment;
import com.learning.platform.entity.User;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	boolean existsByUserAndCourse(User user, Course course);
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    List<Enrollment> findByUser(User user); 	

}
