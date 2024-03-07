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
    private String faculty_name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID", insertable=false, updatable=false)
    private User manager_id;

    @Column(name = "CREATED_AT", nullable = false)
    private Date created_at;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updated_at;
}
