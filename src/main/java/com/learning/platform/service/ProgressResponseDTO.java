package com.learning.platform.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
class ProgressResponseDTO {
    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private long totalSubtopics;
    private long completedSubtopics;
    private double completionPercentage;
    private List<CompletedItemDTO> completedItems;
}
