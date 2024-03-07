package com.example.backend1640.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateSubmissionPeriodDTO {
    private String name;
    private Date start_date;
    private Date closure_date;
    private Date final_closure_date;
}
