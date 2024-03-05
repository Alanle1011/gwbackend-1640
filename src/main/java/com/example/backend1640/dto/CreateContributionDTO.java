package com.example.backend1640.dto;

import com.example.backend1640.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContributionDTO {
    private Long uploadedUserId;
    private String studentId;
    private String studentName;
    private String title;
    private String content;
    private Long imageId;
    private Long documentId;
}
