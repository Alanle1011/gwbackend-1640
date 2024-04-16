package com.example.backend1640.service;

import com.example.backend1640.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createCommentByCoordinatorId(String coordinatorId, String contributionId, CommentDTO commentDTO);
    List<CommentDTO> findPrivateByContributionId(Long id);
    List<CommentDTO> findPublicByContributionId(Long id);
}
