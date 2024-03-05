package com.example.backend1640.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CONTRIBUTIONS")
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

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "CREATED_AT", nullable = false)
    private Timestamp created_at;

    @Column(name = "UPDATED_AT", nullable = false)
    private Timestamp updated_at;
    
}
