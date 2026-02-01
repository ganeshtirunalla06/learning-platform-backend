 package com.learning.platform.config;

import java.util.List;

import com.learning.platform.entity.Course;

import lombok.Data;

@Data
public class CourseContainer {
	List<Course> courses;
}
