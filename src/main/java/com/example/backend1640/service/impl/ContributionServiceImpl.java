package com.example.backend1640.service.impl;

import com.example.backend1640.constants.StatusEnum;
import com.example.backend1640.dto.ContributionDTO;
import com.example.backend1640.dto.CreateContributionDTO;
import com.example.backend1640.entity.Contribution;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.service.ContributionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ContributionServiceImpl implements ContributionService {
    private final ContributionRepository contributionRepository;

    public ContributionServiceImpl(ContributionRepository contributionRepository) {
        this.contributionRepository = contributionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ContributionDTO createContribution(CreateContributionDTO contributionDTO) {
        User uploader = validateUserNotExists(contributionDTO.getUploaded_user_id());
//        User coordinator = validateUserNotExists(uploader.getFaculty());

        Contribution contribution = new Contribution();
        BeanUtils.copyProperties(contributionDTO, contribution);
        contribution.setCreated_at(new Date());
        contribution.setUpdated_at(new Date());
        contribution.setStatus(StatusEnum.OPEN);
        contribution.setUploaded_user_id(uploader);

        contribution.setApproved_coordinator_id(null);//TESTTTT

        //Save
        Contribution savedContribution = contributionRepository.save(contribution);

        //Convert back to ContributionDTO to return
        ContributionDTO returnedContribution = new ContributionDTO();
        BeanUtils.copyProperties(savedContribution, returnedContribution);

        return returnedContribution;
    }

    private User validateUserNotExists(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("User Not Exists");
        }
        return optionalUser.get();
    }

}
