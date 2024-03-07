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
    @JoinColumn(name = "ID", insertable=false, updatable=false)
    private User uploaded_user_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID", insertable=false, updatable=false)
    private User approved_coordinator_id;

    @Column(name = "STUDENT_ID", nullable = false)
    private String student_id;

    @Column(name = "STUDENT_NAME", nullable = false)
    private String student_name;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @OneToOne
    @JoinColumn(name = "ID")
    private SubmissionPeriod submission_period_id;

    @Column(name = "STATUS", nullable = false)
    @Convert(converter = StatusConverter.class)
    private StatusEnum status;

    @Column(name = "CREATED_AT", nullable = false)
    private Date created_at;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updated_at;
    
}
