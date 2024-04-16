package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateSubmissionPeriodDTO {
    private Long id;
    private String name;
    private String startDate;
    private String closureDate;
    private String finalClosureDate;
}
