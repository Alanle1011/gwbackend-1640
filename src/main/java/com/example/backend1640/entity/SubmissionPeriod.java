package com.example.backend1640.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

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
    private Date start_date;

    @Column(name = "CLOSURE_DATE", nullable = false)
    private Timestamp closure_date;

    @Column(name = "FINAL_CLOSURE_DATE", nullable = false)
    private Date final_closure_date;

    @Column(name = "CREATED_AT", nullable = false)
    private Date created_at;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updated_at;
}
