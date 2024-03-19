package com.example.backend1640.dto;

import com.example.backend1640.constants.UserRoleEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    private String message;
    private Boolean status;
    private Long userId;
    private UserRoleEnum role;
}
