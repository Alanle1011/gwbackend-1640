package com.example.backend1640.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "COORDINATOR_ID", referencedColumnName = "id")
    private User coordinator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CONTRIBUTION_ID", referencedColumnName = "id")
    private Contribution contribution;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;
}
