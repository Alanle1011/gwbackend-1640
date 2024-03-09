package com.example.backend1640.service;

import com.example.backend1640.dto.CreateSubmissionPeriodDTO;
import com.example.backend1640.dto.SubmissionPeriodDTO;
import com.example.backend1640.entity.SubmissionPeriod;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface SubmissionPeriodService {
    SubmissionPeriodDTO createSubmissionPeriod(CreateSubmissionPeriodDTO submissionPeriodDTO);
}
