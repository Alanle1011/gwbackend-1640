package com.example.backend1640.service.impl;

import com.example.backend1640.constants.StatusEnum;
import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.dto.*;
import com.example.backend1640.entity.*;
import com.example.backend1640.exception.*;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.repository.DocumentRepository;
import com.example.backend1640.repository.ImageRepository;
import com.example.backend1640.repository.SubmissionPeriodRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.ContributionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContributionServiceImpl implements ContributionService {
    private final ContributionRepository contributionRepository;
    private final UserRepository userRepository;
    private final SubmissionPeriodRepository submissionPeriodRepository;
    private final ImageRepository imageRepository;
    private final DocumentRepository documentRepository;

    public ContributionServiceImpl(ContributionRepository contributionRepository, UserRepository userRepository, ImageRepository imageRepository, DocumentRepository documentRepository, SubmissionPeriodRepository submissionPeriodRepository) {
        this.contributionRepository = contributionRepository;
        this.userRepository = userRepository;
        this.submissionPeriodRepository = submissionPeriodRepository;
        this.imageRepository = imageRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public ContributionDTO createContribution(CreateContributionDTO contributionDTO) {
        User uploader = validateUserNotExists(contributionDTO.getUploadedUserId());
        if(uploader.getUserRole() != UserRoleEnum.STUDENT){
            throw new UploaderNotStudentException("Uploader is not a student");
        }

        //Get the coordinator manage this contribution
        User coordinator = validateUserNotExists(uploader.getFacultyId().getCoordinatorId().getId());

        List<SubmissionPeriod> submissionPeriods = submissionPeriodRepository.findAll();

        Date today = new Date();
        SubmissionPeriod validSubmissionPeriod = null;

        for (SubmissionPeriod submissionPeriod : submissionPeriods) {
            if (today.after(submissionPeriod.getStartDate()) && today.before(submissionPeriod.getClosureDate())) {
                validSubmissionPeriod = submissionPeriod;
                break;
            }
        }

        if (validSubmissionPeriod == null) {
            throw new SubmissionPeriodNotExistsException("Submission Period Not Exists");
        }

        Contribution contribution = new Contribution();
        BeanUtils.copyProperties(contributionDTO, contribution);
        contribution.setCreatedAt(new Date());
        contribution.setUpdatedAt(new Date());
        contribution.setStatus(StatusEnum.OPEN);
        contribution.setUploadedUserId(uploader);
        contribution.setApprovedCoordinatorId(coordinator);
        contribution.setSubmissionPeriodId(validSubmissionPeriod);

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
    @Transactional
    public List<ReadContributionDTO> findAll() {
        List<Contribution> contributions = contributionRepository.findAll();
        List<ReadContributionDTO> readContributionDTOS = new ArrayList<>();

        for (Contribution contribution : contributions) {
            Image image = imageRepository.findByContributionId(contribution);
            Document document = documentRepository.findByContributionId(contribution);
            ReadContributionDTO readContributionDTO = new ReadContributionDTO();
            readContributionDTO.setTitle(contribution.getTitle());
            readContributionDTO.setContent(contribution.getContent());
            readContributionDTO.setUploadedUserId(contribution.getUploadedUserId().getId());
            readContributionDTO.setSubmissionPeriodId(contribution.getSubmissionPeriodId().getId());
            if (image != null) {
                readContributionDTO.setImageId(image.getId());
            }
            if (document != null) {
                readContributionDTO.setDocumentId(document.getId());
            }

            readContributionDTOS.add(readContributionDTO);
        }

        return readContributionDTOS;
    }

    @Override
    @Transactional
    public List<ReadContributionByCoordinatorIdDTO> findByCoordinatorId(Long id) {
        User coordinator = validateUserNotExists(id);
        List<Contribution> contributions = contributionRepository.findByApprovedCoordinatorId(coordinator);
        List<ReadContributionByCoordinatorIdDTO> readContributionByCoordinatorIdDTOS = new ArrayList<>();

        for (Contribution contribution : contributions) {
            Image image = imageRepository.findByContributionId(contribution);
            Document document = documentRepository.findByContributionId(contribution);
            ReadContributionByCoordinatorIdDTO readContributionByCoordinatorIdDTO = new ReadContributionByCoordinatorIdDTO();
            readContributionByCoordinatorIdDTO.setApprovedCoordinatorId(coordinator.getId());
            readContributionByCoordinatorIdDTO.setTitle(contribution.getTitle());
            readContributionByCoordinatorIdDTO.setContent(contribution.getContent());
            readContributionByCoordinatorIdDTO.setUploadedUserId(contribution.getUploadedUserId().getId());
            readContributionByCoordinatorIdDTO.setSubmissionPeriod(contribution.getSubmissionPeriodId().getName());
            if (image != null) {
                readContributionByCoordinatorIdDTO.setImageId(image.getId());
            }
            if (document != null) {
                readContributionByCoordinatorIdDTO.setDocumentId(document.getId());
            }

            readContributionByCoordinatorIdDTOS.add(readContributionByCoordinatorIdDTO);
        }

        return readContributionByCoordinatorIdDTOS;
    }

    @Override
    public void deleteContribution(Long id) {
        Contribution contribution = validateContributionNotExists(id);
        contributionRepository.delete(contribution);
    }

    @Override
    public ContributionDTO updateContribution(UpdateContributionDTO contributionDTO) {
        Contribution contribution = validateContributionNotExists(contributionDTO.getId());

        if (contributionDTO.getTitle() != null)
            contribution.setTitle(contributionDTO.getTitle());
        if (contributionDTO.getContent() != null)
            contribution.setContent(contributionDTO.getContent());

        contribution.setUpdatedAt(new Date());

        Contribution savedContribution = contributionRepository.save(contribution);
        ContributionDTO responseContributionDTO = new ContributionDTO();
        BeanUtils.copyProperties(savedContribution, responseContributionDTO);
        responseContributionDTO.setUploadedUserId(savedContribution.getUploadedUserId().getId());
        responseContributionDTO.setId(savedContribution.getId());

        return responseContributionDTO;
    }

    private Contribution validateContributionNotExists(Long id) {
        Optional<Contribution> contributionOptional = contributionRepository.findById(id);
        if (contributionOptional.isEmpty()) {
            throw new ContributionNotExistsException("Contribution Not Exists");
        }
        return contributionOptional.get();
    }

    private User validateUserNotExists(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("User Not Exists");
        }
        return optionalUser.get();
    }
}
