package com.example.backend1640.service;

import com.example.backend1640.dto.*;

import java.text.ParseException;
import java.util.List;


public interface SubmissionPeriodService {
    SubmissionPeriodDTO createSubmissionPeriod(CreateSubmissionPeriodDTO submissionPeriodDTO) throws ParseException;

    List<ReadSubmissionPeriodDTO> findAll();

    void deleteSubmissionPeriod(Long id);

    SubmissionPeriodDTO updateSubmissionPeriod(UpdateSubmissionPeriodDTO submissionPeriodDTO) throws ParseException;

    void createNewSubmissionPeriod();

    ReadSubmissionPeriodByIdDTO findSubmissionPeriodById(Long id);
}
