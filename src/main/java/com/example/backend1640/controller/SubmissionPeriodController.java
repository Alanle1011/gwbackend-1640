package com.example.backend1640.controller;

import com.example.backend1640.dto.*;
import com.example.backend1640.service.SubmissionPeriodService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@CrossOrigin
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

    @GetMapping
    public List<ReadSubmissionPeriodDTO> getSubmissionPeriod() {
        return submissionPeriodService.findAll();
    }

    @GetMapping("/{id}")
    public ReadSubmissionPeriodByIdDTO getSubmissionPeriodById(@PathVariable Long id) {
        return submissionPeriodService.findSubmissionPeriodById(id);
    }

    @PutMapping("/update/{id}")
    public SubmissionPeriodDTO updateSubmissionPeriod(@PathVariable Long id, @Validated @RequestBody UpdateSubmissionPeriodDTO submissionPeriodDTO) throws ParseException {
        submissionPeriodDTO.setId(id);
        return submissionPeriodService.updateSubmissionPeriod(submissionPeriodDTO);
    }

    @DeleteMapping
    public void deleteSubmissionPeriod(@RequestParam Long id) {
        submissionPeriodService.deleteSubmissionPeriod(id);
    }
}
