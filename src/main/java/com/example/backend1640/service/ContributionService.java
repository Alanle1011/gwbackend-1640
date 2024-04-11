package com.example.backend1640.service;

import com.example.backend1640.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;


public interface ContributionService {
    ContributionDTO createContribution(CreateContributionDTO contributionDTO);

    ReadContributionDTO findOne(Long id);

    List<ReadContributionDTO> findAll();

    List<ReadContributionByCoordinatorIdDTO> findByCoordinatorId(Long id);

    List<ReadContributionPendingByCoordinatorIdDTO> findPendingContributionsByCoordinatorId(Long id);

    List<ReadContributionByUserIdDTO> findByUserId(Long id);

    List<ReadContributionByStatusApprovedDTO> findByStatusApproved(String status);

    void deleteContribution(Long id);

    ContributionDTO updateContribution(UpdateContributionDTO contributionDTO);

    void setContributionStatus(Long id, String status) throws JsonProcessingException;
}
