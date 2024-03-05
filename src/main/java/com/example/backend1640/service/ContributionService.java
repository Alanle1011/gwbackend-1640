package com.example.backend1640.service;

import com.example.backend1640.dto.ContributionDTO;
import com.example.backend1640.dto.CreateContributionDTO;
import org.springframework.stereotype.Service;

@Service
public interface ContributionService {
    ContributionDTO createContribution(CreateContributionDTO contributionDTO);
}
