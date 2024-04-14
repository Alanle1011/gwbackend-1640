package com.example.backend1640.service;

import com.example.backend1640.dto.CreateSubmissionPeriodDTO;
import com.example.backend1640.dto.ReadSubmissionPeriodDTO;
import com.example.backend1640.dto.SubmissionPeriodDTO;
import com.example.backend1640.dto.UpdateSubmissionPeriodDTO;

import java.text.ParseException;
import java.util.List;


public interface SubmissionPeriodService {
    SubmissionPeriodDTO createSubmissionPeriod(CreateSubmissionPeriodDTO submissionPeriodDTO) throws ParseException;

    List<ReadSubmissionPeriodDTO> findAll();

    void deleteSubmissionPeriod(Long id);

    SubmissionPeriodDTO updateSubmissionPeriod(UpdateSubmissionPeriodDTO submissionPeriodDTO) throws ParseException;

    void createNewSubmissionPeriod();
}
