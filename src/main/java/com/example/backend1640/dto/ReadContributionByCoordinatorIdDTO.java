package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReadContributionByCoordinatorIdDTO {
    private Long id;
    private String title;
    private String content;
    private Long uploadedUserId;
    private String uploadedUserName;
    private String submissionPeriod;
    private Long imageId;
    private Long documentId;
    private Date createdAt;
}
