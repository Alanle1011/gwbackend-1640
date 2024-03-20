package com.example.backend1640.entity;

import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.entity.converters.UserRoleConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "USER_ROLE", nullable = false)
    @Convert(converter= UserRoleConverter.class)
    private UserRoleEnum userRole;

    @ManyToOne
    @JoinColumn(name = "FACULTY_ID", referencedColumnName = "id")
    private Faculty facultyId;

    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;
}
