package com.example.backend1640.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SUBMISSION_PERIODS")
@Getter
@Setter
public class SubmissionPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "CLOSURE_DATE", nullable = false)
    private Date closureDate;

    @Column(name = "FINAL_CLOSURE_DATE", nullable = false)
    private Date finalClosureDate;

    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;
}
