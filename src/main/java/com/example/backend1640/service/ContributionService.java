package com.example.backend1640.service;

import com.example.backend1640.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;


public interface ContributionService {
    ContributionDTO createContribution(CreateContributionDTO contributionDTO);

    List<ReadContributionDTO> findAll();

    List<ReadContributionByCoordinatorIdDTO> findByCoordinatorId(Long id);
    List<ReadContributionByStatusApprovedDTO> findByStatusApproved(String status);

    void deleteContribution(Long id);

    ContributionDTO updateContribution(UpdateContributionDTO contributionDTO);

    void setContributionStatus(Long id, String status) throws JsonProcessingException;
}
