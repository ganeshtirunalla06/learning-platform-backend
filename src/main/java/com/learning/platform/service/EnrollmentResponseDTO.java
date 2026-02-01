package com.learning.platform.service;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EnrollmentResponseDTO {
    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private LocalDateTime enrolledAt;
}
