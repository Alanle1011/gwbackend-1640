package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadContributionDTO {
    private Long id;
    private String title;
    private String content;
    private Long uploadedUserId;
    private Long submissionPeriodId;
    private Long imageId;
    private Long documentId;
}
