package com.example.backend1640.service.impl;

import com.example.backend1640.constants.StatusEnum;
import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.dto.*;
import com.example.backend1640.entity.*;
import com.example.backend1640.entity.converters.StatusConverter;
import com.example.backend1640.exception.*;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.repository.DocumentRepository;
import com.example.backend1640.repository.ImageRepository;
import com.example.backend1640.repository.SubmissionPeriodRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.ContributionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

@Service
public class ContributionServiceImpl implements ContributionService {
    private final ContributionRepository contributionRepository;
    private final UserRepository userRepository;
    private final SubmissionPeriodRepository submissionPeriodRepository;
    private final ImageRepository imageRepository;
    private final DocumentRepository documentRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.contribution}")
    private String contributionRountingKey;

    public ContributionServiceImpl(ContributionRepository contributionRepository, UserRepository userRepository, ImageRepository imageRepository, DocumentRepository documentRepository, SubmissionPeriodRepository submissionPeriodRepository, RabbitTemplate rabbitTemplate) {
        this.contributionRepository = contributionRepository;
        this.userRepository = userRepository;
        this.submissionPeriodRepository = submissionPeriodRepository;
        this.imageRepository = imageRepository;
        this.documentRepository = documentRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ContributionDTO createContribution(CreateContributionDTO contributionDTO) {
        User uploader = validateUserNotExists(contributionDTO.getUploadedUserId());
        if (uploader.getUserRole() != UserRoleEnum.STUDENT) {
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

        //Send Email
        rabbitTemplate.convertAndSend(exchange,
                contributionRountingKey,
                EmailDetails.builder().
                        messageBody("Contribution created Successful with mail: " + uploader.getEmail() + "\n" +
                                "Student Name: " + uploader.getName() + "\n" +
                                "Title: " + savedContribution.getTitle() + "\n" +
                                "Created at: " + savedContribution.getCreatedAt() + "\n")
                        .recipient(coordinator.getEmail())
                        .subject("CONTRIBUTION CREATED SUCCESS")
                        .build());

        //Convert back to ContributionDTO to return
        ContributionDTO returnedContribution = new ContributionDTO();
        BeanUtils.copyProperties(savedContribution, returnedContribution);
        returnedContribution.setUploadedUserId(savedContribution.getUploadedUserId().getId());
        returnedContribution.setId(savedContribution.getId());
        returnedContribution.setSubmissionPeriod(savedContribution.getSubmissionPeriodId().getName());

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
            readContributionDTO.setId(contribution.getId());
            readContributionDTO.setApprovedCoordinator(contribution.getApprovedCoordinatorId().getName());
            readContributionDTO.setTitle(contribution.getTitle());
            readContributionDTO.setContent(contribution.getContent());
            readContributionDTO.setUploadedUserId(contribution.getUploadedUserId().getId());
            readContributionDTO.setUploadedUserName(contribution.getUploadedUserId().getName());
            readContributionDTO.setSubmissionPeriod(contribution.getSubmissionPeriodId().getName());
            readContributionDTO.setFaculty(contribution.getUploadedUserId().getFacultyId().getFacultyName());
            readContributionDTO.setStatus(contribution.getStatus().toString());
            if (image != null) {
                readContributionDTO.setImageId(image.getId());
            }
            if (document != null) {
                readContributionDTO.setDocumentId(document.getId());
            }
            readContributionDTO.setCreatedAt(contribution.getCreatedAt());

            readContributionDTOS.add(readContributionDTO);
        }

        readContributionDTOS.sort(Comparator.comparing(ReadContributionDTO::getCreatedAt));

        return readContributionDTOS;
    }

    @Override
    @Transactional
    public List<ReadContributionByCoordinatorIdDTO> findByCoordinatorId(Long id) {
        User coordinator = validateUserNotExists(id);
        if (coordinator.getUserRole() != UserRoleEnum.COORDINATOR) {
            throw new UserNotCoordinatorException("User is not Coordinator");
        }
        List<Contribution> contributions = contributionRepository.findByApprovedCoordinatorId(coordinator);
        List<ReadContributionByCoordinatorIdDTO> readContributionByCoordinatorIdDTOS = new ArrayList<>();

        for (Contribution contribution : contributions) {
            Image image = imageRepository.findByContributionId(contribution);
            Document document = documentRepository.findByContributionId(contribution);
            ReadContributionByCoordinatorIdDTO readContributionByCoordinatorIdDTO = new ReadContributionByCoordinatorIdDTO();
            readContributionByCoordinatorIdDTO.setId(contribution.getId());
            readContributionByCoordinatorIdDTO.setTitle(contribution.getTitle());
            readContributionByCoordinatorIdDTO.setContent(contribution.getContent());
            readContributionByCoordinatorIdDTO.setUploadedUserId(contribution.getUploadedUserId().getId());
            readContributionByCoordinatorIdDTO.setUploadedUserName(contribution.getUploadedUserId().getName());
            readContributionByCoordinatorIdDTO.setSubmissionPeriod(contribution.getSubmissionPeriodId().getName());
            readContributionByCoordinatorIdDTO.setFaculty(contribution.getUploadedUserId().getFacultyId().getFacultyName());
            readContributionByCoordinatorIdDTO.setStatus(contribution.getStatus().toString());
            if (image != null) {
                readContributionByCoordinatorIdDTO.setImageId(image.getId());
            }
            if (document != null) {
                readContributionByCoordinatorIdDTO.setDocumentId(document.getId());
            }
            readContributionByCoordinatorIdDTO.setCreatedAt(contribution.getCreatedAt());

            readContributionByCoordinatorIdDTOS.add(readContributionByCoordinatorIdDTO);
        }

        readContributionByCoordinatorIdDTOS.sort(Comparator.comparing(ReadContributionByCoordinatorIdDTO::getCreatedAt));

        return readContributionByCoordinatorIdDTOS;
    }

    @Override
    @Transactional
    public List<ReadContributionByStatusApprovedDTO> findByStatusApproved(String status) {
        StatusEnum statusEnum = StatusEnum.valueOf(status.toUpperCase());
        List<Contribution> contributions = contributionRepository.findByStatus(statusEnum);
        List<ReadContributionByStatusApprovedDTO> readContributionByStatusApprovedDTOS = new ArrayList<>();

        for (Contribution contribution : contributions) {
            Image image = imageRepository.findByContributionId(contribution);
            Document document = documentRepository.findByContributionId(contribution);
            ReadContributionByStatusApprovedDTO readContributionByStatusApprovedDTO = new ReadContributionByStatusApprovedDTO();
            readContributionByStatusApprovedDTO.setId(contribution.getId());
            readContributionByStatusApprovedDTO.setApprovedCoordinator(contribution.getApprovedCoordinatorId().getName());
            readContributionByStatusApprovedDTO.setTitle(contribution.getTitle());
            readContributionByStatusApprovedDTO.setContent(contribution.getContent());
            readContributionByStatusApprovedDTO.setUploadedUserId(contribution.getUploadedUserId().getId());
            readContributionByStatusApprovedDTO.setUploadedUserName(contribution.getUploadedUserId().getName());
            readContributionByStatusApprovedDTO.setSubmissionPeriod(contribution.getSubmissionPeriodId().getName());
            readContributionByStatusApprovedDTO.setFaculty(contribution.getUploadedUserId().getFacultyId().getFacultyName());
            readContributionByStatusApprovedDTO.setStatus(contribution.getStatus().toString());
            if (image != null) {
                readContributionByStatusApprovedDTO.setImageId(image.getId());
            }
            if (document != null) {
                readContributionByStatusApprovedDTO.setDocumentId(document.getId());
            }
            readContributionByStatusApprovedDTO.setCreatedAt(contribution.getCreatedAt());

            readContributionByStatusApprovedDTOS.add(readContributionByStatusApprovedDTO);
        }

        readContributionByStatusApprovedDTOS.sort(Comparator.comparing(ReadContributionByStatusApprovedDTO::getCreatedAt));

        return readContributionByStatusApprovedDTOS;
    }

    @Override
    @Transactional
    public void deleteContribution(Long id) {
        Contribution contribution = validateContributionNotExists(id);
        Image image = imageRepository.findByContributionId(contribution);
        if (image != null) {
            imageRepository.delete(image);
        }
        Document document = documentRepository.findByContributionId(contribution);
        if (document != null) {
            documentRepository.delete(document);
        }
        contributionRepository.delete(contribution);
    }

    @Override
    public ContributionDTO updateContribution(UpdateContributionDTO contributionDTO) {
        Contribution contribution = validateContributionNotExists(contributionDTO.getId());

        if (contributionDTO.getTitle() != null) contribution.setTitle(contributionDTO.getTitle());
        if (contributionDTO.getContent() != null) contribution.setContent(contributionDTO.getContent());

        contribution.setUpdatedAt(new Date());

        Contribution savedContribution = contributionRepository.save(contribution);
        ContributionDTO responseContributionDTO = new ContributionDTO();
        BeanUtils.copyProperties(savedContribution, responseContributionDTO);
        responseContributionDTO.setUploadedUserId(savedContribution.getUploadedUserId().getId());
        responseContributionDTO.setId(savedContribution.getId());

        return responseContributionDTO;
    }

    @Override
    public void setContributionStatus(Long id, String status) throws JsonProcessingException {
        Contribution contribution = validateContributionNotExists(id);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode statusNode = objectMapper.readTree(status);

        String statusString = statusNode.get("status").asText().toUpperCase();
        StatusConverter statusConverter = new StatusConverter();
        StatusEnum statusEnum = statusConverter.convertToEntityAttribute(statusString);

        contribution.setStatus(statusEnum);
        contributionRepository.save(contribution);
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
