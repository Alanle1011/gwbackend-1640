package com.example.backend1640.service;

import com.example.backend1640.dto.ContributionDTO;
import com.example.backend1640.dto.CreateContributionDTO;
import com.example.backend1640.dto.ReadContributionDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ContributionService {
    ContributionDTO createContribution(CreateContributionDTO contributionDTO);

    List<ReadContributionDTO> findAll();
}
