package com.example.backend1640.controller;

import com.example.backend1640.dto.CommentDTO;
import com.example.backend1640.dto.ReadContributionByCoordinatorIdDTO;
import com.example.backend1640.service.CommentService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("contribution/private/{id}")
    public List<CommentDTO> getPrivateCommentByContributionId(@PathVariable Long id) {
        return commentService.findPrivateByContributionId(id);
    }
    @GetMapping("contribution/public/{id}")
    public List<CommentDTO> getPublicCommentByContributionId(@PathVariable Long id) {
        return commentService.findPublicByContributionId(id);
    }

    @PostMapping()
    public CommentDTO createCommentByCoordinatorId(@RequestParam(value = "coordinatorId", required = true) String coordinatorId,
                                                   @RequestParam(value = "contributionId", required = true) String contributionId,
                                                   @RequestBody CommentDTO commentDTO) {
        return commentService.createCommentByCoordinatorId(coordinatorId, contributionId, commentDTO);
    }
}
