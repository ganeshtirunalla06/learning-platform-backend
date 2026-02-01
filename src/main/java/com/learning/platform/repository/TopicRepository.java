package com.learning.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.platform.entity.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {

	List<Topic> findByTitleContainingIgnoreCase(String title);
	
}
