package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFacultyDTO {
    private Long id;
    private String facultyName;
    private Long coordinatorId;
}
