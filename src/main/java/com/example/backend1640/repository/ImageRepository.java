package com.example.backend1640.repository;

import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByContributionId(Contribution contribution);
}
