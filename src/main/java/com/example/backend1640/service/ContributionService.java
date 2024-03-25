package com.example.backend1640.service;

import com.example.backend1640.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ContributionService {
    ContributionDTO createContribution(CreateContributionDTO contributionDTO);

    List<ReadContributionDTO> findAll();

    List<ReadContributionByCoordinatorIdDTO> findByCoordinatorId(Long id);

    void deleteContribution(Long id);

    ContributionDTO updateContribution(UpdateContributionDTO contributionDTO);
}
