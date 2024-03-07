package com.example.backend1640.repository;

import com.example.backend1640.entity.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution , Long> {
    Optional<Contribution> findById(Long id);
}
