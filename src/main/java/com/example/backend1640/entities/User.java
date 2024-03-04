package com.example.backend1640.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "USER_ROLE", nullable = false)
    private String user_role;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID")
    private Faculty faculty;

    @Column(name = "CREATED_AT", nullable = false)
    private Timestamp created_at;

    @Column(name = "UPDATED_AT", nullable = false)
    private Timestamp updated_at;
}