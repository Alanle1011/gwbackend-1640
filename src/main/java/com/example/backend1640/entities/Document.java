package com.example.backend1640.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "DOCUMENTS")
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID")
    private Contribution contribution;

    @Column(name = "PATH", nullable = false)
    private String path;

    @Column(name = "CREATED_AT", nullable = false)
    private Timestamp created_at;

    @Column(name = "UPDATED_AT", nullable = false)
    private Timestamp updated_at;
}
