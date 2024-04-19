package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReadContributionPendingByCoordinatorIdDTO {
    private Long id;
    private String title;
    private String content;
    private Long uploadedUserId;
    private String uploadedUserName;
    private Long uploadedUserImageId;
    private String submissionPeriod;
    private String faculty;
    private String status;
    private Long imageId;
    private Long documentId;
    private Date createdAt;
}
