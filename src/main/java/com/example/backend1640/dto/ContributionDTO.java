package com.example.backend1640.dto;

import com.example.backend1640.entity.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionDTO {
    private Long id;
    private Long uploadedUserId;
    private String title;
    private String content;
    private Long imageId;
    private Long documentId;
    private Date createdAt;
    private Date updatedAt;
    private String submissionPeriod;
}
