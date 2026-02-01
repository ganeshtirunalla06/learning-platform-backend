package com.learning.platform.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
class SearchResultDTO {
    private String courseId;
    private String courseTitle;
    private List<MatchDTO> matches = new ArrayList<>();
    public SearchResultDTO(String id, String title) { this.courseId = id; this.courseTitle = title; }
}
