package com.learning.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.platform.entity.Enrollment;
import com.learning.platform.entity.Subtopic;
import com.learning.platform.entity.SubtopicProgress;

@Repository
public interface SubtopicProgressRepository extends JpaRepository<SubtopicProgress, Long> {
	long countByEnrollment(Enrollment enrollment);
    boolean existsByEnrollmentAndSubtopic(Enrollment enrollment, Subtopic subtopic);
    List<SubtopicProgress> findByEnrollment(Enrollment enrollment);


}
