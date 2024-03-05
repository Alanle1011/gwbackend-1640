package com.example.backend1640.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "FACULTIES")
@Getter
@Setter
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FACULTY_NAME", nullable = false)
    private String faculty_name;

    @Column(name = "CREATED_AT", nullable = false)
    private Date created_at;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updated_at;
}
