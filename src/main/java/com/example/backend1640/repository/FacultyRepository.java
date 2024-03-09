package com.example.backend1640.repository;

import com.example.backend1640.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByFacultyName(String name);
    Optional<Faculty> findById(Long id);
}
