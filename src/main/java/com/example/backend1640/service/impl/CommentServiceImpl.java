package com.example.backend1640.service.impl;

import com.example.backend1640.constants.StatusEnum;
import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.dto.CommentDTO;
import com.example.backend1640.entity.Comment;
import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.User;
import com.example.backend1640.exception.ContributionNotExistsException;
import com.example.backend1640.exception.UserNotExistsException;
import com.example.backend1640.repository.CommentRepository;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ContributionRepository contributionRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, ContributionRepository contributionRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.contributionRepository = contributionRepository;
    }

    @Override
    public List<CommentDTO> findPrivateByContributionId(Long id) {
        List<Comment> commentList = commentRepository.getByContributionId(id);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentList) {
            if(!comment.getIsPublishedContribution()){
                new CommentDTO();
                CommentDTO commentDTO = CommentDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .coordinatorName(comment.getUser().getName())
                        .createdAt(comment.getCreatedAt())
                        .build();
                commentDTOList.add(commentDTO);
            }
        }
        return commentDTOList;
    }

    @Override
    public List<CommentDTO> findPublicByContributionId(Long id) {
        List<Comment> commentList = commentRepository.getByContributionId(id);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentList) {
            if(comment.getIsPublishedContribution()){
                new CommentDTO();
                CommentDTO commentDTO = CommentDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .coordinatorName(comment.getUser().getName())
                        .createdAt(comment.getCreatedAt())
                        .build();
                commentDTOList.add(commentDTO);
            }
        }
        return commentDTOList;
    }

    @Override
    public CommentDTO createCommentByCoordinatorId(String coordinatorId, String contributionId, CommentDTO commentDTO) {
        User coordinator = validateCoordinatorNotExists(Long.valueOf(coordinatorId));
        Contribution contribution = validateContributionNotExists(Long.valueOf(contributionId));
        Comment comment;

        if (contribution.getStatus() == StatusEnum.PUBLISHED) {
            comment = Comment.builder()
                    .user(coordinator)
                    .contribution(contribution)
                    .content(commentDTO.getContent())
                    .isPublishedContribution(true)
                    .createdAt(new Date())
                    .updatedAt(new Date()).build();
        } else {
            comment = Comment.builder()
                    .user(coordinator)
                    .contribution(contribution)
                    .content(commentDTO.getContent())
                    .isPublishedContribution(false)
                    .createdAt(new Date())
                    .updatedAt(new Date()).build();
        }

        Comment savedComment = commentRepository.save(comment);
        CommentDTO responseCommentDTO = new CommentDTO();
        BeanUtils.copyProperties(savedComment, responseCommentDTO);

        return responseCommentDTO;
    }

    private User validateCoordinatorNotExists(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("User Not Exists");
        }

        if (optionalUser.get().getUserRole() != UserRoleEnum.COORDINATOR) {
            throw new UserNotExistsException("User Is Not Coordinator");

        }
        return optionalUser.get();
    }

    private Contribution validateContributionNotExists(Long id) {
        Optional<Contribution> contributionOptional = contributionRepository.findById(id);
        if (contributionOptional.isEmpty()) {
            throw new ContributionNotExistsException("Contribution Not Exists");
        }
        return contributionOptional.get();
    }
}
