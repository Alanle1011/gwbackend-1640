package com.example.backend1640.dto;

import com.example.backend1640.constants.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadUserDTO {
    private Long id;
    private UserRoleEnum userRole;
    private Long imageId;
    private String name;
    private String email;
    private String faculty;
}
