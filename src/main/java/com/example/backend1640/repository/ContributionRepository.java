package com.example.backend1640.repository;

import com.example.backend1640.constants.StatusEnum;
import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.SubmissionPeriod;
import com.example.backend1640.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution , Long> {
    Optional<Contribution> findById(Long id);

    List<Contribution> findByUploadedUserId(User user);

    List<Contribution> findByApprovedCoordinatorId(User coordinator);

    List<Contribution> findByStatus(StatusEnum statusEnum);

    List<Contribution> findByApprovedCoordinatorIdAndStatusIn(User coordinator, List<StatusEnum> statuses);

    List<Contribution> findBySubmissionPeriodId(Optional<SubmissionPeriod> submissionPeriod);
}
