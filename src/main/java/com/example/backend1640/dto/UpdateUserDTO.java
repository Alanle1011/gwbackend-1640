package com.example.backend1640.dto;

import com.example.backend1640.constants.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private Long id;
    private String name;
    private String oldPassword;
    private String newPassword;
    private String email;
    private UserRoleEnum userRole;
    private String faculty;
}
