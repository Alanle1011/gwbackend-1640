package com.example.backend1640.controller;

import com.example.backend1640.dto.CreateSubmissionPeriodDTO;
import com.example.backend1640.dto.SubmissionPeriodDTO;
import com.example.backend1640.service.SubmissionPeriodService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("submission_period")
public class SubmissionPeriodController {
    private final SubmissionPeriodService submissionPeriodService;

    public SubmissionPeriodController(SubmissionPeriodService submissionPeriodService) {
        this.submissionPeriodService = submissionPeriodService;
    }

    @PostMapping
    public SubmissionPeriodDTO createSubmissionPeriod(@Validated @RequestBody CreateSubmissionPeriodDTO submissionPeriodDTO) throws ParseException {
        return submissionPeriodService.createSubmissionPeriod(submissionPeriodDTO);
    }
}
