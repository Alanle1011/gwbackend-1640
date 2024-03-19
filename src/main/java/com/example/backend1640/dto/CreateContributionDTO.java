package com.example.backend1640.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContributionDTO {
    private Long uploadedUserId;
    private String title;
    private String content;
    private Long imageId;
    private Long documentId;
    private Long submissionPeriodId;
}
