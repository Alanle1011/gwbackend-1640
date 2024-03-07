package com.example.backend1640.dto;

import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.entity.Faculty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    private String username;
    private String password;
    private String email;
    private UserRoleEnum user_role;
    private Long faculty;
}
