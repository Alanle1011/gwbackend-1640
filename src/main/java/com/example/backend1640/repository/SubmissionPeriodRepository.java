package com.example.backend1640.repository;

import com.example.backend1640.entity.SubmissionPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionPeriodRepository extends JpaRepository<SubmissionPeriod, Long> {
    Optional<SubmissionPeriod> findByName(String name);
}
