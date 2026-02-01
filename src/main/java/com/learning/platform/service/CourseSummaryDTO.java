package com.learning.platform.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseSummaryDTO {
    private String id;
    private String title;
    private String description;
    private int topicCount;
    private long subtopicCount;
}
