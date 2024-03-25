package com.example.backend1640.controller;

import com.example.backend1640.dto.*;
import com.example.backend1640.service.ContributionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<ReadContributionDTO> getAllContributions() {
        return contributionService.findAll();
    }

    @GetMapping("coordinator/{id}")
    public List<ReadContributionByCoordinatorIdDTO> getContributionByCoordinatorId(@PathVariable Long id) {
        return contributionService.findByCoordinatorId(id);
    }

    @PutMapping("update/{id}")
    public ContributionDTO updateContribution(@PathVariable Long id, @RequestBody UpdateContributionDTO contributionDTO) {
        contributionDTO.setId(id);
        return contributionService.updateContribution(contributionDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContribution(@PathVariable Long id) {
        contributionService.deleteContribution(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
