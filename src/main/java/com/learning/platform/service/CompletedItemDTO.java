package com.learning.platform.service;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
class CompletedItemDTO { String subtopicId; String subtopicTitle; LocalDateTime completedAt; }