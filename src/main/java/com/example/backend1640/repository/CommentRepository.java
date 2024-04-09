package com.example.backend1640.repository;

import com.example.backend1640.entity.Comment;
import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getByContributionId(Long contribution_id);
}
