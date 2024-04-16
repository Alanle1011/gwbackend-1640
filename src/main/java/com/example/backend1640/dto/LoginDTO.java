package com.example.backend1640.dto;

import com.example.backend1640.constants.UserRoleEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    private Boolean status;
    private Long userId;
    private String name;
    private String faculty;
    private String email;
    private UserRoleEnum role;
    private Long imageId;
}
