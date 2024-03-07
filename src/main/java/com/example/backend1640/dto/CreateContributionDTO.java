package com.example.backend1640.dto;

import com.example.backend1640.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContributionDTO {
    private Long uploaded_user_id;
    private String student_id;
    private String student_name;
    private String title;
    private String content;
    private Long imageId;
    private Long documentId;
}
