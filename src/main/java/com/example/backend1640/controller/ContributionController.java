package com.example.backend1640.controller;

import com.example.backend1640.dto.ContributionDTO;
import com.example.backend1640.dto.CreateContributionDTO;
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
    public ResponseEntity<List<ReadContributionDTO>> getAllContributions() {
        List<ReadContributionDTO> contributions = contributionService.findAll();
        return new ResponseEntity<>(contributions, HttpStatus.OK);
    }
}
