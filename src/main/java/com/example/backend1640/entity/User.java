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

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "USER_ROLE", nullable = false)
    @Convert(converter= UserRoleConverter.class)
    private UserRoleEnum user_role;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID", insertable=false, updatable=false)
    private Faculty faculty;

    @Column(name = "CREATED_AT", nullable = false)
    private Date created_at;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updated_at;
}
