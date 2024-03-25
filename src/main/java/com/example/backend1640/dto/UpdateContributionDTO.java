package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContributionDTO {
    private Long id;
    private Long uploadedUserId;
    private String title;
    private String content;
    private Long imageId;
    private Long documentId;
    private Long submissionPeriodId;
}
