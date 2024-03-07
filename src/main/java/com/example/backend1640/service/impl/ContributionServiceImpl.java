package com.example.backend1640.service.impl;

import com.example.backend1640.constants.StatusEnum;
import com.example.backend1640.dto.ContributionDTO;
import com.example.backend1640.dto.CreateContributionDTO;
import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.User;
import com.example.backend1640.exception.ContributionAlreadyExistsException;
import com.example.backend1640.repository.ContributeRepository;
import com.example.backend1640.service.ContributionService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContributionServiceImpl implements ContributionService {
    private final ContributeRepository contributionRepository;

    public ContributionServiceImpl(ContributeRepository contributionRepository) {
        this.contributionRepository = contributionRepository;
    }

    @Override
    public ContributionDTO createContribution(CreateContributionDTO contributionDTO) {
        Contribution contribution = new Contribution();
        BeanUtils.copyProperties(contributionDTO, contribution);
        contribution.setCreated_at(new Date());
        contribution.setUpdated_at(new Date());
        contribution.setStatus(StatusEnum.OPEN);
        contribution.setApproved_coordinator_id(null);//TESTTTT
        contribution.setUploaded_user_id(null);//TESTTT

        //Save
        Contribution savedContribution = contributionRepository.save(contribution);

        //Convert back to ContributionDTO to return
        ContributionDTO returnedContribution = new ContributionDTO();
        BeanUtils.copyProperties(savedContribution, returnedContribution);

        return returnedContribution;
    }

}
