package com.example.backend1640.service;

import com.example.backend1640.dto.CreateUserDTO;
import com.example.backend1640.dto.LoginDTO;
import com.example.backend1640.dto.LoginRequestDTO;
import com.example.backend1640.dto.UserDTO;


public interface UserService {

    UserDTO createUser(CreateUserDTO userDTO);

    LoginDTO loginUser(LoginRequestDTO loginRequestDTO);
}
