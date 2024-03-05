package com.example.backend1640.service.impl;

import com.example.backend1640.dto.ContributionDTO;
import com.example.backend1640.dto.CreateContributionDTO;
import com.example.backend1640.entity.Contribution;
import com.example.backend1640.repository.ContributeRepository;
import com.example.backend1640.service.ContributionService;

public class ContributionServiceImpl implements ContributionService {
    private final ContributeRepository contributionRepository;

    public ContributionServiceImpl(ContributeRepository contributionRepository) {
        this.contributionRepository = contributionRepository;
    }

    @Override
    public ContributionDTO createContribution(CreateContributionDTO contributionDTO) {



        Contribution.builder()
                .uploaded_user(contributionDTO.getUploadedUserId())

                .build()
    }
}
