package com.example.backend1640.controller;

import com.example.backend1640.dto.ContributionDTO;
import com.example.backend1640.dto.CreateContributionDTO;
import com.example.backend1640.dto.ReadContributionByCoordinatorIdDTO;
import com.example.backend1640.dto.ReadContributionDTO;
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContribution(@PathVariable Long id) {
        contributionService.deleteContribution(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
