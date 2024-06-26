package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReadContributionByStatusApprovedDTO {
    private Long id;
    private String approvedCoordinator;
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
    private String documentName;
    private String documentType;
    private Date createdAt;
}
