package com.example.backend1640.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "FACULTY")
@Getter
@Setter
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FACULTY_NAME", nullable = false)
    private String facultyName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MANAGER_ID", referencedColumnName = "id")
    private User managerId;

    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;
}
