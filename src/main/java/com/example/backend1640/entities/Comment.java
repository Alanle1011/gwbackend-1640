package com.example.backend1640.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "COMMENTS")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID", insertable=false, updatable=false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID", insertable=false, updatable=false)
    private Contribution contribution;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "CREATED_AT", nullable = false)
    private Timestamp created_at;

    @Column(name = "UPDATED_AT", nullable = false)
    private Timestamp updated_at;
}
