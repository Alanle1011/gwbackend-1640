package com.example.backend1640.service.impl;

import com.example.backend1640.dto.CreateSubmissionPeriodDTO;
import com.example.backend1640.dto.SubmissionPeriodDTO;
import com.example.backend1640.entity.SubmissionPeriod;
import com.example.backend1640.exception.UserAlreadyExistsException;
import com.example.backend1640.repository.SubmissionPeriodRepository;
import com.example.backend1640.service.SubmissionPeriodService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class SubmissionPeriodServiceImpl implements SubmissionPeriodService {
    private final SubmissionPeriodRepository submissionPeriodRepository;

    public SubmissionPeriodServiceImpl(SubmissionPeriodRepository submissionPeriodRepository) {
        this.submissionPeriodRepository = submissionPeriodRepository;
    }

    @Override
    public SubmissionPeriodDTO createSubmissionPeriod(CreateSubmissionPeriodDTO submissionPeriodDTO) {
        validateSubmissionPeriodExists(submissionPeriodDTO.getName());
        SubmissionPeriod submissionPeriod = new SubmissionPeriod();
        BeanUtils.copyProperties(submissionPeriodDTO, submissionPeriod);
        submissionPeriod.setCreatedAt(new Date());
        submissionPeriod.setUpdatedAt(new Date());

        //Save Submission Period
        SubmissionPeriod savedSubmissionPeriod = submissionPeriodRepository.save(submissionPeriod);
        SubmissionPeriodDTO responseSubmissionPeriodDTO = new SubmissionPeriodDTO();
        BeanUtils.copyProperties(savedSubmissionPeriod, responseSubmissionPeriodDTO);

        return responseSubmissionPeriodDTO;
    }

    private void validateSubmissionPeriodExists(String name) {
        Optional<SubmissionPeriod> optionalSubmissionPeriod = submissionPeriodRepository.findByName(name);

        if (optionalSubmissionPeriod.isPresent()) {
            throw new UserAlreadyExistsException("SubmissionPeriodAlreadyExists");
        }
    }
}
