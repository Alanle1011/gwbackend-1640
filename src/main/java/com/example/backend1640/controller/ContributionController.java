package com.example.backend1640.controller;

import com.example.backend1640.dto.*;
import com.example.backend1640.service.ContributionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("contribution")
public class ContributionController {
    private final ContributionService contributionService;

    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @PostMapping
    public ContributionDTO createContribution(@RequestBody CreateContributionDTO contributionDTO) {
        return contributionService.createContribution(contributionDTO);
    }

    @GetMapping("/{id}")
    public ReadContributionDTO getContribution(@PathVariable Long id) {
        return contributionService.findOne(id);
    }

    @GetMapping
    public List<ReadContributionDTO> getAllContributions() {
        return contributionService.findAll();
    }

    @GetMapping("coordinator/{id}")
    public List<ReadContributionByCoordinatorIdDTO> getContributionByCoordinatorId(@PathVariable Long id) {
        return contributionService.findByCoordinatorId(id);
    }

    @GetMapping("coordinator/{id}/pending")
    public List<ReadContributionPendingByCoordinatorIdDTO> getPendingContributionsByCoordinatorId(@PathVariable Long id) {
        return contributionService.findPendingContributionsByCoordinatorId(id);
    }

    @GetMapping("user/{id}")
    public List<ReadContributionByUserIdDTO> getContributionByUserId(@PathVariable Long id) {
        return contributionService.findByUserId(id);
    }

    @GetMapping("approved")
    public List<ReadContributionByStatusApprovedDTO> getContributionByStatusApproved() {
        return contributionService.findByStatusApproved("approved");
    }

    @PutMapping("update/{id}")
    public ContributionDTO updateContribution(@PathVariable Long id, @RequestBody UpdateContributionDTO contributionDTO) {
        contributionDTO.setId(id);
        return contributionService.updateContribution(contributionDTO);
    }

    @PutMapping("setStatus/{id}")
    public void setContributionStatus(@PathVariable Long id, @RequestBody String status) throws JsonProcessingException {
        contributionService.setContributionStatus(id, status);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteContribution(@PathVariable Long id) {
        contributionService.deleteContribution(id);
    }
}
