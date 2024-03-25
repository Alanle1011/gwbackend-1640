package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadContributionByCoordinatorIdDTO {
    private Long approvedCoordinatorId;
    private String title;
    private String content;
    private Long uploadedUserId;
    private String submissionPeriod;
    private Long imageId;
    private Long documentId;
}
