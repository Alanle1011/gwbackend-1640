package com.example.backend1640.entity;

import com.example.backend1640.constants.StatusEnum;
import com.example.backend1640.entity.converters.StatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CONTRIBUTION")
@Getter
@Setter
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "UPLOADED_USER_ID", referencedColumnName = "id")
    private User uploadedUserId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "APPROVED_COORDINATOR_ID", referencedColumnName = "id")
    private User approvedCoordinatorId;

    @Column(name = "STUDENT_ID", nullable = false)
    private String studentId;

    @Column(name = "STUDENT_NAME", nullable = false)
    private String studentName;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @OneToOne
    @JoinColumn(name = "SUBMISSION_PERIOD_ID", referencedColumnName = "id")
    private SubmissionPeriod submissionPeriodId;

    @Column(name = "STATUS", nullable = false)
    @Convert(converter = StatusConverter.class)
    private StatusEnum status;

    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;
    
}
