package com.example.backend1640.service.impl;

import com.example.backend1640.constants.StatusEnum;
import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.dto.ContributionDTO;
import com.example.backend1640.dto.CreateContributionDTO;
import com.example.backend1640.dto.ReadContributionDTO;
import com.example.backend1640.entity.*;
import com.example.backend1640.exception.SubmissionPeriodNotExistsException;
import com.example.backend1640.exception.UploaderNotStudentException;
import com.example.backend1640.exception.UserNotExistsException;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.repository.DocumentRepository;
import com.example.backend1640.repository.ImageRepository;
import com.example.backend1640.repository.SubmissionPeriodRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.ContributionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContributionServiceImpl implements ContributionService {
    private final ContributionRepository contributionRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final DocumentRepository documentRepository;
    private final SubmissionPeriodRepository submissionPeriodRepository;

    public ContributionServiceImpl(ContributionRepository contributionRepository, UserRepository userRepository, ImageRepository imageRepository, DocumentRepository documentRepository, SubmissionPeriodRepository submissionPeriodRepository) {
        this.contributionRepository = contributionRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.documentRepository = documentRepository;
        this.submissionPeriodRepository = submissionPeriodRepository;
    }

    @Override
    public ContributionDTO createContribution(CreateContributionDTO contributionDTO) {
        User uploader = validateUserNotExists(contributionDTO.getUploadedUserId());
        if(uploader.getUserRole() != UserRoleEnum.STUDENT){
            throw new UploaderNotStudentException("Uploader is not a student");
        }

        //Get the coordinator manage this contribution
        User coordinator = validateUserNotExists(uploader.getFacultyId().getManagerId().getId());

        SubmissionPeriod submissionPeriod = validateSubmissionPeriodNotExists(contributionDTO.getSubmissionPeriodId());

        Contribution contribution = new Contribution();
        BeanUtils.copyProperties(contributionDTO, contribution);
        contribution.setCreatedAt(new Date());
        contribution.setUpdatedAt(new Date());
        contribution.setStatus(StatusEnum.OPEN);
        contribution.setUploadedUserId(uploader);
        contribution.setApprovedCoordinatorId(coordinator);
        contribution.setSubmissionPeriodId(submissionPeriod);

        //Save
        Contribution savedContribution = contributionRepository.save(contribution);

        //Convert back to ContributionDTO to return
        ContributionDTO returnedContribution = new ContributionDTO();
        BeanUtils.copyProperties(savedContribution, returnedContribution);
        returnedContribution.setUploadedUserId(savedContribution.getUploadedUserId().getId());
        returnedContribution.setId(savedContribution.getId());

        return returnedContribution;
    }

    @Override
    public List<ReadContributionDTO> findAll() {
        List<Contribution> contributions = contributionRepository.findAll();
        List<ReadContributionDTO> readContributionDTOS = new ArrayList<>();

        for (Contribution contribution : contributions) {
            ReadContributionDTO readContributionDTO = new ReadContributionDTO();
            readContributionDTO.setTitle(contribution.getTitle());
            readContributionDTO.setContent(contribution.getContent());
            readContributionDTO.setUploadedUserId(contribution.getUploadedUserId().getId());
            readContributionDTO.setSubmissionPeriodId(contribution.getSubmissionPeriodId().getId());

            readContributionDTOS.add(readContributionDTO);
        }

        return readContributionDTOS;
    }

    private SubmissionPeriod validateSubmissionPeriodNotExists(Long submissionPeriodId) {
        Optional<SubmissionPeriod> submissionPeriodOptional= submissionPeriodRepository.findById(submissionPeriodId);
        if (submissionPeriodOptional.isEmpty()) {
            throw new SubmissionPeriodNotExistsException(" Submission Period Not Exists");
        }
        return submissionPeriodOptional.get();
    }

    private User validateUserNotExists(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("User Not Exists");
        }
        return optionalUser.get();
    }

}
