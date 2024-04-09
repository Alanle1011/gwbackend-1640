package com.example.backend1640.dto;

import com.example.backend1640.constants.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    private String name;
    private String password;
    private String email;
    private UserRoleEnum userRole;
    private String faculty;
}
