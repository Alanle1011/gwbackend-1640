package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SubmissionPeriodDTO {
    private String name;
    private Date startDate;
    private Date closureDate;
    private Date finalClosureDate;

    //Check if this is correct
}
