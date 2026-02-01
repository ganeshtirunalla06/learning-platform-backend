package com.learning.platform.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
class MatchDTO {
    private String type;
    private String topicTitle;
    private String subtopicId;
    private String subtopicTitle;
    private String snippet;
}