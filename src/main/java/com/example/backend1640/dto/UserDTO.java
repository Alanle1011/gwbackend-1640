package com.example.backend1640.dto;

import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.entity.Faculty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String email;
    private UserRoleEnum userRole;
    private Faculty faculty;
}
